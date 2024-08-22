package com.meidanet.system.preference.form;

import com.meidanet.system.preference.form.course.request.CoursePreferences;

import java.util.List;

public class PreferencesForm {
    private String studentID;
    private List<CoursePreferences> requiredSemesterA;
    private List<CoursePreferences> choiceSemesterA;
    private List<CoursePreferences> requiredSemesterB;
    private List<CoursePreferences> choiceSemesterB;

    // Default constructor
    public PreferencesForm(){
    }

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

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

//    public void addReqCourseSemA(CoursePreferences course) {
//        if(requiredSemesterA.isEmpty()){
//            requiredSemesterA = new ArrayList<>();
//        }
//        requiredSemesterA.add(course);
//    }
//
//    public void addChoiceCourseSemA(CoursePreferences course) {
//        if(choiceSemesterA.isEmpty()){
//            choiceSemesterA = new ArrayList<>();
//        }
//        choiceSemesterA.add(course);
//    }
//
//    public void addReqCourseSemB(CoursePreferences course) {
//        if(requiredSemesterB.isEmpty()){
//            requiredSemesterB = new ArrayList<>();
//        }
//        requiredSemesterB.add(course);
//    }
//
//    public void addChoiceCourseSemB(CoursePreferences course) {
//        if(choiceSemesterB.isEmpty()){
//            choiceSemesterB = new ArrayList<>();
//        }
//        choiceSemesterB.add(course);
//    }


}