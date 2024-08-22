package com.meidanet.system.scheduler.answer;

import com.meidanet.system.preference.form.course.request.CoursePreferences;

import java.util.ArrayList;
import java.util.List;

public class FinalSystem {
    private String studentID;
    private List<CoursePreferences> requiredSemesterA;
    private List<CoursePreferences> choiceSemesterA;
    private List<CoursePreferences> requiredSemesterB;
    private List<CoursePreferences> choiceSemesterB;

    private List<String> errors;
    private List<String> changes;

    public void addError(String error){
        if(errors.isEmpty()){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public void addChange(String change){
        if(changes.isEmpty()){
            changes = new ArrayList<>();
        }
        changes.add(change);
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void addReqCourseSemA(CoursePreferences coursePreferences){
        requiredSemesterA.add(coursePreferences);
    }

    public void addChoCourseSemA(CoursePreferences coursePreferences){
        choiceSemesterA.add(coursePreferences);
    }

    public void addReqCourseSemB(CoursePreferences coursePreferences){
        requiredSemesterB.add(coursePreferences);
    }

    public void addChoCourseSemB(CoursePreferences coursePreferences){
        choiceSemesterB.add(coursePreferences);
    }

    public List<CoursePreferences> getRequiredSemesterA() {
        return requiredSemesterA;
    }

    public List<CoursePreferences> getChoiceSemesterA() {
        return choiceSemesterA;
    }

    public List<CoursePreferences> getRequiredSemesterB() {
        return requiredSemesterB;
    }

    public List<CoursePreferences> getChoiceSemesterB() {
        return choiceSemesterB;
    }
}

