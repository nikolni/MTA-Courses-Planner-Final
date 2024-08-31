package com.meidanet.system.preference.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meidanet.system.preference.form.course.request.SelectedCoursesLists;

public class PreferencesForm {

    @JsonProperty("selectedCoursesData")
    private SelectedCoursesLists selectedCoursesLists;

    @JsonProperty("studentId")
    private String studentId;

    // Getters and setters
    public SelectedCoursesLists getSelectedCoursesData() {
        return selectedCoursesLists;
    }

    public void setSelectedCoursesData(SelectedCoursesLists selectedCoursesData) {
        this.selectedCoursesLists = selectedCoursesData;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}