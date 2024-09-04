package com.meidanet.database.student.courses;

import java.io.Serializable;
import java.util.Objects;

public class CourseId implements Serializable {
    private String student_id;
    private String course_id;

    // Default constructor
    public CourseId() {}

    // Parameterized constructor
    public CourseId(String student_id, String course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
    }

    // Getters and setters
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(student_id, courseId.student_id) && Objects.equals(course_id, courseId.course_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student_id, course_id);
    }
}
