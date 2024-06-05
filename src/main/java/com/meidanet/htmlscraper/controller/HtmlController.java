package com.meidanet.htmlscraper.controller;

import com.meidanet.htmlscraper.database.courses.Course;
import com.meidanet.htmlscraper.database.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HtmlController {


    @Autowired
    private CourseRepository coursesRepository;

    @PostMapping("/upload")
    public List<Course> receiveHtml(@RequestBody HtmlSource htmlSource) {
        List<String> courseNames = HtmlParser.extractCourseNames(htmlSource.getHtmlSource());

        List<Course> savedCourses = new ArrayList<>();
        for (String courseName : courseNames) {
            Course course = new Course();
            course.setCourseName(courseName);
            savedCourses.add(coursesRepository.save(course));
        }
        return savedCourses;
    }
}

class HtmlSource {
    private String htmlSource;

    public String getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }
}