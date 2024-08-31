package com.meidanet.database.computer.science.course.choice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChoiceCoursesService {

    private final ChoiceCoursesRepository choiceCoursesRepository;

    @Autowired
    public ChoiceCoursesService(ChoiceCoursesRepository choiceCoursesRepository) {
        this.choiceCoursesRepository = choiceCoursesRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    //@Transactional
    public CSCoursesChoice addChoiceCourse(List<String> courseData) {
        String courseIDName = "סדנת כישורי למידה - 70005";

        CSCoursesChoice choiceCourses = new CSCoursesChoice();

        choiceCourses.setCourse_id_name(courseIDName);
        choiceCourses.setGroup_number(courseData.get(0));
        choiceCourses.setLesson_or_exercise(courseData.get(1));
        choiceCourses.setSemester(courseData.get(2));
        choiceCourses.setDay(courseData.get(3));
        choiceCourses.setStart_time(courseData.get(4));
        choiceCourses.setEnd_time(courseData.get(5));
        choiceCourses.setLecturer_name(courseData.get(6));

        return choiceCourses;

    }

    public List<CSCoursesChoice> getLessonHours(String group_number){
        return choiceCoursesRepository.findByGroup_number(group_number);
    }

    public List<CSCoursesChoice> getAllCourseLessons(String courseIdName, String semester){
        if(semester.equals("A")){
            return choiceCoursesRepository.findCoursesByLessonA(courseIdName);
        }
        else
            return choiceCoursesRepository.findCoursesByLessonB(courseIdName);

    }
    public List<CSCoursesChoice> getAllCourseExercises(String courseIdName, String semester){
        if(semester.equals("A")){
            return choiceCoursesRepository.findCoursesByExerciseA(courseIdName);
        }
        else
            return choiceCoursesRepository.findCoursesByExerciseB(courseIdName);

    }
}
