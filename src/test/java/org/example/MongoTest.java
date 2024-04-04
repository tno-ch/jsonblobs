/*
 * Copyright TNO Geologische Dienst Nederland
 *
 * Alle rechten voorbehouden.
 * Niets uit deze software mag worden vermenigvuldigd en/of openbaar gemaakt door middel van druk, fotokopie,
 * microfilm of op welke andere wijze dan ook, zonder voorafgaande toestemming van TNO.
 *
 * Indien deze software in opdracht werd uitgebracht, wordt voor de rechten en verplichtingen van opdrachtgever
 * en opdrachtnemer verwezen naar de Algemene Voorwaarden voor opdrachten aan TNO, dan wel de betreffende
 * terzake tussen de partijen gesloten overeenkomst.
 */
package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.mongodb.EntityMongo;
import org.example.mongodb.EntityMongoCodecService;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Slf4j
public class MongoTest {

    public static final int MAX = 100;

    //CHECKSTYLE:OFF
    @Inject
    EntityMongoCodecService service;
    //https://www.mongodb.com/developer/products/mongodb/mongodb-network-compression/
    //CHECKSTYLE:ON

    @Test
    public void testAllJson() throws IOException {
        String jsonSmall = IOUtils.toString( this.getClass().getResourceAsStream( "/data_small.json" ), StandardCharsets.UTF_8 );   //   643 kb op file -  1.3MB in mongo
        String jsonMedium = IOUtils.toString( this.getClass().getResourceAsStream( "/data_medium.json" ), StandardCharsets.UTF_8 ); // 7.642 kb op file - 15.6MB in mongo
        String jsonLarge = IOUtils.toString( this.getClass().getResourceAsStream( "/data_large.json" ), StandardCharsets.UTF_8 );   //12.472 kb op file - 25.5MB in mongo

        Instant start = Instant.now();
        insert( jsonSmall, MAX, Type.SMALL );
        insert( jsonMedium, MAX, Type.MEDIUM );
        insert( jsonLarge, MAX, Type.LARGE );
        log.info( "Finished inserting total: {}ms", Duration.between( start, Instant.now() ).toMillis() );

        log.info( "start reading" );
        start = Instant.now();
        read( MAX, Type.SMALL );
        read( MAX, Type.MEDIUM );
        read( MAX, Type.LARGE );
        log.info( "Finished reading total: {}ms", Duration.between( start, Instant.now() ).toMillis() );
    }

    private void insert(String json, int max, Type type) {

        Instant start = Instant.now();
        for ( int i = 0; i < max; i++ ) {
            service.add( EntityMongo.builder()
                                    .name( type.name() + "-" + i )
                                    .payload( json )
                                    .registrationObjectDbk( i )
                                    .build() );
        }
        long total = Duration.between( start, Instant.now() ).toMillis();
        long avg = total / max;
        log.info( "Finished inserting {} items => json {}: {}ms (avg={}ms)", max, type, total, avg );
    }

    private void read(int max, Type type) {
        Instant start = Instant.now();
        for ( int i = 0; i < max; i++ ) {
            String name = type.name() + "-" + i;
            EntityMongo byName = service.findByName( name );
            assertThat( byName ).isNotNull();
            assertThat( byName.getPayload() ).isNotNull();
        }
        long total = Duration.between( start, Instant.now() ).toMillis();
        long avg = total / max;
        log.info( "Finished reading {} items => json {}: {}ms (avg={}ms)", max, type, total, avg );
    }

    //    SELECT
    //          (select count(*) from bro_dba.bro_entity_value e) AS total,
    //          (select count(*) from bro_dba.bro_entity_value e where dbms_lob.getlength(json_value) < 800000) AS small,
    //          (select count(*) from bro_dba.bro_entity_value e where dbms_lob.getlength(json_value) >= 800000 and dbms_lob.getlength(json_value) < 6000000) AS medium,
    //          (select count(*) from bro_dba.bro_entity_value e where dbms_lob.getlength(json_value) >= 6000000) AS big
    //    FROM    dual

    enum Type {
        SMALL,
        MEDIUM,
        LARGE
    }

}
