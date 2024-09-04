package com.meidanet.database.computer.science.course.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesConditionsService {
    private final CoursesConditionsRepository coursesConditionsRepository;

    @Autowired
    public CoursesConditionsService(CoursesConditionsRepository coursesConditionsRepository) {
        this.coursesConditionsRepository = coursesConditionsRepository;
    }

    //@Transactional
    public CSCoursesConditions addCourseCondition(List<String> courseData) {
        CSCoursesConditions conditions = new CSCoursesConditions();

        conditions.setCourse_id_name(courseData.get(0));
        conditions.setPrerequisite(courseData.get(1));
        conditions.setParallel_condition(courseData.get(2));
        conditions.setExclusive_condition(courseData.get(3));

        return conditions;
    }

    public CSCoursesConditions findByCourseId(String courseID) {
        return coursesConditionsRepository.findByCourseId(courseID);
    }

    public CSCoursesConditions findByCourseName(String courseName) {
        return coursesConditionsRepository.findByCourseName(courseName);
    }
}
