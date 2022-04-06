package hcmut.thesis.gpduserver.config.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import com.mongodb.client.model.geojson.codecs.GeometryCollectionCodec;
import com.mongodb.client.model.geojson.codecs.PointCodec;
import eu.dozd.mongo.MongoMapper;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;


@Component
public class MongoDBClient {

  private final MongoClient mongoClient;

  @Autowired
  public MongoDBClient(MongoDbConfig config) {
    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(MongoMapper.getProviders())
    );
    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .applyConnectionString(new ConnectionString(config.getDatabase()))
        .build();
    mongoClient = MongoClients.create(settings);
  }

  public MongoClient getClient() {
    return mongoClient;
  }
}
