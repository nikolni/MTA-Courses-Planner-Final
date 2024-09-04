package com.meidanet.database.computer.science.course.choice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceCoursesRepository extends JpaRepository<CSCoursesChoice, Long > {
    @Query("SELECT c FROM CSCoursesChoice c WHERE c.group_number = :group_number")
    List<CSCoursesChoice> findByGroup_number(String group_number);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'א'")
    List<CSCoursesChoice> findCoursesByExerciseA(String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'א'")
    List<CSCoursesChoice> findCoursesByLessonA(String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'תרגיל' AND c.semester = 'ב'")
    List<CSCoursesChoice> findCoursesByExerciseB(String courseIdName);

    @Query("SELECT c FROM CSCoursesChoice c WHERE c.course_id_name = :courseIdName AND c.lesson_or_exercise = 'שיעור' AND c.semester = 'ב'")
    List<CSCoursesChoice> findCoursesByLessonB(String courseIdName);


}
