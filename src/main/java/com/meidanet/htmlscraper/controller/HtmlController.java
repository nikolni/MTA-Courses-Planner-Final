package com.meidanet.htmlscraper.controller;

import com.meidanet.htmlscraper.database.courses.Course;
import com.meidanet.htmlscraper.database.courses.CourseRepository;
import com.meidanet.htmlscraper.database.courses.CourseService;
import com.meidanet.htmlscraper.database.user.Student;
import com.meidanet.htmlscraper.database.user.StudentRepository;
import com.meidanet.htmlscraper.database.user.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HtmlController {

    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Autowired
    public HtmlController(CourseRepository courseRepository, CourseService courseService,
                          StudentRepository studentRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    @PostMapping("/upload-stu-data")
    public ResponseEntity<List<String>> receiveStuDataHtml(@RequestBody HtmlSource htmlSource) {
        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> studentData = HtmlParser.extractStudentData(htmlSource.getHtmlSource());

        if (studentData.size() < 3) {
            return ResponseEntity.badRequest().body(null); // Or handle appropriately
        }

        Student student = studentService.addStudent(studentData);
        if (student != null) {
            studentRepository.save(student);
        }

        return ResponseEntity.ok(studentData);
    }

    @PostMapping("/upload-courses")
    public ResponseEntity<List<Course>> receiveHtml(@RequestBody HtmlSource htmlSource) {
        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> userID = HtmlParser.extractUserAndID(htmlSource.getHtmlSource());
        if (userID.size() < 2) {
            return ResponseEntity.badRequest().body(null); // Or handle appropriately
        }

        Student student = studentService.addStudent(userID);
        if (student != null) {
            studentRepository.save(student);
        }

        List<String> courseNames = HtmlParser.extractCourseNames(htmlSource.getHtmlSource());
        String[] courseDetails;

        List<Course> savedCourses = new ArrayList<>();
        for (String courseName : courseNames) {
            courseDetails = courseService.extractCourseIdAndName(courseName);
            Course course = courseService.addCourse(courseDetails, userID.get(0));
            if (course != null) {
                savedCourses.add(courseRepository.save(course));
            }
        }
        System.out.println("Hello World!");

        return ResponseEntity.ok(savedCourses);
    }

}
