package com.meidanet.system.preference.form.course.request;

public class CoursePreferences {

    private final String course_id_name;
    private String group_number;
    private final boolean isCourseHasExercise;
    private String exerciseLesson;

    public CoursePreferences(String courseIdName, String groupNumber, boolean isCourseHasExercise,
                             String exerciseLesson) {
        this.course_id_name = courseIdName;
        this.group_number = groupNumber;
        this.isCourseHasExercise = isCourseHasExercise;
        if(isCourseHasExercise)
            this.exerciseLesson = exerciseLesson;
    }

    public String getCourse_id_name() {
        return course_id_name;
    }

    public String getGroup_number() {
        return group_number;
    }

    public boolean isCourseHasExercise() {
        return isCourseHasExercise;
    }

    public String getExerciseLesson() {
        return exerciseLesson;
    }

    public void setExerciseLesson(String exerciseLesson){
        this.exerciseLesson = exerciseLesson;
    }

    public String getExerciseLessonByGroupNumber(){
        Integer value = Integer.getInteger(group_number) + 1;
        return value.toString();
    }
}


