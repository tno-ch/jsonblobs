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
package org.example.bytea_lz4;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.json_lz4.EntityJsonLz4;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

//NEED to have at least 1 entity
@Entity
@Table( name = "entity_value_bytea_lz4" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityValueByteaLz4 {

    @Id
    private String id;

    private byte[] payload;

    private int registrationObjectDbk;

}
