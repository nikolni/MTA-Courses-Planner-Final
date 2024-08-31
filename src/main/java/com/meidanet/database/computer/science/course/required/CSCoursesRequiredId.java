package com.meidanet.database.computer.science.course.required;

import java.io.Serializable;
import java.util.Objects;

public class CSCoursesRequiredId implements Serializable {
    private String course_id_name;
    private String group_number;
    private String day;

    // Default constructor
    public CSCoursesRequiredId() {
    }

    // Parameterized constructor
    public CSCoursesRequiredId(String course_id, String course_name, String group_number,
                               String lesson_or_exercise, String day) {
        this.course_id_name = course_id;
        this.group_number = group_number;
        this.day = day;
    }


    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSCoursesRequiredId csCoursesRequiredId = (CSCoursesRequiredId) o;
        return Objects.equals(course_id_name, csCoursesRequiredId.course_id_name) &&
                Objects.equals(group_number, csCoursesRequiredId.group_number) &&
                Objects.equals(day, csCoursesRequiredId.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course_id_name, group_number, day);
    }

    public String getCourse_id_name() {
        return course_id_name;
    }

    public void setCourse_id_name(String course_id_name) {
        this.course_id_name = course_id_name;
    }

    public String getGroup_number() {
        return group_number;
    }

    public void setGroup_number(String group_number) {
        this.group_number = group_number;
    }

    public String getDays() {
        return day;
    }

    public void setDays(String days) {
        this.day = days;
    }
}