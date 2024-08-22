package com.meidanet.system.scheduler;

import com.meidanet.htmlscraper.database.computer.science.course.choice.CSCoursesChoice;
import com.meidanet.htmlscraper.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.htmlscraper.database.computer.science.course.condition.CSCoursesConditions;
import com.meidanet.htmlscraper.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.htmlscraper.database.computer.science.course.required.CSCoursesRequired;
import com.meidanet.htmlscraper.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.htmlscraper.database.courses.Course;
import com.meidanet.htmlscraper.database.courses.CourseService;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.preference.form.course.request.CoursePreferences;
import com.meidanet.system.scheduler.answer.FinalSystem;
import com.meidanet.system.scheduler.helper.Splitter;
import com.meidanet.system.scheduler.validation.CourseConditionsValidator;
import com.meidanet.system.scheduler.validation.ScheduleValidatorService;

import java.util.ArrayList;
import java.util.List;

import static com.meidanet.system.scheduler.helper.Splitter.removeLessonFromCourseID;

public class Scheduler {


    private final List<String> preferredRequiredCoursesIDs = new ArrayList<>();
    private final List<String> preferredChoiceCoursesIDs = new ArrayList<>();

    private List<Course> studentCourses;
    private List<CSCoursesConditions> invalidCoursesRequest;

    private final CourseService courseService;
    private final RequiredCoursesService requiredCoursesService;
    private final ChoiceCoursesService choiceCoursesService;
    private final CoursesConditionsService csCoursesConditionsService;



    public Scheduler(CourseService courseService, RequiredCoursesService requiredCoursesService,
                     ChoiceCoursesService choiceCoursesService, CoursesConditionsService csCoursesConditionsService) {

        this.courseService = courseService;
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesService = choiceCoursesService;
        this.csCoursesConditionsService = csCoursesConditionsService;
    }


    public void getSchedule(PreferencesForm preferencesForm, FinalSystem finalSystem) {
        finalSystem.setStudentID(preferencesForm.getStudentID());

        ScheduleValidatorService scheduleValidatorService = new ScheduleValidatorService();
        getScheduleSemesterA(preferencesForm, finalSystem, scheduleValidatorService);
        getScheduleSemesterB(preferencesForm, finalSystem, scheduleValidatorService);
    }

    private void getScheduleSemesterA(PreferencesForm preferencesForm,FinalSystem finalSystem, ScheduleValidatorService scheduleValidatorService) {
        //semester A
        createListsOfCoursesIDs(preferencesForm.getRequiredSemesterA(), preferencesForm.getChoiceSemesterA());

        getStudentCourses(preferencesForm.getStudentID());

        //fills the class member invalidCoursesRequest
        checkConditions(finalSystem);

        List<CoursePreferences> validRequiredLessons = removeBedLessons(preferencesForm.getRequiredSemesterA());
        List<CoursePreferences> validChoiceLessons = removeBedLessons(preferencesForm.getChoiceSemesterA());

        List<CSCoursesRequired> requiredLessonsHours = getRequiredLessonsHours(validRequiredLessons, "A");
        List<CSCoursesChoice> choiceLessonsHours = getChoiceLessonsHours(validChoiceLessons, "A");

        scheduleValidatorService.validateForSemesterA(requiredLessonsHours, choiceLessonsHours, preferencesForm, finalSystem);
    }

    private void getScheduleSemesterB(PreferencesForm preferencesForm, FinalSystem finalSystem, ScheduleValidatorService scheduleValidatorService) {

        //semester B
        createListsOfCoursesIDs(preferencesForm.getRequiredSemesterB(), preferencesForm.getChoiceSemesterB());

        getStudentCourses(preferencesForm.getStudentID());

        //fills the class member invalidCoursesRequest
        checkConditions(finalSystem);

        List<CoursePreferences> validRequiredLessons = removeBedLessons(preferencesForm.getRequiredSemesterB());
        List<CoursePreferences> validChoiceLessons = removeBedLessons(preferencesForm.getChoiceSemesterB());

        List<CSCoursesRequired> requiredLessonsHours = getRequiredLessonsHours(validRequiredLessons, "B");
        List<CSCoursesChoice> choiceLessonsHours = getChoiceLessonsHours(validChoiceLessons, "B");

        scheduleValidatorService.validateForSemesterB(requiredLessonsHours, choiceLessonsHours, preferencesForm, finalSystem);

    }

