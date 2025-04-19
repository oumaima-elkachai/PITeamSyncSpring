package com.example.payroll.converter;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.payroll.model.Employee;

@Component
public class ObjectIdToEmployeeConverter implements Converter<ObjectId, Employee> {

    private final MongoTemplate mongoTemplate;

    public ObjectIdToEmployeeConverter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Employee convert(ObjectId source) {
        return mongoTemplate.findById(source, Employee.class);  // Utilisation de MongoTemplate
    }
}
