package com.meidanet.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meidanet.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.database.student.courses.CourseService;
import com.meidanet.system.controller.form.parser.FormParser;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.scheduler.Scheduler;
import com.meidanet.system.scheduler.answer.FinalSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/preferences-form")
public class PreferencesFormController {

    private final CourseService courseService;
    private final RequiredCoursesService requiredCoursesService;
    private final ChoiceCoursesService choiceCoursesService;
    private final CoursesConditionsService csCoursesConditionsService;

    @Autowired
    public PreferencesFormController(CourseService courseService, RequiredCoursesService requiredCoursesService,
                           ChoiceCoursesService choiceCoursesService, CoursesConditionsService csCoursesConditionsService)
    {
        this.courseService = courseService;
        this.requiredCoursesService = requiredCoursesService;
        this.choiceCoursesService = choiceCoursesService;
        this.csCoursesConditionsService = csCoursesConditionsService;

    }

    //test
    @GetMapping("/welcome")
    public String welcome() {
        return "your rest endpoint works";
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://mta-courses-planner-f96c9.web.app"}, allowCredentials = "true")
   // @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/upload-preferences-form")
    public ResponseEntity<FinalSystem> receiveForm(@RequestBody String json) throws JsonProcessingException {
        FinalSystem finalSystem = new FinalSystem();
        try {
            PreferencesForm preferencesForm = FormParser.extractStudentRequests(json);
            Scheduler scheduler = new Scheduler(courseService, requiredCoursesService, choiceCoursesService, csCoursesConditionsService);

            scheduler.getSchedule(preferencesForm, finalSystem);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Create ObjectWriter with pretty printing
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        // Convert the object to JSON with pretty printing
        String prettyJsonString = writer.writeValueAsString(finalSystem);
        // Print the pretty JSON string
        System.out.println(prettyJsonString);

        return ResponseEntity.ok(finalSystem);
    }

}