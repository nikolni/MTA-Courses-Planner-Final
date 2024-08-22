package com.meidanet.htmlscraper.database.computer.science.course.required;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequiredCoursesRepository extends JpaRepository<CSCoursesRequired, Long > {

    @PersistenceContext
    EntityManager entityManager = null;

    List<CSCoursesRequired> findByGroup_number(String group_number);
    List<CSCoursesRequired> findByCourse_id_name(String courseIDName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'א'")
    List<CSCoursesRequired> findCoursesByExerciseA(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'א'")
    List<CSCoursesRequired> findCoursesByLessonA(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'ב'")
    List<CSCoursesRequired> findCoursesByExerciseB(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'ב'")
    List<CSCoursesRequired> findCoursesByLessonB(@Param("course_id_name") String courseIdName);

}

