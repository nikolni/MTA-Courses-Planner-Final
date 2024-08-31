package com.meidanet.database.computer.science.course.choice;

import java.io.Serializable;
import java.util.Objects;


public class CSCoursesChoiceId implements Serializable {
    private String course_id_name;
    private String group_number;
    private String day;

    // Default constructor
    public CSCoursesChoiceId() {
    }

    // Parameterized constructor
    public CSCoursesChoiceId(String course_id_name, String group_number,
                               String day) {
        this.course_id_name = course_id_name;
        this.group_number = group_number;
        this.day = day;
    }


    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSCoursesChoiceId csCoursesChoiceId = (CSCoursesChoiceId) o;
        return Objects.equals(course_id_name, csCoursesChoiceId.course_id_name) &&
                Objects.equals(group_number, csCoursesChoiceId.group_number) &&
                Objects.equals(day, csCoursesChoiceId.day);
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