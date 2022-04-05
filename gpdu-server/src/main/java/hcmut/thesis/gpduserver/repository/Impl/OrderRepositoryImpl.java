package hcmut.thesis.gpduserver.repository.Impl;

import hcmut.thesis.gpduserver.config.mongodb.MongoDBClient;
import hcmut.thesis.gpduserver.constants.enumations.StatusOrderEnum;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.constants.mongodb.CollectionNameEnum;
import hcmut.thesis.gpduserver.constants.mongodb.DBNameEnum;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.mongodb.generic.GenericRepository;
import hcmut.thesis.gpduserver.mongodb.operator.MongoDBOperator;
import hcmut.thesis.gpduserver.repository.OrderRepository;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends GenericRepository<Order> implements OrderRepository {

  private final MongoDBOperator<Order> mongoDBOperator;

  @Autowired
  public OrderRepositoryImpl(MongoDBClient mongoDBClient){
    mongoDBOperator = new MongoDBOperator<>(mongoDBClient,
        DBNameEnum.GPDU,
        CollectionNameEnum.ORDERS, Order.class);
  }
  @Override
  public MongoDBOperator<Order> getMongoDBOperator() {
    return mongoDBOperator;
  }


}
