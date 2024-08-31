package com.meidanet.database.student.courses;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    //@Transactional
    public Course addCourse(String[] courseDetails, String stuID) {
        String courseID = courseDetails[0];
        // Check if the course name already exists
        List<Course> existingCourses = entityManager
                .createQuery("SELECT c FROM Course c WHERE c.student_id = :stuID AND c.course_id = :courseID", Course.class)
                .setParameter("courseID", courseID)
                .setParameter("stuID", stuID)
                .getResultList();

        if (existingCourses.isEmpty()) {
            // Course name does not exist, add new course
            Course course = new Course();
            course.setStudent_id(stuID);
            course.setCourseId(courseDetails[0]);
            course.setCourseName(courseDetails[1]);


            entityManager.persist(course);
            return course;
        } else {
            // Handle the case when the course name is already in the database
            return null;
        }
    }

    public String[] extractCourseIdAndName(String courseInfo) {
        // Regular expression to match course ID (assumed to be numeric) and course name
        Pattern pattern = Pattern.compile("(\\d+)\\s+(.+)");
        Matcher matcher = pattern.matcher(courseInfo);

        if (matcher.find()) {
            String courseId = matcher.group(1);
            String courseName = matcher.group(2);
            return new String[]{courseId, courseName};
        } else {
            throw new IllegalArgumentException("Invalid course info format");
        }
    }

    public List<Course> getStudentCourses(String student_id){
        return courseRepository.findByStudent_id(student_id);
    }
}
