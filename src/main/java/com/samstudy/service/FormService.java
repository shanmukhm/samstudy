package com.samstudy.service;

import com.samstudy.model.FormAndVersion;
import com.samstudy.model.FormSubmissionData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FormService {

    ModelMap convertAndSave(MultipartFile file) throws IOException, InvalidFormatException;

    ModelMap submitForm(FormSubmissionData formData);

    List<FormAndVersion> getFormsWithVersions();

    String getForm(String name, int version);
}
