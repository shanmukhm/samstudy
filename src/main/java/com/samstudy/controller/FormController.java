package com.samstudy.controller;

import com.samstudy.model.FormAndVersion;
import com.samstudy.model.FormSubmissionData;
import com.samstudy.service.FormService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.samstudy.util.Constants.Roles.USER_ROLE;

@RestController
public class FormController {

    @Autowired
    private FormService formService;

    @RequestMapping(method = RequestMethod.POST, path = "/uploadForm")
    public ModelMap saveForm(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
        return formService.convertAndSave(file);
    }

    @RequiresRoles(USER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/submitForm")
    public ModelMap submitForm(@RequestBody FormSubmissionData formData) {
        return formService.submitForm(formData);
    }

    @RequiresRoles(USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/whatForms")
    public List<FormAndVersion> whatForms() {
        return formService.getFormsWithVersions();
    }

    @RequiresRoles(USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/getForm")
    public String getForm(@RequestParam String formId, @RequestParam int version) {
        return formService.getForm(formId, version);
    }
}
