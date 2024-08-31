package com.meidanet.system.scheduler.helper;

import com.meidanet.system.preference.form.course.request.CoursePreferences;

import java.util.ArrayList;
import java.util.List;

public class FinalSystemHelper {
    private List<CoursePreferences> requiredSemesterA;
    private List<CoursePreferences> choiceSemesterA;
    private List<CoursePreferences> requiredSemesterB;
    private List<CoursePreferences> choiceSemesterB;


    public void addReqCourseSemA(CoursePreferences coursePreferences){
        if(requiredSemesterA == null)
            requiredSemesterA = new ArrayList<>();
        requiredSemesterA.add(coursePreferences);
    }

    public void addChoCourseSemA(CoursePreferences coursePreferences){
        if(choiceSemesterA == null)
            choiceSemesterA = new ArrayList<>();
        choiceSemesterA.add(coursePreferences);
    }

    public void addReqCourseSemB(CoursePreferences coursePreferences){
        if(requiredSemesterB == null)
            requiredSemesterB = new ArrayList<>();
        requiredSemesterB.add(coursePreferences);
    }

    public void addChoCourseSemB(CoursePreferences coursePreferences){
        if(choiceSemesterB == null)
            choiceSemesterB = new ArrayList<>();
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
