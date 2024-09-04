package com.meidanet.system.controller.form.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meidanet.system.preference.form.PreferencesForm;

public class FormParser {

        public static PreferencesForm extractStudentRequests(String json) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            PreferencesForm preferencesForm = objectMapper.readValue(json, PreferencesForm.class);
            return preferencesForm;
        }
}
