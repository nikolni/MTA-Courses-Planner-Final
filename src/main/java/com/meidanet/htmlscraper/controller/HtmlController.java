package com.meidanet.htmlscraper.controller;

import com.meidanet.database.computer.science.course.choice.ChoiceCoursesRepository;
import com.meidanet.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.database.computer.science.course.condition.CSCoursesConditions;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsRepository;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.database.computer.science.course.required.RequiredCoursesRepository;
import com.meidanet.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.database.student.courses.Course;
import com.meidanet.database.student.courses.CourseRepository;
import com.meidanet.database.student.courses.CourseService;
import com.meidanet.database.student.data.Student;
import com.meidanet.database.student.data.StudentRepository;
import com.meidanet.database.student.data.StudentService;
import com.meidanet.htmlscraper.html.parser.CoursesConditionsParser;
import com.meidanet.htmlscraper.html.parser.StuCoursesHtmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/html")
public class HtmlController {

    private final CourseRepository courseRepository;
    private CourseService courseService;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final RequiredCoursesRepository requiredCoursesRepository;
    private final RequiredCoursesService requiredCoursesService;
    private final ChoiceCoursesRepository choiceCoursesRepository;
    private final ChoiceCoursesService choiceCoursesService;
    private final CoursesConditionsRepository csCoursesConditionsRepository;
    private final CoursesConditionsService csCoursesConditionsService;

@Autowired
    public HtmlController(CourseRepository courseRepository, CourseService courseService,
                          StudentRepository studentRepository, StudentService studentService,
                          RequiredCoursesRepository requiredCoursesRepository, RequiredCoursesService requiredCoursesService,
                          ChoiceCoursesRepository choiceCoursesRepository, ChoiceCoursesService choiceCoursesService,
                          CoursesConditionsRepository csCoursesConditionsRepository, CoursesConditionsService csCoursesConditionsService)
{
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.requiredCoursesRepository = requiredCoursesRepository;
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesRepository = choiceCoursesRepository;
        this.choiceCoursesService = choiceCoursesService;
        this.csCoursesConditionsRepository = csCoursesConditionsRepository;
        this.csCoursesConditionsService = csCoursesConditionsService;

}
    //test
    @GetMapping("/welcome")
    public String welcome() {
        return "your rest endpoint works";
    }

    @CrossOrigin(origins = {"https://rishum.mta.ac.il"}, allowCredentials = "true")
    @PostMapping("/is-student-registered")
    public ResponseEntity<Boolean> isStudentExist(@RequestBody HtmlSource htmlSource) {
        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        String studentID = StuCoursesHtmlParser.extractID(htmlSource.getHtmlSource());
        String studentName = StuCoursesHtmlParser.extractName(htmlSource.getHtmlSource());

        boolean isStudentRegistered = false;
        if(studentID != null && studentName != null) {
            isStudentRegistered = studentService.isStudentRegistered(studentID);

            if(!isStudentRegistered){
                List<String> userID = new ArrayList<>();
                userID.add(studentID);
                userID.add(studentName);

                Student student = studentService.addStudent(userID);
                if (student != null)
                    studentRepository.save(student);

            }
        }

        System.out.println(isStudentRegistered);
        return ResponseEntity.ok(isStudentRegistered);
    }



    @CrossOrigin(origins = {"https://rishum.mta.ac.il"}, allowCredentials = "true")
    @PostMapping("/upload-courses-for-all-years")
    public ResponseEntity<List<Course>> receiveAllCoursesHtml(@RequestBody HtmlSource htmlSource) {
        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> userID = StuCoursesHtmlParser.extractUserAndID(htmlSource.getHtmlSource());
        if (userID.size() < 2) {
            return ResponseEntity.badRequest().body(null); // Or handle appropriately
        }

//        Student student = studentService.addStudent(userID);
//        if (student != null)
//            studentRepository.save(student);
//

        List<String> courseNames = StuCoursesHtmlParser.extractCourseNames(htmlSource.getHtmlSource());
        String[] courseDetails;

        List<Course> savedCourses = new ArrayList<>();
        for (String courseName : courseNames) {
            courseDetails = courseService.extractCourseIdAndName(courseName);
            Course course = courseService.addCourse(courseDetails, userID.get(0));
            if (course != null) {
                savedCourses.add(courseRepository.save(course));
            }
        }

        return ResponseEntity.ok(savedCourses);
    }

// for required courses
//    @PostMapping("/upload-course-details")
//    public ResponseEntity<String> receiveHtmlForCSCourses(@RequestBody HtmlSource htmlSource) {
//        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        List<List<List<String>>> courseDetails = CSCoursesHtmlParser.extractCourseDetails(htmlSource.getHtmlSource());
//        if (courseDetails.isEmpty()) {
//            return ResponseEntity.badRequest().body(null); // Or handle appropriately
//        }
//
//        for (List<List<String>> lessonsList : courseDetails) {
//            for (int i = 0; i < lessonsList.size(); i++) {
//                CSCoursesRequired csCoursesRequired =  new CSCoursesRequired();
//                csCoursesRequired = requiredCoursesService.addRequiredCourse(lessonsList.get(i));
//                if (csCoursesRequired != null) {
//                    requiredCoursesRepository.save(csCoursesRequired);
//                }
//            }
//        }
//
//        return ResponseEntity.ok("Successfully loaded CS courses");
//    }


// for chosen courses
//    @PostMapping("/upload-course-details")
//    public ResponseEntity<String> receiveHtmlForCSCourses(@RequestBody HtmlSource htmlSource) {
//        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        List<List<List<String>>> courseDetails = CSCoursesHtmlParser.extractCourseDetails(htmlSource.getHtmlSource());
//        if (courseDetails.isEmpty()) {
//            return ResponseEntity.badRequest().body(null); // Or handle appropriately
//        }
//
//        for (List<List<String>> lessonsList : courseDetails) {
//            for (int i = 0; i < lessonsList.size(); i++) {
//                CSCoursesChoice csCoursesChoice =  choiceCoursesService.addChoiceCourse(lessonsList.get(i));
//                if (csCoursesChoice != null) {
//                    choiceCoursesRepository.save(csCoursesChoice);
//                }
//            }
//        }
//
//        return ResponseEntity.ok("Successfully loaded CS courses");
//    }

    @CrossOrigin(origins = {"https://rishum.mta.ac.il"}, allowCredentials = "true")
    @PostMapping("/upload-course-conditions")
    public ResponseEntity<String> receiveHtmlForCSCoursesConditions(@RequestBody HtmlSource htmlSource) {
        if (htmlSource == null || htmlSource.getHtmlSource() == null || htmlSource.getHtmlSource().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> courseConditions = CoursesConditionsParser.extractConditions(htmlSource.getHtmlSource());
        if (courseConditions.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Or handle appropriately
        }

        CSCoursesConditions coursesConditions = csCoursesConditionsService.addCourseCondition(courseConditions);
        if (coursesConditions != null) {
            csCoursesConditionsRepository.save(coursesConditions);
        }

        return ResponseEntity.ok("Successfully loaded course conditions");
    }
}
