package com.meidanet.htmlscraper.database.computer.science.course.condition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursesConditionsService {

    @Autowired
    private CoursesConditionsRepository coursesConditionsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CSCoursesConditions addCourseCondition(List<String> courseData) {
        CSCoursesConditions conditions = new CSCoursesConditions();

        conditions.setCourse_id_name(courseData.get(0));
        conditions.setPrerequisite(courseData.get(1));
        conditions.setParallel_condition(courseData.get(2));
        conditions.setExclusive_condition(courseData.get(3));

        return conditions;
    }

    public Optional<CSCoursesConditions> findByCourseId(String courseID) {
        return coursesConditionsRepository.findByCourseId(courseID);
    }
}
