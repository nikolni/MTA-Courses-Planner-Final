package com.meidanet.htmlscraper.database.computer.science.course.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoursesConditionsRepository extends JpaRepository<CSCoursesConditions, Long > {

    @Query("SELECT c FROM CSCoursesConditions c WHERE c.course_id_name LIKE CONCAT(:courseId, '%')")
    Optional<CSCoursesConditions> findByCourseId(@Param("courseId") String courseId);
}
