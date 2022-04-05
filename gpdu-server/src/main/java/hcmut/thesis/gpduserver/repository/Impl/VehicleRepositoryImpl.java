package hcmut.thesis.gpduserver.repository.Impl;

import hcmut.thesis.gpduserver.config.mongodb.MongoDBClient;
import hcmut.thesis.gpduserver.config.mongodb.MongoDbConfig;
import hcmut.thesis.gpduserver.constants.mongodb.CollectionNameEnum;
import hcmut.thesis.gpduserver.constants.mongodb.DBNameEnum;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.mongodb.generic.GenericRepository;
import hcmut.thesis.gpduserver.mongodb.operator.MongoDBOperator;
import hcmut.thesis.gpduserver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleRepositoryImpl extends GenericRepository<Vehicle> implements VehicleRepository {

  private final MongoDBOperator<Vehicle> mongoDBOperator;

  @Autowired
  public VehicleRepositoryImpl(MongoDBClient mongoDBClient){
    mongoDBOperator = new MongoDBOperator<>(mongoDBClient, DBNameEnum.GPDU, CollectionNameEnum.VEHICLE, Vehicle.class);
  }
  @Override
  public MongoDBOperator<Vehicle> getMongoDBOperator() {
    return mongoDBOperator;
  }
}
