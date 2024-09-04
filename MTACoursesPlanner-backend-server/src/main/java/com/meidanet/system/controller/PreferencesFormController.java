package com.meidanet.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meidanet.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.database.student.courses.CourseService;
import com.meidanet.database.student.data.StudentService;
import com.meidanet.system.controller.form.parser.FormParser;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.scheduler.Scheduler;
import com.meidanet.system.scheduler.answer.FinalSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/preferences-form")
public class PreferencesFormController {

    private final CourseService courseService;
    private final RequiredCoursesService requiredCoursesService;
    private final ChoiceCoursesService choiceCoursesService;
    private final CoursesConditionsService csCoursesConditionsService;
    private final StudentService studentService;

    @Autowired
    public PreferencesFormController(CourseService courseService, RequiredCoursesService requiredCoursesService,
                                     ChoiceCoursesService choiceCoursesService, CoursesConditionsService csCoursesConditionsService, StudentService studentService)
    {
        this.courseService = courseService;
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesService = choiceCoursesService;
        this.csCoursesConditionsService = csCoursesConditionsService;
        this.studentService = studentService;
    }

    //test
    @GetMapping("/welcome")
    public String welcome() {
        return "your rest endpoint works";
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://mta-courses-planner-f96c9.web.app"}, allowCredentials = "true")
   // @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/upload-preferences-form")
    public ResponseEntity<Object> receiveForm(@RequestBody String json) throws JsonProcessingException {
        FinalSystem finalSystem = new FinalSystem();
        boolean isValidRequest;
        try {
            PreferencesForm preferencesForm = FormParser.extractStudentRequests(json);
            Scheduler scheduler = new Scheduler(studentService, courseService, requiredCoursesService, choiceCoursesService, csCoursesConditionsService);

            isValidRequest = scheduler.getSchedule(preferencesForm, finalSystem);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        // Create ObjectWriter with pretty printing
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        String prettyJsonString;

        if(isValidRequest) {
            // Convert the object to JSON with pretty printing
             prettyJsonString = writer.writeValueAsString(finalSystem);
        }
        else{
            Map<String, String> response = new HashMap<>();
            response.put("message", "The student ID does not exist in the system. Try again");
            prettyJsonString = writer.writeValueAsString(response);
        }
        System.out.println(prettyJsonString);
        return ResponseEntity.ok(prettyJsonString);
    }

}
