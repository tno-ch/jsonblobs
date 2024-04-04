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

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@ApplicationScoped
@Slf4j
public class EntityMongoCodecService {

    //CHECKSTYLE:OFF
    MongoClient mongoClient;
    //CHECKSTYLE:ON

    @Inject
    public EntityMongoCodecService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;

        if ( !hasIndex( "name" ) ) {
            Document index = new Document();
            index.put( "name", 1 );
            getCollection().createIndex( index );
            log.info( "Created index for field 'name'" );
        }
        else {
            log.info( "Index for field 'name' already exists. Skipping creation..." );
        }
    }

    public List<EntityMongo> list() {
        List<EntityMongo> list = new ArrayList<>();
        try (MongoCursor<EntityMongo> cursor = getCollection().find().iterator()) {
            while ( cursor.hasNext() ) {
                list.add( cursor.next() );
            }
        }

        return list;
    }

    public void add(EntityMongo entityMongo) {
        getCollection().insertOne( entityMongo );
    }

    public EntityMongo findByName(String name) {
        Document searchQuery = new Document();
        searchQuery.put( "name", name );

        FindIterable<EntityMongo> entities = getCollection().find( searchQuery );

        EntityMongo found = null;
        try (MongoCursor<EntityMongo> cursorIterator = entities.cursor()) {
            while ( cursorIterator.hasNext() ) {
                found = cursorIterator.next();
                break;
            }
        }

        return found;
    }

    private MongoCollection<EntityMongo> getCollection() {
        MongoCollection<EntityMongo> collection = mongoClient.getDatabase( "jsonblobs" )
                                                             .getCollection( "jsonblobs", EntityMongo.class );
        return collection;
    }

    private boolean hasIndex(String field) {
        try (MongoCursor<Document> cursor = getCollection().listIndexes().iterator()) {
            while ( cursor.hasNext() ) {
                Document next = cursor.next();
                Document indexDocument = next.get( "key", Document.class ); //retrieve keyValue that is the document with index definition
                if ( indexDocument.get( field ) != null ) { //look if there is a key with the name of the field in the indexDocument
                    return true;
                }
            }
        }

        return false;
    }

}
