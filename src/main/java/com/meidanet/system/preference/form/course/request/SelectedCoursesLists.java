package com.meidanet.system.preference.form.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SelectedCoursesLists {
    @JsonProperty("reqCoursesAlist")
    private List<CoursePreferences> requiredSemesterA;

    @JsonProperty("choiceCoursesAlist")
    private List<CoursePreferences> choiceSemesterA;

    @JsonProperty("reqCoursesBlist")
    private List<CoursePreferences> requiredSemesterB;

    @JsonProperty("choiceCoursesBlist")
    private List<CoursePreferences> choiceSemesterB;

    // Getters and Setters
    public List<CoursePreferences> getRequiredSemesterA() {
        return requiredSemesterA;
    }

    public void setRequiredSemesterA(List<CoursePreferences> requiredSemesterA) {
        this.requiredSemesterA = requiredSemesterA;
    }

    public List<CoursePreferences> getChoiceSemesterA() {
        return choiceSemesterA;
    }

    public void setChoiceSemesterA(List<CoursePreferences> choiceSemesterA) {
        this.choiceSemesterA = choiceSemesterA;
    }

    public List<CoursePreferences> getRequiredSemesterB() {
        return requiredSemesterB;
    }

    public void setRequiredSemesterB(List<CoursePreferences> requiredSemesterB) {
        this.requiredSemesterB = requiredSemesterB;
    }

    public List<CoursePreferences> getChoiceSemesterB() {
        return choiceSemesterB;
    }

    public void setChoiceSemesterB(List<CoursePreferences> choiceSemesterB) {
        this.choiceSemesterB = choiceSemesterB;
    }
}
