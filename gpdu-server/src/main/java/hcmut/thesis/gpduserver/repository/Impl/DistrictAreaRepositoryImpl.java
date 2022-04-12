package hcmut.thesis.gpduserver.repository.Impl;

import hcmut.thesis.gpduserver.config.mongodb.MongoDBClient;
import hcmut.thesis.gpduserver.constants.mongodb.CollectionNameEnum;
import hcmut.thesis.gpduserver.constants.mongodb.DBNameEnum;
import hcmut.thesis.gpduserver.models.entity.DistrictArea;
import hcmut.thesis.gpduserver.mongodb.generic.GenericRepository;
import hcmut.thesis.gpduserver.mongodb.operator.MongoDBOperator;
import hcmut.thesis.gpduserver.repository.DistrictAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DistrictAreaRepositoryImpl extends GenericRepository<DistrictArea> implements DistrictAreaRepository {


  private final MongoDBOperator<DistrictArea> mongoDBOperator;

  @Autowired
  public DistrictAreaRepositoryImpl(MongoDBClient client){
    mongoDBOperator = new MongoDBOperator<>(client, DBNameEnum.GPDU, CollectionNameEnum.DISTRICT_AREA, DistrictArea.class);
  }
  @Override
  public MongoDBOperator<DistrictArea> getMongoDBOperator() {
    return mongoDBOperator;
  }
}
