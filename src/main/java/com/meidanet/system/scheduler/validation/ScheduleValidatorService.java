package com.meidanet.system.scheduler.validation;


import com.meidanet.database.computer.science.course.choice.CSCoursesChoice;
import com.meidanet.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.database.computer.science.course.required.CSCoursesRequired;
import com.meidanet.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.system.preference.form.course.request.CoursePreferences;
import com.meidanet.system.scheduler.answer.FinalSystem;
import com.meidanet.system.scheduler.helper.FinalSystemHelper;

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

   private final RequiredCoursesService requiredCoursesService;
   private final ChoiceCoursesService choiceCoursesService;
   private final FinalSystemHelper finalSystemHelper = new FinalSystemHelper();


    public ScheduleValidatorService(RequiredCoursesService requiredCoursesService, ChoiceCoursesService choiceCoursesService){
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesService = choiceCoursesService;
    }

    public void validateForSemesterA(List<CoursePreferences> validRequiredLessons, List<CSCoursesRequired> requiredLessonsHours, List<CoursePreferences> validChoiceLessons,
                                     List<CSCoursesChoice> choiceLessonsHours, FinalSystem finalSystem){

        searchConflicts(validRequiredLessons, validRequiredLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_REQ_CONFLICTS, "A");
        searchConflicts(validRequiredLessons, validChoiceLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_CHOICE_CONFLICTS, "A");
        searchConflicts(validChoiceLessons, validChoiceLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, CHOICE_CHOICE_CONFLICTS, "A" );

    }



    public void validateForSemesterB(List<CoursePreferences> validRequiredLessons, List<CSCoursesRequired> requiredLessonsHours, List<CoursePreferences> validChoiceLessons,
                                     List<CSCoursesChoice> choiceLessonsHours, FinalSystem finalSystem){
        searchConflicts(validRequiredLessons, validRequiredLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_REQ_CONFLICTS, "B");
        searchConflicts(validRequiredLessons, validChoiceLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, REQ_CHOICE_CONFLICTS, "B");
        searchConflicts(validChoiceLessons, validChoiceLessons,
                requiredLessonsHours, choiceLessonsHours, finalSystem, CHOICE_CHOICE_CONFLICTS, "B" );
    }

    private void searchConflicts(List<CoursePreferences> primaryList, List<CoursePreferences> secondaryList,
                                 List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                 FinalSystem finalSystem, String type, String semester){

        // Group by course ID and group number
        Map<String, List<CSCoursesRequired>> requiredGroups = groupByRequiredCourseAndGroup(requiredLessonsHours);
        Map<String, List<CSCoursesChoice>> choiceGroups = groupByChoiceCourseAndGroup(choiceLessonsHours);
        Map<String, Boolean> isCourseCompared = new HashMap<>();
        List<String> changes = null;

        String exeKey1 = null;
        String exeKey2 = null;

        for(CoursePreferences course1 : primaryList){
            if(changes != null)
                changes.clear();

            CoursePreferences finalCourse;
            String exeLesson1 = "";

            if(!course1.getLessonCode().equals("dc")){

                String key1 = course1.getCourseCodeName() + '-' + course1.getLessonCode();

                if(course1.isHasExercise()) {
                    if (course1.getExerciseCode().equals("dc")) {
                        exeLesson1 = createExerciseNumberFromGroupNumber(course1.getLessonCode());
                        if(changes == null)
                            changes = new ArrayList<>();
                        changes.add("In course '" + course1.getCourseCodeName() + "' a default selection was made for the exercise group.");
                        exeKey1 = course1.getCourseCodeName() + '-' + exeLesson1;
                    } else {
                        exeLesson1 = course1.getExerciseCode();
                    }
                }

                for( CoursePreferences course2 : secondaryList){

                    if(!course1.getCourseCodeName().equals(course2.getCourseCodeName()) && !course2.getLessonCode().equals("dc") &&
                            (isCourseCompared.get(course2.getCourseCodeName()) == null)){
                        String key2 = course2.getCourseCodeName() + '-' + course2.getLessonCode();

                        if(course2.isHasExercise() && course2.getExerciseCode().equals("dc")){
                            String exeLesson2 = createExerciseNumberFromGroupNumber(course2.getLessonCode());
                            exeKey2 = course2.getCourseCodeName() + '-' + exeLesson2;
                        }

                        boolean isConflictFree = isConflicted(type, key1, key2, exeKey1, exeKey2, requiredGroups, choiceGroups);

                        if(!isConflictFree){
                            if(semester.equals("A")){
                                finalSystem.addErrorA("Please note: the courses: '" + course1.getCourseCodeName() + "' and '" + course2.getCourseCodeName() +
                                        "' conflict with study hours. Please choose other study hours");
                            }
                            else
                                finalSystem.addErrorB("Please note: the courses: '" + course1.getCourseCodeName() + "' and '" + course2.getCourseCodeName() +
                                    "' conflict with study hours. Please choose other study hours");
                        }
                    }
                }
                isCourseCompared.put(course1.getCourseCodeName(), true);

                finalCourse = new CoursePreferences(course1.getCourseCodeName(), course1.getLessonCode(), course1.isHasExercise(), exeLesson1);
                addCourseToFinalSystem(finalSystem, type, finalCourse, semester, changes);

            }

            //course1 is "dc", take care after comparing to all req and all choice
            else{
                finalCourse = dillWithCoursesWithDCGroupNumber(requiredLessonsHours, choiceLessonsHours, finalSystem, type,
                        semester, course1, changes, requiredGroups, choiceGroups);
                addCourseToFinalSystem(finalSystem, type, finalCourse, semester, changes);
            }
        }
    }

    private CoursePreferences dillWithCoursesWithDCGroupNumber(List<CSCoursesRequired> requiredLessonsHours, List<CSCoursesChoice> choiceLessonsHours,
                                                  FinalSystem finalSystem, String type, String semester, CoursePreferences course1, List<String> changes,
                                                  Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        String group_number = "";
        String exercise_group_number = "";

        if(changes == null)
            changes = new ArrayList<>();
        changes.add("In course '" + course1.getCourseCodeName() + "' a default selection was made for the lesson group.");

        //if course1 is req, take care after comparing to all req and all choice
        if (type.equals(REQ_CHOICE_CONFLICTS)) {
            List<CSCoursesRequired> filteredList= filterRequiredCourses(requiredLessonsHours, course1.getCourseCodeName());
            group_number = searchOptimalRequiredGroupNumber(filteredList, finalSystem, semester, requiredGroups, choiceGroups);
            if(course1.isHasExercise()){
                changes.add("In course '" + course1.getCourseCodeName() + "' a default selection was made for the exercise group.");
                exercise_group_number = createExerciseNumberFromGroupNumber(group_number);
            }
        }
        //if course1 is choice, take care after comparing to all choice
        else if (type.equals(CHOICE_CHOICE_CONFLICTS)) {
            List<CSCoursesChoice> filteredList= filterChoiceCourses(choiceLessonsHours, course1.getCourseCodeName());
            group_number = searchOptimalChoiceGroupNumber(filteredList, finalSystem, semester, requiredGroups, choiceGroups);
            if(course1.isHasExercise()){
                changes.add("In course '" + course1.getCourseCodeName() + "' a default selection was made for the exercise group.");
                exercise_group_number = createExerciseNumberFromGroupNumber(group_number);
            }
        }
        return new CoursePreferences(course1.getCourseCodeName(), group_number, course1.isHasExercise(), exercise_group_number);
    }


    private String searchOptimalRequiredGroupNumber(List<CSCoursesRequired> groupNumbersList, FinalSystem finalSystem, String semester,
                                                    Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups) {

        Map<String, List<CSCoursesRequired>> groupNumbersGroups = groupByGroupNumberForRequired(groupNumbersList); //כל הקבוצות של הקורס הזה

        String group_number = "";

        boolean isConflictFree = true;
        //for every optional group
        for(List<CSCoursesRequired> optionalGroup : groupNumbersGroups.values()) {
            if(semester.equals("A")){
                isConflictFree = searchConflictInSemesterAReq( finalSystem, isConflictFree, optionalGroup, requiredGroups, choiceGroups);
            }
            else{
                isConflictFree = searchConflictInSemesterBReq( finalSystem, isConflictFree, optionalGroup, requiredGroups, choiceGroups);
            }

            group_number = optionalGroup.get(0).getGroup_number();
            if(isConflictFree){
                break;
            }
        }
        if(!isConflictFree){
            if(semester.equals("A")){
                finalSystem.addErrorA("All groups of course '" + groupNumbersList.get(0).getCourse_id_name() + "' conflict with the other courses." +
                        "This course is a mandatory course, so it is recommended to change the choice of hours to other courses.");

            }
            else
                finalSystem.addErrorB("All groups of course '" + groupNumbersList.get(0).getCourse_id_name() + "' conflict with the other courses." +
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
        for(List<CSCoursesChoice> optionalGroup : groupNumbersGroups.values()) {
            if(semester.equals("A")){
                isConflictFree = searchConflictInSemesterAChoice( finalSystem, isConflictFree, optionalGroup, requiredGroups, choiceGroups);
            }
            else{
                isConflictFree = searchConflictInSemesterBChoice( finalSystem, isConflictFree, optionalGroup, requiredGroups, choiceGroups);
            }

            group_number = optionalGroup.get(0).getGroup_number();
            if(isConflictFree){
                break;
            }
        }
        if(!isConflictFree){
            if(semester.equals("A")){
                finalSystem.addErrorA("All groups of course '" + groupNumbersList.get(0).getCourse_id_name() + "' conflict with the other courses. A default group was selected.");
            }
            else
                finalSystem.addErrorB("All groups of course '" + groupNumbersList.get(0).getCourse_id_name() + "' conflict with the other courses. A default group was selected.");
        }
        return group_number;
    }

    private boolean searchConflictInSemesterAReq(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesRequired> optionalGroup,
                                                 Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        //עבור כל קורס שכבר נוסף לאובייקט הסופי, נבדוק שהקבוצה הנבדקת לא גורמת להתנגשויות
        if(finalSystem.getRequiredSemesterA() != null) {
            for (CoursePreferences reqCourse : finalSystemHelper.getRequiredSemesterA()) {
                if (isConflictFree) {
                    String key = reqCourse.getCourseCodeName() + '-' + reqCourse.getLessonCode();
                    List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                    if(reqCourse.isHasExercise()){
                        String keyExercise = reqCourse.getCourseCodeName() + '-' + reqCourse.getExerciseCode();
                        courseFromFinals.addAll(requiredGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        if(finalSystem.getChoiceSemesterA() != null) {
            for (CoursePreferences choiceCourse : finalSystemHelper.getChoiceSemesterA()) {
                if (isConflictFree){
                    String key = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getLessonCode();
                    List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                    if(choiceCourse.isHasExercise()){
                        String keyExercise = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getExerciseCode();
                        courseFromFinals.addAll(choiceGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        return isConflictFree;

    }

    private boolean searchConflictInSemesterBReq(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesRequired> optionalGroup,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        if(finalSystem.getRequiredSemesterB() != null) {
            for (CoursePreferences reqCourse : finalSystemHelper.getRequiredSemesterB()) {
                if (isConflictFree){
                    String key = reqCourse.getCourseCodeName() + '-' + reqCourse.getLessonCode();
                    List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                    if(reqCourse.isHasExercise()){
                        String keyExercise = reqCourse.getCourseCodeName() + '-' + reqCourse.getExerciseCode();
                        courseFromFinals.addAll(requiredGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        if(finalSystem.getChoiceSemesterB() != null) {
            for (CoursePreferences choiceCourse : finalSystemHelper.getChoiceSemesterB()) {
                if (isConflictFree) {
                    String key = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getLessonCode();
                    List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                    if(choiceCourse.isHasExercise()){
                        String keyExercise = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getExerciseCode();
                        courseFromFinals.addAll(choiceGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        return isConflictFree;

    }

    private boolean searchConflictInSemesterAChoice(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesChoice> optionalGroup,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        if(finalSystem.getRequiredSemesterA() != null) {
            for (CoursePreferences reqCourse : finalSystemHelper.getRequiredSemesterA()) {
                if (isConflictFree) {
                    String key = reqCourse.getCourseCodeName() + '-' + reqCourse.getLessonCode();
                    List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                    if(reqCourse.isHasExercise()){
                        String keyExercise = reqCourse.getCourseCodeName() + '-' + reqCourse.getExerciseCode();
                        courseFromFinals.addAll(requiredGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        if(finalSystem.getChoiceSemesterA() != null) {
            for (CoursePreferences choiceCourse : finalSystemHelper.getChoiceSemesterA()) {
                if (isConflictFree) {
                    String key = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getLessonCode();
                    List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                    if(choiceCourse.isHasExercise()){
                        String keyExercise = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getExerciseCode();
                        courseFromFinals.addAll(choiceGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        return isConflictFree;
    }

    private boolean searchConflictInSemesterBChoice(FinalSystem finalSystem, boolean isConflictFree, List<CSCoursesChoice> optionalGroup,
                                              Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        if(finalSystem.getRequiredSemesterB() != null) {
            for (CoursePreferences reqCourse : finalSystemHelper.getRequiredSemesterB()) {
                if (isConflictFree) {
                    String key = reqCourse.getCourseCodeName() + '-' + reqCourse.getLessonCode();
                    List<CSCoursesRequired> courseFromFinals = requiredGroups.get(key);
                    if(reqCourse.isHasExercise()){
                        String keyExercise = reqCourse.getCourseCodeName() + '-' + reqCourse.getExerciseCode();
                        courseFromFinals.addAll(requiredGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        if(finalSystem.getChoiceSemesterB() != null) {
            for (CoursePreferences choiceCourse : finalSystemHelper.getChoiceSemesterB()) {
                if (isConflictFree) {
                    String key = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getLessonCode();
                    List<CSCoursesChoice> courseFromFinals = choiceGroups.get(key);
                    if(choiceCourse.isHasExercise()){
                        String keyExercise = choiceCourse.getCourseCodeName() + '-' + choiceCourse.getExerciseCode();
                        courseFromFinals.addAll(choiceGroups.get(keyExercise));
                    }
                    isConflictFree = isConflictFree(optionalGroup, courseFromFinals);
                }
            }
        }
        return isConflictFree;
    }


    private boolean isConflicted(String type,String key1, String key2, String exeKey1, String exeKey2, Map<String, List<CSCoursesRequired>> requiredGroups, Map<String, List<CSCoursesChoice>> choiceGroups){
        boolean isConflictFree = false;

        switch (type){
            case REQ_REQ_CONFLICTS:
                List<CSCoursesRequired> list1 =requiredGroups.get(key1);
                List<CSCoursesRequired> list2 = requiredGroups.get(key2);

                if(exeKey1 != null){
                    list1.addAll(requiredGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list2.addAll(requiredGroups.get(exeKey2));
                }
                isConflictFree =  isConflictFree(list1, list2);
                break;

            case REQ_CHOICE_CONFLICTS:
                List<CSCoursesRequired> list3 =requiredGroups.get(key1);
                List<CSCoursesChoice> list4 = choiceGroups.get(key2);

                if(exeKey1 != null){
                    list3.addAll(requiredGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list4.addAll(choiceGroups.get(exeKey2));
                }
                isConflictFree =  isConflictFree(list3, list4);
                break;

            case CHOICE_CHOICE_CONFLICTS:
                List<CSCoursesChoice> list5 =choiceGroups.get(key1);
                List<CSCoursesChoice> list6 = choiceGroups.get(key2);

                if(exeKey1 != null){
                    list5.addAll(choiceGroups.get(exeKey1));
                    if(exeKey2 != null)
                        list6.addAll(choiceGroups.get(exeKey2));
                }
                isConflictFree =  isConflictFree(list5, list6);
                break;
        }
        return isConflictFree;
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

    private void addCourseToFinalSystem(FinalSystem finalSystem, String type, CoursePreferences finalCourse, String semester, List<String> changes) {

        if(type.equals(REQ_CHOICE_CONFLICTS)){
            List<CSCoursesRequired> requiredList = requiredCoursesService.getLessonHours(finalCourse.getLessonCode());
            if(finalCourse.isHasExercise()){
                requiredList.addAll(requiredCoursesService.getLessonHours(finalCourse.getExerciseCode()));
            }
            if(semester.equals("A")) {
                finalSystemHelper.addReqCourseSemA(finalCourse);
                for(CSCoursesRequired required : requiredList)
                    finalSystem.addReqCourseSemA(required);
                finalSystem.addChangesA(changes);
            }
            if(semester.equals("B")) {
                finalSystemHelper.addReqCourseSemB(finalCourse);
                for(CSCoursesRequired required : requiredList)
                    finalSystem.addReqCourseSemB(required);
                finalSystem.addChangesB(changes);
            }
        }
        if(type.equals(CHOICE_CHOICE_CONFLICTS)){
            List<CSCoursesChoice> choiceList = choiceCoursesService.getLessonHours(finalCourse.getLessonCode());
            if(finalCourse.isHasExercise()){
                choiceList.addAll(choiceCoursesService.getLessonHours(finalCourse.getExerciseCode()));
            }
            if(semester.equals("A")) {
                finalSystemHelper.addChoCourseSemA(finalCourse);
                for(CSCoursesChoice choice : choiceList)
                    finalSystem.addChoCourseSemA(choice);
                finalSystem.addChangesA(changes);

            }
            if(semester.equals("B")) {
                finalSystemHelper.addChoCourseSemB(finalCourse);
                for(CSCoursesChoice choice : choiceList)
                    finalSystem.addChoCourseSemB(choice);
                finalSystem.addChangesB(changes);
            }
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

        if (groupNumber == null || groupNumber.length() < 2) {
            throw new IllegalArgumentException("lessonCode is either null or too short to process");
        }
        if(groupNumber.endsWith("9")){
            String lastTwoChars = groupNumber.substring(groupNumber.length() - 2);
            int number = Integer.parseInt(lastTwoChars);
            number += 1;
            String baseString = groupNumber.substring(0, groupNumber.length() - 2);
            return baseString + String.format("%02d", number);
        }
        else{
            String lastChar = groupNumber.substring(groupNumber.length() - 1);
            int number = Integer.parseInt(lastChar);
            number += 1;
            String baseString = groupNumber.substring(0, groupNumber.length() - 1);
            return baseString + number;
        }


    }
}
