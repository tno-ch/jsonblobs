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
package org.example.mongodb;

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

public class EntityMongoCodec implements CollectibleCodec<EntityMongo> {

    private final Codec<Document> documentCodec;

    public EntityMongoCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get( Document.class );
    }

    @Override
    public void encode(BsonWriter writer, EntityMongo entityMongo, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put( "name", entityMongo.getName() );
        doc.put( "payload", entityMongo.getPayload() );
        doc.put( "registrationObjectDbk", entityMongo.getRegistrationObjectDbk() );
        documentCodec.encode( writer, doc, encoderContext );
    }

    @Override
    public Class<EntityMongo> getEncoderClass() {
        return EntityMongo.class;
    }

    @Override
    public EntityMongo generateIdIfAbsentFromDocument(EntityMongo document) {
        if ( !documentHasId( document ) ) {
            document.setId( UUID.randomUUID().toString() );
        }
        return document;
    }

    @Override
    public boolean documentHasId(EntityMongo document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(EntityMongo document) {
        return new BsonString( document.getId() );
    }

    @Override
    public EntityMongo decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode( reader, decoderContext );
        EntityMongo entityMongo = new EntityMongo();
        if ( document.getObjectId( "_id" ) != null ) {
            entityMongo.setId( document.getObjectId( "_id" ).toString() );
        }
        entityMongo.setName( document.getString( "name" ) );
        entityMongo.setPayload( document.getString( "payload" ) );
        entityMongo.setRegistrationObjectDbk( document.getInteger( "registrationObjectDbk" ) );
        return entityMongo;
    }

}
