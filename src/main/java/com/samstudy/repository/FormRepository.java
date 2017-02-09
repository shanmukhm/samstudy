package com.samstudy.repository;


import com.samstudy.model.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FormRepository extends MongoRepository<Form, String>{

    @Query("{'formId' : ?0}")
    Page<Form> findByFormIdAndLatestVersion(String formId, Pageable pageable);

    Form findByFormId(String formId);

    @Query("{'formId' : ?0, 'version' : ?1}")
    Form findByFormIdAndVersion(String formId, int version);

    @Query("{'name' : ?0}")
    Page<Form> findByNameAndLatestVersion(String name, Pageable pageable);

    @Query("{'name' : ?0, 'version' : ?1}")
    Form findByNameAndVersion(String name, int version);
}
