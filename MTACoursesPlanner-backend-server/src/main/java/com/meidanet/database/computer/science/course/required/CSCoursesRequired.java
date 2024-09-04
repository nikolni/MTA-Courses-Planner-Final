package com.meidanet.database.computer.science.course.required;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(CSCoursesRequiredId.class)
public class CSCoursesRequired {
    private String course_year;
    @Id
    private String course_id_name;
    private String lesson_or_exercise;
    @Id
    private String group_number;
    private String semester;
    @Id
    private String day;
    private String start_time;
    private String end_time;
    private String lecturer_name;
    private String course_type = "required";

    public String getCourse_id_name() {
        return course_id_name;
    }

    public void setCourse_id_name(String course_id_name) {
        this.course_id_name = course_id_name;
    }


    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLecturer_name() {
        return lecturer_name;
    }

    public void setLecturer_name(String lecturer_name) {
        this.lecturer_name = lecturer_name;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getGroup_number() {
        return group_number;
    }

    public void setGroup_number(String group_number) {
        this.group_number = group_number;
    }

    public String getLesson_or_exercise() {
        return lesson_or_exercise;
    }

    public void setLesson_or_exercise(String lesson_or_exercise) {
        this.lesson_or_exercise = lesson_or_exercise;
    }

    public String getCourse_year() {
        return course_year;
    }

    public void setCourse_year(String course_year) {
        this.course_year = course_year;
    }
}
