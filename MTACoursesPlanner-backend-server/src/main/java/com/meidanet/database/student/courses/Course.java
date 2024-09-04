package com.meidanet.database.student.courses;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(CourseId.class)
public class Course {

    @Id
    private String student_id;
    @Id
    private String course_id;
    private String course_name;


    // Getters and setters
    public String getCourseId() {
        return course_id;
    }

    public void setCourseId(String coursesId) {
        this.course_id = coursesId;
    }

    public String getCourseName() {
        return course_name;
    }

    public void setCourseName(String coursesName) {
        this.course_name = coursesName;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
