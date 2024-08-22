package com.meidanet.htmlscraper.database.courses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStudent_id(String student_id);
}
