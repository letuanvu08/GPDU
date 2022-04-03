package hcmut.thesis.gpduserver.repository.Impl;

import hcmut.thesis.gpduserver.constants.mongodb.CollectionNameEnum;
import hcmut.thesis.gpduserver.constants.mongodb.DBNameEnum;
import hcmut.thesis.gpduserver.entity.User;
import hcmut.thesis.gpduserver.config.mongodb.MongoDBClient;
import hcmut.thesis.gpduserver.mongodb.generic.GenericRepository;
import hcmut.thesis.gpduserver.mongodb.operator.MongoDBOperator;
import hcmut.thesis.gpduserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepository<User> implements UserRepository {
    private MongoDBOperator mongoDBOperator;

    @Autowired
    public UserRepositoryImpl(MongoDBClient mongoDBClient) {
        mongoDBOperator = new MongoDBOperator<>(mongoDBClient, DBNameEnum.USER, CollectionNameEnum.USERS, User.class);
    }

    @Override
    public MongoDBOperator<User> getMongoDBOperator() {
        return mongoDBOperator;
    }
}
