package com.meidanet.database.computer.science.course.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesConditionsRepository extends JpaRepository<CSCoursesConditions, Long > {

    @Query("SELECT c FROM CSCoursesConditions c WHERE c.course_id_name LIKE CONCAT('%', :courseId, '%')")
    CSCoursesConditions findByCourseId(String courseId);

    @Query("SELECT c FROM CSCoursesConditions c WHERE c.course_id_name LIKE CONCAT('%', :courseName, '%')")
    CSCoursesConditions findByCourseName(String courseName);
}
