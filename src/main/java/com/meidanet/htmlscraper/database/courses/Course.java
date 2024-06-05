package com.meidanet.htmlscraper.database.courses;

import jakarta.persistence.*;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long course_id;

    private String course_name;

    // Getters and setters
    public Long getCourseId() {
        return course_id;
    }

    public void setCourseId(Long coursesId) {
        this.course_id = coursesId;
    }

    public String getCourseName() {
        return course_name;
    }

    public void setCourseName(String coursesName) {
        this.course_name = coursesName;
    }
}
