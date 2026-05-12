package com.rfid.employeeattendance.dao;

import java.util.List;
import org.bson.conversions.Bson;

public interface BaseDAO<T> {
    void save(T entity);
    void update(Bson filter, T entity);
    void delete (Bson filter);
    
    List<T> findAll();
    T findOne(Bson filter);
    List<T> findMany(Bson filter);
}
