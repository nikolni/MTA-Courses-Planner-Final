package com.meidanet.htmlscraper.database.computer.science.course.choice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChoiceCoursesRepository extends JpaRepository<CSCoursesChoice, Long > {
    List<CSCoursesChoice> findByGroup_number(String group_number);
    List<CSCoursesChoice> findByCourse_id_name(String courseIDName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'א'")
    List<CSCoursesChoice> findCoursesByExerciseA(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'א'")
    List<CSCoursesChoice> findCoursesByLessonA(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'ב'")
    List<CSCoursesChoice> findCoursesByExerciseB(@Param("course_id_name") String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'ב'")
    List<CSCoursesChoice> findCoursesByLessonB(@Param("course_id_name") String courseIdName);


}
