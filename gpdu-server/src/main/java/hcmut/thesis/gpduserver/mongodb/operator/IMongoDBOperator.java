package hcmut.thesis.gpduserver.mongodb.operator;

import org.bson.Document;
import java.util.List;

public interface IMongoDBOperator<Model> {

    Model insert(Model data);

    List<Model> insert(List<Model> data);

    Long count(Document query);

    Model find(Document query, Document sort);

    List<Model> findMany(Document query, Document sort, int skip, int limit);

    List<Model> findMany(Document query, Document sort, Document projection, int skip, int limit);

    List<Model> findAll(Document query, Document sort, Document projection);

    Boolean updateOne(Document query, Document data);

    Boolean updateMany(Document query, Document data);

}
