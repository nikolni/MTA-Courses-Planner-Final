package com.meidanet.system.scheduler.answer;

import com.meidanet.database.computer.science.course.choice.CSCoursesChoice;
import com.meidanet.database.computer.science.course.required.CSCoursesRequired;

import java.util.ArrayList;
import java.util.List;

public class FinalSystem {
    private String studentID;

    private List<CSCoursesRequired> requiredSemesterA;
    private List<CSCoursesChoice> choiceSemesterA;
    private List<CSCoursesRequired> requiredSemesterB;
    private List<CSCoursesChoice> choiceSemesterB;

    private List<String> errorsA;
    private List<String> changesA;
    private List<String> errorsB;
    private List<String> changesB;

    public void addErrorA(String error){
        if(this.errorsA == null){
            this.errorsA = new ArrayList<>();
        }
        this.errorsA.add(error);
    }

    public void addChangesA(List<String> changes){
        if(changes != null){
            if(this.changesA == null){
                this.changesA = new ArrayList<>();
            }
            this.changesA.addAll(changes);
        }
    }
    public void addErrorB(String error){
        if(this.errorsB == null){
            this.errorsB = new ArrayList<>();
        }
        this.errorsB.add(error);
    }

    public void addChangesB(List<String> changes) {
        if (changes != null) {
            if (this.changesB == null) {
                this.changesB = new ArrayList<>();
            }
            this.changesB.addAll(changes);
        }
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void addReqCourseSemA(CSCoursesRequired coursePreferences){
        if(requiredSemesterA == null)
            requiredSemesterA = new ArrayList<>();
        requiredSemesterA.add(coursePreferences);
    }

    public void addChoCourseSemA(CSCoursesChoice coursePreferences){
        if(choiceSemesterA == null)
            choiceSemesterA = new ArrayList<>();
        choiceSemesterA.add(coursePreferences);
    }

    public void addReqCourseSemB(CSCoursesRequired coursePreferences){
        if(requiredSemesterB == null)
            requiredSemesterB = new ArrayList<>();
        requiredSemesterB.add(coursePreferences);
    }

    public void addChoCourseSemB(CSCoursesChoice coursePreferences){
        if(choiceSemesterB == null)
            choiceSemesterB = new ArrayList<>();
        choiceSemesterB.add(coursePreferences);
    }

    public List<CSCoursesRequired> getRequiredSemesterA() {
        return requiredSemesterA;
    }

    public List<CSCoursesChoice> getChoiceSemesterA() {
        return choiceSemesterA;
    }

    public List<CSCoursesRequired> getRequiredSemesterB() {
        return requiredSemesterB;
    }

    public List<CSCoursesChoice> getChoiceSemesterB() {
        return choiceSemesterB;
    }

    public List<String> getErrorsA(){
        return errorsA;
    }

    public List<String> getChangesA(){
        return changesA;
    }

    public List<String> getErrorsB(){
        return errorsB;
    }

    public List<String> getChangesB(){
        return changesB;
    }
}

