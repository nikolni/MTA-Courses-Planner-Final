package com.meidanet.database.computer.science.course.required;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RequiredCoursesService {

    private final RequiredCoursesRepository requiredCoursesRepository;

    @Autowired
    public RequiredCoursesService(RequiredCoursesRepository requiredCoursesRepository) {
        this.requiredCoursesRepository = requiredCoursesRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CSCoursesRequired addRequiredCourse(List<String> courseData) {
        String courseYear = "שנה ג";
        String courseIDName = "כלים פרקטיים לתעשייה - מתקדם - 131150";

            CSCoursesRequired requiredCourse = new CSCoursesRequired();

            requiredCourse.setCourse_year(courseYear);
            requiredCourse.setCourse_id_name(courseIDName);
            requiredCourse.setGroup_number(courseData.get(0));
            requiredCourse.setLesson_or_exercise(courseData.get(1));
            requiredCourse.setSemester(courseData.get(2));
            requiredCourse.setDay(courseData.get(3));
            requiredCourse.setStart_time(courseData.get(4));
            requiredCourse.setEnd_time(courseData.get(5));
            requiredCourse.setLecturer_name(courseData.get(6));

            return requiredCourse;
    }

    public List<CSCoursesRequired> getLessonHours(String group_number){
        return requiredCoursesRepository.findByGroup_number(group_number);
    }

    public List<CSCoursesRequired> getAllCourseLessons(String courseIdName, String semester){
        if(semester.equals("A")){
            return requiredCoursesRepository.findCoursesByLessonA(courseIdName);
        }
        else
            return requiredCoursesRepository.findCoursesByLessonB(courseIdName);

    }
    public List<CSCoursesRequired> getAllCourseExercises(String courseIdName, String semester){
        if(semester.equals("A")){
            return requiredCoursesRepository.findCoursesByExerciseA(courseIdName);
        }
        else
            return requiredCoursesRepository.findCoursesByExerciseB(courseIdName);

    }

}
