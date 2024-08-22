package com.meidanet.system.scheduler.validation;

import com.meidanet.htmlscraper.database.computer.science.course.condition.CSCoursesConditions;
import com.meidanet.htmlscraper.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.htmlscraper.database.courses.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.meidanet.system.scheduler.helper.Splitter.stringSplitByComma;
import static com.meidanet.system.scheduler.helper.Splitter.stringSplitByHyphen;

public class CourseConditionsValidator {

    private List<String> preferredRequiredCoursesIDs;
    private List<String> preferredChoiceCoursesIDs;
    private List<Course> studentCourses;
    private CoursesConditionsService csCoursesConditionsService;
    private List<String> errors;

    public CourseConditionsValidator(List<String> preferredRequiredCoursesIDs, List<String> preferredChoiceCoursesIDs, List<Course> studentCourses,
                                     CoursesConditionsService csCoursesConditionsService) {
        this.preferredRequiredCoursesIDs = preferredRequiredCoursesIDs;
        this.preferredChoiceCoursesIDs = preferredChoiceCoursesIDs;
        this.studentCourses = studentCourses;
        this.csCoursesConditionsService = csCoursesConditionsService;
    }


    public List<CSCoursesConditions> getBedCoursesRequest() {

        List<CSCoursesConditions> coursesConditions = getPreferredCoursesConditions();
        List<CSCoursesConditions> bedCoursesRequest = new ArrayList<>();

        String errorMassage = "";

        //for each course check his conditions
        for (CSCoursesConditions courseCondition : coursesConditions) {

            List<String> prerequisite = stringSplitByComma(courseCondition.getPrerequisite());
            List<String> pre_exchangeable = stringSplitByComma(courseCondition.getPre_exchangeable());
            List<String> parallel_condition = stringSplitByComma(courseCondition.getParallel_condition());
            List<String> parallel_exchangeable = stringSplitByComma(courseCondition.getParallel_exchangeable());
            List<String> exclusive_condition = stringSplitByComma(courseCondition.getExclusive_condition());

            if (prerequisite.size() != pre_exchangeable.size() || parallel_condition.size() != exclusive_condition.size()) {
                throw new IllegalArgumentException("Lists must have the same size!");
            }

            boolean allPrerequisiteValid = isAllPrerequisiteValid(prerequisite, pre_exchangeable);
            boolean allParallelValid = isAllParallelValid(parallel_condition, parallel_exchangeable, courseCondition.getCourse_id_name());

            if(!allPrerequisiteValid){
                if(!allParallelValid)
                    errorMassage = "Course " + courseCondition.getCourse_id_name() + "does not meet one or more of the prerequisites and the parallels conditions!";
                else
                    errorMassage = "Course " + courseCondition.getCourse_id_name() + "does not meet one or more of the prerequisites conditions!";
            }
            else if(!allParallelValid)
                errorMassage = "Course " + courseCondition.getCourse_id_name() + "does not meet one or more of the parallels conditions!";


            //הקורס הזה לא עמד בלפחות אחד מתנאי הקדם או התנאי המקביל
            if(!allPrerequisiteValid || !allParallelValid) {
                bedCoursesRequest.add(courseCondition);
                if(errors.isEmpty())
                    errors = new ArrayList<>();
                errors.add(errorMassage);
            }
        }

        return bedCoursesRequest;
    }

    public List<String> getErrors(){
        if(!errors.isEmpty())
            return this.errors;
        return null;
    }

    private List<CSCoursesConditions> getPreferredCoursesConditions(){
        List<CSCoursesConditions> coursesConditions = new ArrayList<>();

        for (String courseId : preferredRequiredCoursesIDs) {
            // Fetch the condition for each courseId
            Optional<CSCoursesConditions> condition = csCoursesConditionsService.findByCourseId(courseId);

            // If the condition is present, add it to the list
            condition.ifPresent(coursesConditions::add);
        }

        for (String courseId : preferredChoiceCoursesIDs) {
            // Fetch the condition for each courseId
            Optional<CSCoursesConditions> condition = csCoursesConditionsService.findByCourseId(courseId);

            // If the condition is present, add it to the list
            condition.ifPresent(coursesConditions::add);
        }

        return coursesConditions;
    }

    private boolean isAllPrerequisiteValid (List<String> prerequisite, List<String> pre_exchangeable) {
        boolean allPrerequisiteValid = true;

        for (int i = 0; i < prerequisite.size(); i++) {

            String prerequisiteCondition = prerequisite.get(i);
            String pre_exchangeableCondition = pre_exchangeable.get(i);

            boolean flag = false;

            for (Course course : studentCourses) {
                if(course.getCourseName().equals(prerequisiteCondition) || course.getCourseName().equals(pre_exchangeableCondition)) {
                    flag = true;
                    break;
                }
            }
            if(!flag){
                allPrerequisiteValid = false;
            }
        }

        return allPrerequisiteValid;
    }

    private boolean isAllParallelValid (List<String> parallel_condition, List<String> parallel_exchangeable, String course_id_name) {
        boolean allParallelValid = true;

        for (int i = 0; i < parallel_condition.size(); i++) {

            String parallelCondition = parallel_condition.get(i);
            String parallel_exchangeableCondition = parallel_exchangeable.get(i);

            boolean flag = false;

            for (Course course : studentCourses) {
                if(course.getCourseName().equals(parallelCondition) || course.getCourseName().equals(parallel_exchangeableCondition)) {
                    flag = true;
                    break;
                }
            }
            //הקןרס הנדרש ללמוד במקביל לא נלמד עדיין, אז צריך לבדוק אם הסטודנט רצה ללמוד אותו עכשיו
            if(!flag){
                String conditionsCourseID = stringSplitByHyphen(course_id_name).get(0);
                for (String courseID : preferredRequiredCoursesIDs) {
                    if(courseID.equals(conditionsCourseID) || courseID.equals(conditionsCourseID)) {
                        flag = true;
                        break;
                    }
                }

                if(!flag){
                    for (String courseID : preferredChoiceCoursesIDs) {
                        if(courseID.equals(conditionsCourseID) || courseID.equals(conditionsCourseID)) {
                            flag = true;
                            break;
                        }
                    }
                }
            }


            if(!flag){
                allParallelValid = false;
            }
        }

        return allParallelValid;
    }


}
