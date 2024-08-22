package com.meidanet.system.controller;


import com.meidanet.htmlscraper.database.computer.science.course.choice.ChoiceCoursesService;
import com.meidanet.htmlscraper.database.computer.science.course.condition.CoursesConditionsService;
import com.meidanet.htmlscraper.database.computer.science.course.required.RequiredCoursesService;
import com.meidanet.htmlscraper.database.courses.CourseService;
import com.meidanet.system.controller.form.parser.FormParser;
import com.meidanet.system.preference.form.PreferencesForm;
import com.meidanet.system.scheduler.Scheduler;
import com.meidanet.system.scheduler.answer.FinalSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
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


    @PostMapping("/upload-preferences-form")
    public ResponseEntity<FinalSystem> receiveForm(@RequestBody String json) {
        FinalSystem finalSystem = new FinalSystem();
        try {
            PreferencesForm preferencesForm = FormParser.extractStudentRequests(json);
            Scheduler scheduler = new Scheduler(courseService,
                     requiredCoursesService,  choiceCoursesService, csCoursesConditionsService);

            scheduler.getSchedule(preferencesForm, finalSystem);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(finalSystem);
    }

}