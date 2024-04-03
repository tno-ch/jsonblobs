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

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class FruitCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if ( clazz.equals( Fruit.class ) ) {
            return (Codec<T>) new FruitCodec();
        }
        return null;
    }

}