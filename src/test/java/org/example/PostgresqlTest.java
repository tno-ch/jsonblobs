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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.jsonb.EntityJsonb;
import org.example.jsonb.EntityValueJsonb;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Slf4j
public class PostgresqlTest {

    @PersistenceContext
    private EntityManager em;

    public static final int MAX = 100;

    @Test
    @Transactional
    void testAllJson() throws IOException {
        String jsonSmall = IOUtils.toString( this.getClass().getResourceAsStream( "/data_small.json" ), StandardCharsets.UTF_8 );   //   643 kb op file -  1.3MB in mongo
        String jsonMedium = IOUtils.toString( this.getClass().getResourceAsStream( "/data_medium.json" ), StandardCharsets.UTF_8 ); // 7.642 kb op file - 15.6MB in mongo
        String jsonLarge = IOUtils.toString( this.getClass().getResourceAsStream( "/data_large.json" ), StandardCharsets.UTF_8 );   //12.472 kb op file - 25.5MB in mongo

        Instant start = Instant.now();
        insert( jsonSmall, MAX, Type.SMALL );
        insert( jsonMedium, MAX, Type.MEDIUM );
        insert( jsonLarge, MAX, Type.LARGE );
        log.info( "Finished inserting total: {}ms", Duration.between( start, Instant.now() ).toMillis() );
        //start = Instant.now();
        //em.flush();
        //log.info( "Flushing: {}ms", Duration.between( start, Instant.now() ).toMillis() );

        start = Instant.now();
        em.clear();
        log.info( "Clearing Hibernate cache: {}ms", Duration.between( start, Instant.now() ).toMillis() );

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
            EntityValueJsonb entityValueJsonb = EntityValueJsonb.builder()
                                                                .id( type.name() + "-" + i )
                                                                .payload( EntityJsonb.builder()
                                                                      .stringProp( json )
                                                                      .build() )
                                                                .registrationObjectDbk( i )
                                                                .build();
            em.persist( entityValueJsonb );
            em.flush();
        }
        long total = Duration.between( start, Instant.now() ).toMillis();
        BigDecimal avg = new BigDecimal( total ).divide( new BigDecimal( max ), 3, RoundingMode.HALF_UP );
        log.info( "Finished inserting {} items => json {}: {}ms (avg={}ms)", max, type, total, avg );
    }

    private void read(int max, Type type) {
        Instant start = Instant.now();
        for ( int i = 0; i < max; i++ ) {
            EntityValueJsonb entityValueJsonb = em.find( EntityValueJsonb.class, type.name() + "-" + i );
            assertThat( entityValueJsonb ).isNotNull();
            assertThat( entityValueJsonb.getPayload().getStringProp() ).isNotNull();
        }
        long total = Duration.between( start, Instant.now() ).toMillis();
        BigDecimal avg = new BigDecimal( total ).divide( new BigDecimal( max ), 3, RoundingMode.HALF_UP );
        log.info( "Finished reading {} items => json {}: {}ms (avg={}ms)", max, type, total, avg );
    }

    enum Type {
        SMALL,
        MEDIUM,
        LARGE
    }

}
