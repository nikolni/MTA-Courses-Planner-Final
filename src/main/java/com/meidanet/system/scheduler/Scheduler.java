package com.meidanet.system.scheduler;

import com.meidanet.database.computer.science.course.choice.CSCoursesChoice;
import com.meidanet.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.database.computer.science.course.condition.CSCoursesConditions;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.database.computer.science.course.required.CSCoursesRequired;
import com.meidanet.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.database.student.courses.Course;
import com.meidanet.database.student.courses.CourseService;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.preference.form.course.request.CoursePreferences;
import com.meidanet.system.scheduler.answer.FinalSystem;
import com.meidanet.system.scheduler.validation.CourseConditionsValidator;
import com.meidanet.system.scheduler.validation.ScheduleValidatorService;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<Course> studentCourses;
    private List<CSCoursesConditions> invalidCoursesRequest;


    private final CourseService courseService;
    private final RequiredCoursesService requiredCoursesService;
    private final ChoiceCoursesService choiceCoursesService;
    private final CoursesConditionsService csCoursesConditionsService;


    public Scheduler(CourseService courseService, RequiredCoursesService requiredCoursesService,
                                     ChoiceCoursesService choiceCoursesService, CoursesConditionsService csCoursesConditionsService)
    {
        this.courseService = courseService;
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesService = choiceCoursesService;
        this.csCoursesConditionsService = csCoursesConditionsService;

    }



    public void getSchedule(PreferencesForm preferencesForm, FinalSystem finalSystem) {
        finalSystem.setStudentID(preferencesForm.getStudentId());

        ScheduleValidatorService scheduleValidatorService = new ScheduleValidatorService(requiredCoursesService, choiceCoursesService);
        getScheduleSemesterA(preferencesForm, finalSystem, scheduleValidatorService);
        getScheduleSemesterB(preferencesForm, finalSystem, scheduleValidatorService);
    }

    private void getScheduleSemesterA(PreferencesForm preferencesForm,FinalSystem finalSystem, ScheduleValidatorService scheduleValidatorService) {
        //semester A
        getStudentCourses(preferencesForm.getStudentId());

        //fills the class member invalidCoursesRequest
        checkConditions(preferencesForm.getSelectedCoursesData().getRequiredSemesterA(), preferencesForm.getSelectedCoursesData().getChoiceSemesterA(),
                finalSystem, "A");

        List<CoursePreferences> validRequiredLessons = removeBedLessons(preferencesForm.getSelectedCoursesData().getRequiredSemesterA());
        List<CoursePreferences> validChoiceLessons = removeBedLessons(preferencesForm.getSelectedCoursesData().getChoiceSemesterA());

        List<CSCoursesRequired> requiredLessonsHours = getRequiredLessonsHours(validRequiredLessons, "A");
        List<CSCoursesChoice> choiceLessonsHours = getChoiceLessonsHours(validChoiceLessons, "A");

        scheduleValidatorService.validateForSemesterA(validRequiredLessons, requiredLessonsHours, validChoiceLessons, choiceLessonsHours, finalSystem);
    }

    private void getScheduleSemesterB(PreferencesForm preferencesForm, FinalSystem finalSystem, ScheduleValidatorService scheduleValidatorService) {
        //semester B
        getStudentCourses(preferencesForm.getStudentId());

        //fills the class member invalidCoursesRequest
        checkConditions(preferencesForm.getSelectedCoursesData().getRequiredSemesterB(), preferencesForm.getSelectedCoursesData().getChoiceSemesterB(),
                finalSystem, "B");

        List<CoursePreferences> validRequiredLessons = removeBedLessons(preferencesForm.getSelectedCoursesData().getRequiredSemesterB());
        List<CoursePreferences> validChoiceLessons = removeBedLessons(preferencesForm.getSelectedCoursesData().getChoiceSemesterB());

        List<CSCoursesRequired> requiredLessonsHours = getRequiredLessonsHours(validRequiredLessons, "B");
        List<CSCoursesChoice> choiceLessonsHours = getChoiceLessonsHours(validChoiceLessons, "B");

        scheduleValidatorService.validateForSemesterB(validRequiredLessons, requiredLessonsHours, validChoiceLessons, choiceLessonsHours, finalSystem);


    }

    private void getStudentCourses(String student_id) {
        studentCourses = courseService.getStudentCourses(student_id);
    }


    private void checkConditions(List<CoursePreferences> requiredList, List<CoursePreferences> choiceList, FinalSystem finalSystem, String semester) {
        CourseConditionsValidator conditionsValidator = new CourseConditionsValidator(studentCourses, csCoursesConditionsService, requiredList, choiceList );
        invalidCoursesRequest = conditionsValidator.getBedCoursesRequest();
        List<String> errors = conditionsValidator.getErrorsList();
        if(errors != null){
            for(String error : errors) {
                if(semester.equals("A"))
                    finalSystem.addErrorA(error);
                else
                    finalSystem.addErrorB(error);
            }
        }
    }

    private List<CoursePreferences> removeBedLessons(List<CoursePreferences> courses){
        List<CoursePreferences> validCourses = new ArrayList<>();
        boolean invalidLesson;

        for (CoursePreferences course : courses) {
            if (!course.getCourseCodeName().isEmpty()) {
                invalidLesson = false;
                for (CSCoursesConditions invalidCourse : invalidCoursesRequest) {
                    if (course.getCourseCodeName().equals(invalidCourse.getCourse_id_name())) {
                        invalidLesson = true;
                        break;
                    }
                }
                if (!invalidLesson) {
                    validCourses.add(course);
                }
            }
        }

        return validCourses;
    }

    private List<CSCoursesRequired> getRequiredLessonsHours(List<CoursePreferences> validCourses, String semester){
        List<CSCoursesRequired> courseHours = new ArrayList<>();

        for (CoursePreferences course : validCourses){
            if(course.getLessonCode().equals("dc"))
                courseHours.addAll(requiredCoursesService.getAllCourseLessons(course.getCourseCodeName(), semester));
            else
                courseHours.addAll(requiredCoursesService.getLessonHours(course.getLessonCode()));
            if(course.isHasExercise()){
                if(course.getExerciseCode().equals("dc"))
                    courseHours.addAll(requiredCoursesService.getAllCourseExercises(course.getCourseCodeName(), semester));
                else
                    courseHours.addAll(requiredCoursesService.getLessonHours(course.getExerciseCode()));
            }
        }
        return courseHours;
    }

    private List<CSCoursesChoice> getChoiceLessonsHours(List<CoursePreferences> validCourses, String semester){
        List<CSCoursesChoice> courseHours = new ArrayList<>();

        for (CoursePreferences course : validCourses){
            if(course.getLessonCode().equals("dc"))
                courseHours.addAll(choiceCoursesService.getAllCourseLessons(course.getCourseCodeName(), semester));
            else
                courseHours.addAll(choiceCoursesService.getLessonHours(course.getLessonCode()));
            if(course.isHasExercise()){
                if(course.getExerciseCode().equals("dc"))
                    courseHours.addAll(choiceCoursesService.getAllCourseExercises(course.getCourseCodeName(), semester));
                else
                    courseHours.addAll(choiceCoursesService.getLessonHours(course.getExerciseCode()));
            }
        }
        return courseHours;
    }



}
