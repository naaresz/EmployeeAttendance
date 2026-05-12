
package com.rfid.employeeattendance.dao;

import com.mongodb.client.MongoCollection;
import com.rfid.employeeattendance.db.MongoManager;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;

public class GenericDAO<T> implements BaseDAO<T> {
    private final MongoCollection<T> collection;
    private final Class<T> clazz;
    
    public GenericDAO(String collectionName, Class<T> clazz){
        this.clazz = clazz;
        this.collection = MongoManager.getDatabase().getCollection(collectionName, clazz);
    }
    
    @Override
    public void save(T entity){
        collection.insertOne(entity);
    }
    
    @Override
    public void update(Bson filter, T entity) {
        collection.replaceOne(filter, entity);
    }
    
    @Override
    public void delete(Bson filter){
        collection.deleteOne(filter);
    }
    
    @Override
    public List<T> findAll(){
        return collection.find().into(new ArrayList<>());
    }
    
    @Override
    public T findOne(Bson filter){
        return collection.find(filter).first();
    }
    
    @Override
    public List<T> findMany(Bson filter){
        return collection.find(filter).into(new ArrayList<>());
    }
    
    public List<T> findWithSort(Bson filter, Bson sort, int limit){
        return collection.find(filter)
                .sort(sort)
                .limit(limit)
                .into(new ArrayList<>());
    }
}

