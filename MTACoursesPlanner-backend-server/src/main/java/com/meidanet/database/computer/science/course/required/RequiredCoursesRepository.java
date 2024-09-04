package com.meidanet.database.computer.science.course.required;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequiredCoursesRepository extends JpaRepository<CSCoursesRequired, Long > {

    @PersistenceContext
    EntityManager entityManager = null;

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.group_number = :group_number")
    List<CSCoursesRequired> findByGroup_number(String group_number);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'א'")
    List<CSCoursesRequired> findCoursesByExerciseA(String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'א'")
    List<CSCoursesRequired> findCoursesByLessonA(String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'ב'")
    List<CSCoursesRequired> findCoursesByExerciseB(String courseIdName);

    @Query("SELECT c FROM CSCoursesRequired c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'ב'")
    List<CSCoursesRequired> findCoursesByLessonB(String courseIdName);

}

