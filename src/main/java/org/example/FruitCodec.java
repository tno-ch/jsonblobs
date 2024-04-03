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

import java.util.UUID;

import com.mongodb.MongoClientSettings;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class FruitCodec implements CollectibleCodec<Fruit> {

    private final Codec<Document> documentCodec;

    public FruitCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get( Document.class );
    }

    @Override
    public void encode(BsonWriter writer, Fruit fruit, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put( "name", fruit.getName() );
        doc.put( "description", fruit.getDescription() );
        doc.put( "registrationObjectDbk", fruit.getRegistrationObjectDbk() );
        documentCodec.encode( writer, doc, encoderContext );
    }

    @Override
    public Class<Fruit> getEncoderClass() {
        return Fruit.class;
    }

    @Override
    public Fruit generateIdIfAbsentFromDocument(Fruit document) {
        if ( !documentHasId( document ) ) {
            document.setId( UUID.randomUUID().toString() );
        }
        return document;
    }

    @Override
    public boolean documentHasId(Fruit document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Fruit document) {
        return new BsonString( document.getId() );
    }

    @Override
    public Fruit decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode( reader, decoderContext );
        Fruit fruit = new Fruit();
        if ( document.getObjectId( "_id" ) != null ) {
            fruit.setId( document.getObjectId( "_id" ).toString() );
        }
        fruit.setName( document.getString( "name" ) );
        fruit.setDescription( document.getString( "description" ) );
        fruit.setRegistrationObjectDbk( document.getInteger( "registrationObjectDbk" ) );
        return fruit;
    }

}