    private void createListsOfCoursesIDs(List<CoursePreferences> requiredCourses, List<CoursePreferences> choiceCourses) {
        for (CoursePreferences course : requiredCourses){
            preferredRequiredCoursesIDs.add(removeLessonFromCourseID(course.getGroup_number()));
        }
        for (CoursePreferences course : choiceCourses){
            preferredChoiceCoursesIDs.add(removeLessonFromCourseID(course.getGroup_number()));
        }
    }

    private void getStudentCourses(String studentID) {
        studentCourses = courseService.getStudentCourses(studentID);
    }


    private void checkConditions(FinalSystem finalSystem) {
        CourseConditionsValidator conditionsValidator = new CourseConditionsValidator(preferredRequiredCoursesIDs, preferredChoiceCoursesIDs,
                studentCourses, csCoursesConditionsService );
        invalidCoursesRequest = conditionsValidator.getBedCoursesRequest();
        List<String> errors = conditionsValidator.getErrors();
        if(errors != null){
            for(String error : errors)
                finalSystem.addError(error);
        }
    }

    private List<CoursePreferences> removeBedLessons(List<CoursePreferences> courses){
        List<CoursePreferences> validCourses = new ArrayList<>();
        boolean invalidLesson;

        for (CoursePreferences course : courses) {
            invalidLesson = false;
            for (CSCoursesConditions invalidCourse : invalidCoursesRequest){
                if(removeLessonFromCourseID(course.getGroup_number()).equals(
                        Splitter.stringSplitByHyphen(invalidCourse.getCourse_id_name()).get(0))){
                    invalidLesson = true;
                }
            }
            if(!invalidLesson){
                validCourses.add(course);
            }
        }

        return validCourses;
    }

    private List<CSCoursesRequired> getRequiredLessonsHours(List<CoursePreferences> validCourses, String semester){
        List<CSCoursesRequired> courseHours = new ArrayList<>();

        for (CoursePreferences course : validCourses){
            if(course.getGroup_number().equals("dc"))
                courseHours.addAll(requiredCoursesService.getAllCourseLessons(course.getCourse_id_name(), semester));
            else
                courseHours.addAll(requiredCoursesService.getLessonHours(course.getGroup_number()));
            if(course.isCourseHasExercise()){
                if(course.getExerciseLesson().equals("dc"))
                    courseHours.addAll(requiredCoursesService.getAllCourseExercises(course.getCourse_id_name(), semester));
                else
                    courseHours.addAll(requiredCoursesService.getLessonHours(course.getExerciseLesson()));
            }
        }
        return courseHours;
    }

    private List<CSCoursesChoice> getChoiceLessonsHours(List<CoursePreferences> validCourses, String semester){
        List<CSCoursesChoice> courseHours = new ArrayList<>();

        for (CoursePreferences course : validCourses){
            if(course.getGroup_number().equals("dc"))
                courseHours.addAll(choiceCoursesService.getAllCourseLessons(course.getCourse_id_name(), semester));
            else
                courseHours.addAll(choiceCoursesService.getLessonHours(course.getGroup_number()));
            if(course.isCourseHasExercise()){
                if(course.getExerciseLesson().equals("dc"))
                    courseHours.addAll(choiceCoursesService.getAllCourseExercises(course.getCourse_id_name(), semester));
                else
                    courseHours.addAll(choiceCoursesService.getLessonHours(course.getExerciseLesson()));
            }
        }
        return courseHours;
    }

}
