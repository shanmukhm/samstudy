package com.samstudy.service.impl;

import com.samstudy.model.Form;
import com.samstudy.model.FormAndVersion;
import com.samstudy.model.FormSubmissionData;
import com.samstudy.repository.FormRepository;
import com.samstudy.repository.FormSubmissionDataRepository;
import com.samstudy.service.FormService;
import com.samstudy.util.SessionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormSubmissionDataRepository submissionRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ModelMap convertAndSave(MultipartFile file) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        ModelMap mp = new ModelMap();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            JSONObject json = new JSONObject();

            JSONArray rows = new JSONArray();
            // Iterate through the rows.
            int rowCount = 0;
            List<String> columnTitles = new ArrayList<>();
            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();

                JSONObject jRow = new JSONObject();
                // Iterate through the cells.

                JSONArray cells = new JSONArray();
                int columnCount = 0;
                for (Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
                {
                    Cell cell = cellsIT.next();
                    int colIndex = cell.getColumnIndex();

                    if (rowCount == 0) {
                        columnTitles.add(columnCount, formatKey(cell.getStringCellValue()));
                    } else {
                        jRow.put(columnTitles.get(colIndex), cell.getStringCellValue());
                        cells.put( cell.getStringCellValue() );
                    }

                    columnCount++;
                }

                rows.put( jRow );
                rowCount++;
            }

            // Create the JSON.
            json.put( "rows", rows );

            JSONObject formJson = formatJson(json);
            formJson.getJSONObject("metadata").put("name", sheetName);
            String formId = formJson.getJSONObject("metadata").getString("id");
            int version = formJson.getJSONObject("metadata").getInt("version");

            formRepository.insert(new Form(formId, sheetName, version, formJson.toString()));
        }
        mp.addAttribute("msg", "Imported " + workbook.getNumberOfSheets() + " forms from xls sheet");
        return mp;
    }

    @Override
    public ModelMap submitForm(FormSubmissionData formData) {
        formData.setUserId(SessionUtils.getCurrentUserId());
        formData.setSubmittedDate(new Date());
        submissionRepository.insert(formData);
        Form latestForm = formRepository.findByFormIdAndLatestVersion(formData.getFormId(), new PageRequest(0, 1,
                Sort.Direction.DESC, "version")).getContent().get(0);
        return new ModelMap("msg", formData.getUserId() + " submitted form : " + formData.getFormId() + " successfully");
    }

    @Override
    public List<FormAndVersion> getFormsWithVersions() {
        List<String> formIds = mongoTemplate.getDb().getCollection("forms").distinct("formId");
        List<Form> forms = formRepository.findAll();
        List<FormAndVersion> formsWithVersions = new ArrayList<>();
        for (int i = 0; i < formIds.size(); i++) {
            Form form = formRepository.findByFormIdAndLatestVersion(formIds.get(i), new PageRequest(0, 1,
                    Sort.Direction.DESC, "version")).getContent().get(0);
            formsWithVersions.add(new FormAndVersion(form.getFormId(), form.getVersion()));
        }

        return formsWithVersions;
    }

    @Override
    public String getForm(String formId, int version) {
        Form form = formRepository.findByFormIdAndVersion(formId, version);
        return form.getFormJson();
    }

    private String formatKey(String key) {
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(key);
        if (key == null && key.trim().isEmpty()) {
            return key;
        }
        StringBuilder builder = new StringBuilder(key);

        for(int i = 0; i < key.length(); i++) {
            if (key.substring(i, i + 1).matches("[^A-Za-z0-9 ]")) {
                if (i == 0 || i == key.length() - 1) {
                    builder.setCharAt(i, ' ');
                } else {
                    builder.setCharAt(i, '_');
                }
            } else {
                builder.setCharAt(i, key.substring(i, i + 1).toLowerCase().charAt(0));
            }
        }

        return builder.toString().trim().replaceAll("\\s+","");
    }

    private JSONObject formatJson(JSONObject json) {
        JSONArray array = json.getJSONArray("rows");

        JSONObject formJson = new JSONObject();
        JSONArray questions = new JSONArray();

        JSONObject multipleChoiceQuestion = new JSONObject();
        JSONArray choices = null;

        // Add metadata
        JSONObject metadata = new JSONObject();
        metadata.put("name", "test-form");
        metadata.put("id", UUID.randomUUID().toString());
        metadata.put("generated_time", new Date());
        metadata.put("version", 1);
        formJson.put("metadata", metadata);

        for (int i = 0; i < array.length(); i++) {
            JSONObject currentQuestion = array.getJSONObject(i);

            if (currentQuestion.length() != 0) {
                if (!currentQuestion.isNull("type") && ("Select One".equals(currentQuestion.getString("type")) ||
                        "Select Multiple".equals(currentQuestion.getString("type")))) {
                    multipleChoiceQuestion = currentQuestion;
                    choices = new JSONArray();
                } else if (!currentQuestion.isNull("type") && !"Choice".equals(currentQuestion.getString("type"))){
                    choices = null;
                }

                if (!currentQuestion.isNull("type") && "Choice".equals(currentQuestion.getString("type"))) {
                    choices.put(currentQuestion);
                    if (!multipleChoiceQuestion.isNull("choices")) {
                        multipleChoiceQuestion.remove("choices");
                    }

                    multipleChoiceQuestion.put("choices", removeDuplicates(choices));
                }
                if (choices != null) {
                    multipleChoiceQuestion.put("guid", UUID.randomUUID().toString());
                    questions.put(multipleChoiceQuestion);
                } else {
                    currentQuestion.put("guid", UUID.randomUUID().toString());
                    questions.put(currentQuestion);
                }
            }
        }
        formJson.put("questions", removeDuplicates(questions));
        return formJson;
    }

    private JSONArray removeDuplicates(JSONArray array) {
        Set<String> questions = new HashSet<>();
        JSONArray tempArray=new JSONArray();
        for (int i = 0;i < array.length(); i++) {
            String question = array.getJSONObject(i).getString("question");
            if (questions.contains(question)) {
                continue;
            } else {
                questions.add(question);
                tempArray.put(array.getJSONObject(i));
            }
        }

        return tempArray;
    }
}
