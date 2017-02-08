package com.samstudy.repository;

import com.samstudy.model.FormSubmissionData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormSubmissionDataRepository extends MongoRepository<FormSubmissionData, String> {

}
