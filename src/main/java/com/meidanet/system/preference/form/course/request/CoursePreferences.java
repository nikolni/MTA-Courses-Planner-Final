package com.meidanet.system.preference.form.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoursePreferences {

    @JsonProperty("course_code_name")
    private String courseCodeName;

    @JsonProperty("lesson_code")
    private String lessonCode;

    @JsonProperty("has_exercise")
    private boolean hasExercise;

    @JsonProperty("exercise_code")
    private String exerciseCode;

    public CoursePreferences(){
    }

    public CoursePreferences(String courseCodeName, String lessonCode, boolean hasExercise, String exerciseCode){
        this.courseCodeName = courseCodeName;
        this.lessonCode = lessonCode;
        this.hasExercise = hasExercise;
        this.exerciseCode = exerciseCode;
    }

    // Getters and setters
    public String getCourseCodeName() {
        return courseCodeName;
    }

    public void setCourseCodeName(String courseCodeName) {
        this.courseCodeName = courseCodeName;
    }

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLessonCode(String lessonCode) {
        this.lessonCode = lessonCode;
    }

    public boolean isHasExercise() {
        return hasExercise;
    }

    public void setHasExercise(boolean hasExercise) {
        this.hasExercise = hasExercise;
    }

    public String getExerciseCode() {
        return exerciseCode;
    }

    public void setExerciseCode(String exerciseCode) {
        this.exerciseCode = exerciseCode;
    }

//    public String getExerciseLessonByGroupNumber() {
////        int value = Integer.getInteger(lessonCode) + 1;
////        return Integer.toString(value);
//        if (lessonCode == null || lessonCode.length() < 2) {
//            throw new IllegalArgumentException("lessonCode is either null or too short to process");
//        }
//        String lastTwoChars = lessonCode.substring(lessonCode.length() - 2);
//        int number = Integer.parseInt(lastTwoChars);
//        number += 1;
//        String baseString = lessonCode.substring(0, lessonCode.length() - 2);
//        return baseString + String.format("%02d", number);
//    }
}


