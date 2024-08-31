package com.meidanet.system.scheduler.validation;

import com.meidanet.database.computer.science.course.condition.CSCoursesConditions;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.database.student.courses.Course;
import com.meidanet.system.preference.form.course.request.CoursePreferences;

import java.util.ArrayList;
import java.util.List;

import static com.meidanet.system.scheduler.helper.Splitter.stringSplitByComma;
import static com.meidanet.system.scheduler.helper.Splitter.stringSplitByHyphen;

public class CourseConditionsValidator {
    private final List<CoursePreferences> preferredRequiredCourses;
    private final List<CoursePreferences> preferredChoiceCourses;
    private final List<Course> studentCourses;
    private final CoursesConditionsService coursesConditionsService;
    private List<String> errors;

    public CourseConditionsValidator(List<Course> studentCourses, CoursesConditionsService coursesConditionsService,
                                     List<CoursePreferences> requiredList, List<CoursePreferences> choiceList) {
        this.studentCourses = studentCourses;
        this.coursesConditionsService = coursesConditionsService;
        this.preferredRequiredCourses = requiredList;
        this.preferredChoiceCourses = choiceList;
    }


    public List<CSCoursesConditions> getBedCoursesRequest() {

        List<CSCoursesConditions> coursesConditions = getPreferredCoursesConditions();
        List<CSCoursesConditions> bedCoursesRequest = new ArrayList<>();

        //for each course check his conditions
        for (CSCoursesConditions courseCondition : coursesConditions) {
            List<String> prerequisite_List = stringSplitByComma(courseCondition.getPrerequisite());
            List<String> pre_exchangeable_List = stringSplitByComma(courseCondition.getPre_exchangeable());
            List<String> parallel_condition_List = stringSplitByComma(courseCondition.getParallel_condition());
            List<String> parallel_exchangeable_List = stringSplitByComma(courseCondition.getParallel_exchangeable());
            List<String> exclusive_condition_List = stringSplitByComma(courseCondition.getExclusive_condition());

            boolean allPrerequisiteValid = true;
            boolean allParallelValid = true;
            boolean allExclusiveValid = true;

            if(prerequisite_List != null){
                allPrerequisiteValid = isAllPrerequisiteValid(prerequisite_List, pre_exchangeable_List);
            }
            if(parallel_condition_List != null){
                allParallelValid = isAllParallelValid(parallel_condition_List, parallel_exchangeable_List);
            }
            if(exclusive_condition_List != null){
                List<String> bedExclusivityNames = isExclusiveValid(exclusive_condition_List);

                if(bedExclusivityNames != null) {
                    for (String courseName : bedExclusivityNames) {
                        CSCoursesConditions bedExclusivityCourse = coursesConditionsService.findByCourseName(courseName);
                        //אם לפחות קורס אחד שפוגע באקסקלוסיביות לא נמצא כבר ברשימת ההבקשות הרעות, נוסיף אץ הקורס הראשי שנפגע
                        if(!bedCoursesRequest.contains(bedExclusivityCourse)){
                            allExclusiveValid = false;
                            break;
                        }
                    }
                }
            }

            String errorMassage = updateErrorMassage(allPrerequisiteValid, allParallelValid, allExclusiveValid, courseCondition);

            //הקורס הזה לא עמד בלפחות אחד מתנאי הקדם או התנאי המקביל
            if(!allPrerequisiteValid || !allParallelValid || (!allExclusiveValid)) {
                bedCoursesRequest.add(courseCondition);
                if(errorMassage != null){
                    if(errors == null)
                        errors = new ArrayList<>();
                    errors.add(errorMassage);
                }
            }
        }

        return bedCoursesRequest;
    }

    private String updateErrorMassage(boolean allPrerequisiteValid, boolean allParallelValid, boolean allExclusiveValid,
                                    CSCoursesConditions courseCondition) {
        String errorMassage = null;
        if(!allPrerequisiteValid) {
            if (!allParallelValid) {
                if (!allExclusiveValid)
                    errorMassage = "Course '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the prerequisites/parallels/exclusive conditions!";
                else
                    errorMassage = "Course '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the prerequisites/parallels conditions!";
            }
            else {
                if (!allExclusiveValid)
                    errorMassage = "Course: '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the prerequisites/exclusive conditions!";
                else
                    errorMassage = "Course: '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the prerequisites conditions!";
            }
        }
        else{
            if (!allParallelValid) {
                if (!allExclusiveValid)
                    errorMassage = "Course: '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the parallels/exclusive conditions!";
                else
                    errorMassage = "Course: '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the parallels conditions!";
            }
            else {
                if (!allExclusiveValid)
                    errorMassage = "Course: '" + courseCondition.getCourse_id_name() + "' does not meet one or more of the exclusive conditions!";
            }
        }
        return errorMassage;
    }


