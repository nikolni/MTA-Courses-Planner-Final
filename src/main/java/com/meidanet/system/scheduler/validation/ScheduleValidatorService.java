package com.meidanet.system.scheduler.validation;


import com.meidanet.htmlscraper.database.computer.science.course.choice.CSCoursesChoice;
import com.meidanet.htmlscraper.database.computer.science.course.required.CSCoursesRequired;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.preference.form.course.request.CoursePreferences;
import com.meidanet.system.scheduler.answer.FinalSystem;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleValidatorService {
    public static final String REQ_REQ_CONFLICTS = "1";
    public static final String REQ_CHOICE_CONFLICTS = "2";
    public static final String CHOICE_CHOICE_CONFLICTS = "3";


    public void validateForSemesterA(List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                        PreferencesForm preferencesForm, FinalSystem finalSystem){

        searchConflicts(preferencesForm.getRequiredSemesterA(), preferencesForm.getRequiredSemesterA(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_REQ_CONFLICTS, "A");
        searchConflicts(preferencesForm.getRequiredSemesterA(), preferencesForm.getChoiceSemesterA(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_CHOICE_CONFLICTS, "A");
        searchConflicts(preferencesForm.getChoiceSemesterA(), preferencesForm.getChoiceSemesterA(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, CHOICE_CHOICE_CONFLICTS, "A" );

    }



    public void validateForSemesterB(List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                     PreferencesForm preferencesForm, FinalSystem finalSystem){

        searchConflicts(preferencesForm.getRequiredSemesterB(), preferencesForm.getRequiredSemesterB(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_REQ_CONFLICTS, "B");
        searchConflicts(preferencesForm.getRequiredSemesterB(), preferencesForm.getChoiceSemesterB(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_CHOICE_CONFLICTS, "B");
        searchConflicts(preferencesForm.getChoiceSemesterB(), preferencesForm.getChoiceSemesterB(),
                requiredLessonsHours, choiceLessonsHours, finalSystem, CHOICE_CHOICE_CONFLICTS, "B" );
    }

    private void searchConflicts(List<CoursePreferences> primaryList, List<CoursePreferences> secondaryList,
                                 List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                 FinalSystem finalSystem, String type, String semester){

        // Group by course ID and group number
        Map<String, List<CSCoursesRequired>> requiredGroups = groupByRequiredCourseAndGroup(requiredLessonsHours);
        Map<String, List<CSCoursesChoice>> choiceGroups = groupByChoiceCourseAndGroup(choiceLessonsHours);
        Map<String, Boolean> isCourseCompared = new HashMap<>();

        String exeKey1 = null;
        String exeKey2 = null;

        for(CoursePreferences course1 : primaryList){

            CoursePreferences finalCourse;

            if(!course1.getGroup_number().equals("dc")){
                String key1 = course1.getCourse_id_name() + '-' + course1.getGroup_number();

                if(course1.isCourseHasExercise() && course1.getExerciseLesson().equals("dc")){
                    String exeLesson1 = course1.getExerciseLessonByGroupNumber();
                    exeKey1 = course1.getCourse_id_name() + '-' + exeLesson1;
                }
                for( CoursePreferences course2 : secondaryList){

                    if(!course1.getCourse_id_name().equals(course2.getCourse_id_name()) && !course2.getGroup_number().equals("dc") &&
                            (isCourseCompared.get(course2.getCourse_id_name()) == null)){
                        String key2 = course2.getCourse_id_name() + '-' + course2.getGroup_number();

                        if(course2.isCourseHasExercise() && course2.getExerciseLesson().equals("dc")){
                            String exeLesson2 = course2.getExerciseLessonByGroupNumber();
                            exeKey2 = course2.getCourse_id_name() + '-' + exeLesson2;
                        }

                        boolean isConflicted = isConflicted(type, key1, key2, exeKey1, exeKey2, requiredGroups, choiceGroups);

                        if(isConflicted){
                            finalSystem.addError("״Please note: the courses" + course1.getCourse_id_name() + "and" +
                                    course2.getCourse_id_name() + "conflict with study hours. Please choose other study hours");
                        }
                    }
                }
                isCourseCompared.put(course1.getCourse_id_name(), true);

                finalCourse = new CoursePreferences(course1.getCourse_id_name(), course1.getGroup_number(), course1.isCourseHasExercise(), course1.getExerciseLessonByGroupNumber());
                addCourseToFinalSystem(finalSystem, type, finalCourse, semester);

            }

            //course1 is "dc", take care after comparing to all req and all choice
            else{
                finalCourse = dillWithCoursesWithDCGroupNumber(requiredLessonsHours, choiceLessonsHours, finalSystem, type, semester, course1, requiredGroups, choiceGroups);
                addCourseToFinalSystem(finalSystem, type, finalCourse, semester);
            }
        }
    }

    private CoursePreferences dillWithCoursesWithDCGroupNumber(List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                                  FinalSystem finalSystem, String type, String semester, CoursePreferences course1,
                                                  Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        String group_number = "";
        String exercise_group_number = "dc";
        //if course1 is req, take care after comparing to all req and all choice
        if (type.equals(REQ_CHOICE_CONFLICTS)) {
            List<CSCoursesRequired> filteredList= filterRequiredCourses(requiredLessonsHours, course1.getCourse_id_name());
            group_number = searchOptimalRequiredGroupNumber(filteredList, finalSystem, semester, requiredGroups, choiceGroups);
            if(course1.isCourseHasExercise()){
                exercise_group_number = createExerciseNumberFromGroupNumber(group_number);
            }
        }
        //if course1 is choice, take care after comparing to all choice
        else if (type.equals(CHOICE_CHOICE_CONFLICTS)) {
            List<CSCoursesChoice> filteredList= filterChoiceCourses(choiceLessonsHours, course1.getCourse_id_name());
            group_number = searchOptimalChoiceGroupNumber(filteredList, finalSystem, semester, requiredGroups, choiceGroups);
            if(course1.isCourseHasExercise()){
                exercise_group_number = createExerciseNumberFromGroupNumber(group_number);
            }
        }
        return new CoursePreferences(course1.getCourse_id_name(), group_number, course1.isCourseHasExercise(), exercise_group_number);
    }


    private String searchOptimalRequiredGroupNumber(List<CSCoursesRequired> groupNumbersList, FinalSystem finalSystem, String semester,
                                                    Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups) {

        Map<String, List<CSCoursesRequired>> groupNumbersGroups = groupByGroupNumberForRequired(groupNumbersList);

        String group_number = "";

        boolean isConflictFree = true;
        //for every optional group
        for(List<CSCoursesRequired> requiredList : groupNumbersGroups.values()) {
            if(semester.equals("A")){
                isConflictFree = searchConflictInSemesterAReq( finalSystem, isConflictFree, requiredList, requiredGroups, choiceGroups);
            }
            else{
                isConflictFree = searchConflictInSemesterBReq( finalSystem, isConflictFree, requiredList, requiredGroups, choiceGroups);
            }

            group_number = requiredList.get(0).getGroup_number();
            if(isConflictFree){
                break;
            }
        }
        if(!isConflictFree){
            finalSystem.addError("All groups of course" + groupNumbersList.get(0).getCourse_id_name() + "conflict with the other courses. " +
                    "This course is a mandatory course, so it is recommended to change the choice of hours to other courses.");
        }
        return group_number;
    }

    private String searchOptimalChoiceGroupNumber(List<CSCoursesChoice> groupNumbersList, FinalSystem finalSystem, String semester,
                                                    Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups) {

        Map<String, List<CSCoursesChoice>> groupNumbersGroups = groupByGroupNumberForChoice(groupNumbersList);

        String group_number = "";

        boolean isConflictFree = true;
        //for every optional group
        for(List<CSCoursesChoice> choiceList : groupNumbersGroups.values()) {
            if(semester.equals("A")){
                isConflictFree = searchConflictInSemesterAChoice( finalSystem, isConflictFree, choiceList, requiredGroups, choiceGroups);
            }
            else{
                isConflictFree = searchConflictInSemesterBChoice( finalSystem, isConflictFree, choiceList, requiredGroups, choiceGroups);
            }

            group_number = choiceList.get(0).getGroup_number();
            if(isConflictFree){
                break;
            }
        }
        if(!isConflictFree){
            finalSystem.addError("All groups of course" + groupNumbersList.get(0).getCourse_id_name() + "conflict with the other courses. " +
                    "This course is a mandatory course, so it is recommended to change the choice of hours to other courses.");
        }
        return group_number;
    }

    private boolean searchConflictInSemesterAReq(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesRequired> requiredList,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        for(CoursePreferences reqCourse : finalSystem.getRequiredSemesterA()){
            if(isConflictFree) {
                String key = reqCourse.getCourse_id_name() + '-' + reqCourse.getGroup_number();
                List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                isConflictFree = isConflictFree(requiredList, courseFromFinals);
            }
        }
        for(CoursePreferences choiceCourse : finalSystem.getChoiceSemesterA()){
            if(isConflictFree) {
                String key = choiceCourse.getCourse_id_name() + '-' + choiceCourse.getGroup_number();
                List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                isConflictFree = isConflictFree(requiredList, courseFromFinals);
            }
        }
        return isConflictFree;

    }

    private boolean searchConflictInSemesterBReq(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesRequired> requiredList,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        for(CoursePreferences reqCourse : finalSystem.getRequiredSemesterB()){
            if(isConflictFree) {
                String key = reqCourse.getCourse_id_name() + '-' + reqCourse.getGroup_number();
                List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                isConflictFree = isConflictFree(requiredList, courseFromFinals);
            }
        }
        for(CoursePreferences choiceCourse : finalSystem.getChoiceSemesterB()){
            if(isConflictFree) {
                String key = choiceCourse.getCourse_id_name() + '-' + choiceCourse.getGroup_number();
                List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                isConflictFree = isConflictFree(requiredList, courseFromFinals);
            }
        }
        return isConflictFree;

    }

    private boolean searchConflictInSemesterAChoice(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesChoice> choiceList,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        for(CoursePreferences reqCourse : finalSystem.getRequiredSemesterA()){
            if(isConflictFree) {
                String key = reqCourse.getCourse_id_name() + '-' + reqCourse.getGroup_number();
                List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                isConflictFree = isConflictFree(choiceList, courseFromFinals);
            }
        }
        for(CoursePreferences choiceCourse : finalSystem.getChoiceSemesterA()){
            if(isConflictFree) {
                String key = choiceCourse.getCourse_id_name() + '-' + choiceCourse.getGroup_number();
                List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                isConflictFree = isConflictFree(choiceList, courseFromFinals);
            }
        }

        return isConflictFree;
    }

    private boolean searchConflictInSemesterBChoice(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesChoice> choiceList,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        for(CoursePreferences reqCourse : finalSystem.getRequiredSemesterB()){
            if(isConflictFree) {
                String key = reqCourse.getCourse_id_name() + '-' + reqCourse.getGroup_number();
                List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                isConflictFree = isConflictFree(choiceList, courseFromFinals);
            }
        }
        for(CoursePreferences choiceCourse : finalSystem.getChoiceSemesterB()){
            if(isConflictFree) {
                String key = choiceCourse.getCourse_id_name() + '-' + choiceCourse.getGroup_number();
                List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                isConflictFree = isConflictFree(choiceList, courseFromFinals);
            }
        }
        return isConflictFree;

    }

    private boolean isConflicted(String type,String key1, String key2, String exeKey1, String exeKey2, Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        boolean isConflicted = false;

        switch (type){
            case REQ_REQ_CONFLICTS:
                List<CSCoursesRequired> list1 =requiredGroups.get(key1);
                List<CSCoursesRequired> list2 = requiredGroups.get(key2);

                if(exeKey1 != null){
                    list1.addAll(requiredGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list2.addAll(requiredGroups.get(exeKey2));
                }
                isConflicted =  isConflictFree(list1, list2);
                break;

            case REQ_CHOICE_CONFLICTS:
                List<CSCoursesRequired> list3 =requiredGroups.get(key1);
                List<CSCoursesChoice> list4 = choiceGroups.get(key2);

                if(exeKey1 != null){
                    list3.addAll(requiredGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list4.addAll(choiceGroups.get(exeKey2));
                }
                isConflicted =  isConflictFree(list3, list4);
                break;

            case CHOICE_CHOICE_CONFLICTS:
                List<CSCoursesChoice> list5 =choiceGroups.get(key1);
                List<CSCoursesChoice> list6 = choiceGroups.get(key2);

                if(exeKey1 != null){
                    list5.addAll(choiceGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list6.addAll(choiceGroups.get(exeKey2));
                }
                isConflicted =  isConflictFree(list5, list6);
                break;
        }
        return isConflicted;
    }



    private <T, U> boolean isConflictFree(List<T> primaryLessons, List<U> secondaryLessons) {
        for (T lesson1 : primaryLessons) {
            for (U lesson2 : secondaryLessons) {
                if (isTimeConflict(lesson1, lesson2)) {
                    return false; // Conflict found
                }
            }
        }
        return true; // No conflicts
    }


    private boolean isTimeConflict(Object lesson1, Object lesson2) {
        String day1 = (lesson1 instanceof CSCoursesRequired) ? ((CSCoursesRequired) lesson1).getDay() : ((CSCoursesChoice) lesson1).getDay();
        String day2 = (lesson2 instanceof CSCoursesRequired) ? ((CSCoursesRequired) lesson2).getDay() : ((CSCoursesChoice) lesson2).getDay();

        if (!day1.equals(day2)) {
            return false; // No conflict if the days are different
        }

        LocalTime start1 = (lesson1 instanceof CSCoursesRequired) ? LocalTime.parse(((CSCoursesRequired) lesson1).getStart_time()) : LocalTime.parse(((CSCoursesChoice) lesson1).getStart_time());
        LocalTime end1 = (lesson1 instanceof CSCoursesRequired) ? LocalTime.parse(((CSCoursesRequired) lesson1).getEnd_time()) : LocalTime.parse(((CSCoursesChoice) lesson1).getEnd_time());

        LocalTime start2 = (lesson2 instanceof CSCoursesRequired) ? LocalTime.parse(((CSCoursesRequired) lesson2).getStart_time()) : LocalTime.parse(((CSCoursesChoice) lesson2).getStart_time());
        LocalTime end2 = (lesson2 instanceof CSCoursesRequired) ? LocalTime.parse(((CSCoursesRequired) lesson2).getEnd_time()) : LocalTime.parse(((CSCoursesChoice) lesson2).getEnd_time());

        return start1.isBefore(end2) && start2.isBefore(end1); // Check if the time ranges overlap
    }

    private void addCourseToFinalSystem(FinalSystem finalSystem, String type, CoursePreferences finalCourse, String semester) {

        if(type.equals(REQ_CHOICE_CONFLICTS)){
            if(semester.equals("A"))
                finalSystem.addReqCourseSemA(finalCourse);
            if(semester.equals("B"))
                finalSystem.addReqCourseSemB(finalCourse);
        }
        if(type.equals(CHOICE_CHOICE_CONFLICTS)){
            if(semester.equals("A"))
                finalSystem.addChoCourseSemA(finalCourse);
            if(semester.equals("B"))
                finalSystem.addChoCourseSemB(finalCourse);
        }
    }

    public List<CSCoursesRequired> filterRequiredCourses(List<CSCoursesRequired> primaryList, String courseName) {
        List<CSCoursesRequired> filteredList = new ArrayList<>();
        for (CSCoursesRequired course : primaryList) {
            if (courseName.equals(course.getCourse_id_name()) && "שיעור".equals(course.getLesson_or_exercise())) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }

    public List<CSCoursesChoice> filterChoiceCourses(List<CSCoursesChoice> primaryList, String courseName) {
        List<CSCoursesChoice> filteredList = new ArrayList<>();
        for (CSCoursesChoice course : primaryList) {
            if (courseName.equals(course.getCourse_id_name()) && "שיעור".equals(course.getLesson_or_exercise())) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }

    private Map<String, List<CSCoursesRequired>> groupByGroupNumberForRequired(List<CSCoursesRequired> lessons) {
        return lessons.stream()
                .collect(Collectors.groupingBy(CSCoursesRequired::getGroup_number));
    }

    private Map<String, List<CSCoursesChoice>> groupByGroupNumberForChoice(List<CSCoursesChoice> lessons) {
        return lessons.stream()
                .collect(Collectors.groupingBy(CSCoursesChoice::getGroup_number));
    }

    private Map<String, List<CSCoursesRequired>> groupByRequiredCourseAndGroup(List<CSCoursesRequired> lessons) {
        return lessons.stream()
                .collect(Collectors.groupingBy(lesson -> lesson.getCourse_id_name() + "-" + lesson.getGroup_number()));
    }

    private Map<String, List<CSCoursesChoice>> groupByChoiceCourseAndGroup(List<CSCoursesChoice> lessons) {
        return lessons.stream()
                .collect(Collectors.groupingBy(lesson -> lesson.getCourse_id_name() + "-" + lesson.getGroup_number()));
    }


    private String createExerciseNumberFromGroupNumber(String groupNumber) {
        Integer value = Integer.getInteger(groupNumber) + 1;
        return value.toString();
    }
}
