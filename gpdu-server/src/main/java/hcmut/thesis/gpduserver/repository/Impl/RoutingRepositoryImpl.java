package hcmut.thesis.gpduserver.repository.Impl;

import hcmut.thesis.gpduserver.config.mongodb.MongoDBClient;
import hcmut.thesis.gpduserver.constants.mongodb.CollectionNameEnum;
import hcmut.thesis.gpduserver.constants.mongodb.DBNameEnum;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.mongodb.generic.GenericRepository;
import hcmut.thesis.gpduserver.mongodb.operator.MongoDBOperator;
import hcmut.thesis.gpduserver.repository.RoutingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoutingRepositoryImpl extends GenericRepository<Routing> implements RoutingRepository {

  private final MongoDBOperator<Routing> mongoDBOperator;

  @Autowired
  public RoutingRepositoryImpl(MongoDBClient mongoDBClient) {
    mongoDBOperator = new MongoDBOperator<>(mongoDBClient, DBNameEnum.GPDU,
        CollectionNameEnum.ROUTINGS, Routing.class);
  }

  @Override
  public MongoDBOperator<Routing> getMongoDBOperator() {
    return mongoDBOperator;
  }
}