    public List<String> getErrorsList(){
        return errors;
    }

    private List<CSCoursesConditions> getPreferredCoursesConditions(){
        List<CSCoursesConditions> coursesConditions = new ArrayList<>();

        for (CoursePreferences courseCodeName : preferredRequiredCourses) {
            if(!courseCodeName.getCourseCodeName().isEmpty()) {
                String courseId = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(0);
                // Fetch the condition for each courseId
                CSCoursesConditions condition = coursesConditionsService.findByCourseId(courseId);

                // If the condition is present, add it to the list
                coursesConditions.add(condition);
            }
        }

        for (CoursePreferences courseCodeName : preferredChoiceCourses) {
            if(!courseCodeName.getCourseCodeName().isEmpty()) {
                String courseId = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(0);
                // Fetch the condition for each courseId
                CSCoursesConditions condition = coursesConditionsService.findByCourseId(courseId);

                // If the condition is present, add it to the list
                coursesConditions.add(condition);
            }
        }

        return coursesConditions;
    }

    private boolean isAllPrerequisiteValid (List<String> prerequisite, List<String> pre_exchangeable) {
        boolean allPrerequisiteValid = true;

        for (int i = 0; i < prerequisite.size(); i++) {

            String prerequisiteCondition = prerequisite.get(i);
            String pre_exchangeableCondition = null;
            if(pre_exchangeable != null) {
                pre_exchangeableCondition = pre_exchangeable.get(i);
            }
            boolean flag = false;

            for (Course course : studentCourses) {
                if(course.getCourseName().equals(prerequisiteCondition) || course.getCourseName().equals(pre_exchangeableCondition)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                allPrerequisiteValid = false;
                break;
            }
        }

        return allPrerequisiteValid;
    }

    private boolean isAllParallelValid (List<String> parallel_condition, List<String> parallel_exchangeable) {
        boolean allParallelValid = true;

        for (int i = 0; i < parallel_condition.size(); i++) {

            String parallelCondition = parallel_condition.get(i);
            String parallel_exchangeableCondition = null;
            if(parallel_exchangeable != null) {
                parallel_exchangeableCondition = parallel_exchangeable.get(i);
            }

            boolean flag = false;

            for (Course course : studentCourses) {
                if(course.getCourseName().equals(parallelCondition) || course.getCourseName().equals(parallel_exchangeableCondition)) {
                    flag = true;
                    break;
                }
            }
            //הקןרס הנדרש ללמוד במקביל לא נלמד עדיין, אז צריך לבדוק אם הסטודנט רצה ללמוד אותו עכשיו
            if(!flag){
                //String conditionsCourseID = stringSplitByHyphen(course_id_name).get(0);
                for (CoursePreferences courseCodeName : preferredRequiredCourses) {
                    String courseName = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(1);
                    if(courseName.equals(parallelCondition) || courseName.equals(parallel_exchangeableCondition)) {
                        flag = true;
                        break;
                    }
                }

                if(!flag){
                    for (CoursePreferences courseCodeName : preferredChoiceCourses) {
                        String courseName = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(1);
                        if(courseName.equals(parallelCondition) || courseName.equals(parallel_exchangeableCondition)) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (!flag) {
                allParallelValid = false;
                break;
            }
        }

        return allParallelValid;
    }


    private List<String> isExclusiveValid(List<String> exclusiveCondition) {
        List<String> bedExclusivity = null;
        for (int i = 0; i < exclusiveCondition.size(); i++) {

            String excCondition = exclusiveCondition.get(i);

            boolean flag = false;

            for (Course course : studentCourses) {
                if(course.getCourseName().equals(excCondition)) {
                    flag = true;
                    break;
                }
            }
            //הקןרס שאסור ללמוד לפני או במקביל לא נלמד עדיין, אז צריך לבדוק אם הסטודנט רצה ללמוד אותו עכשיו
            if(!flag){
                //String conditionsCourseID = stringSplitByHyphen(course_id_name).get(0);
                for (CoursePreferences courseCodeName : preferredRequiredCourses) {
                    String courseName = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(1);
                    if(courseName.equals(excCondition)){
                        flag = true;
                        break;
                    }
                }

                if(!flag){
                    for (CoursePreferences courseCodeName : preferredChoiceCourses) {
                        String courseName = stringSplitByHyphen(courseCodeName.getCourseCodeName()).get(1);
                        if(courseName.equals(excCondition)) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (flag) {
                if(bedExclusivity == null)
                    bedExclusivity = new ArrayList<>();
                bedExclusivity.add(excCondition);
            }
        }
        return bedExclusivity;
    }
}
