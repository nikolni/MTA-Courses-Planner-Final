package com.meidanet.htmlscraper.controller.html.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CoursesConditionsParser {

    public static List<String> extractConditions(String html){
        // Parse the HTML
        Document doc = Jsoup.parse(html);
        List<String> courseConditions = new ArrayList<>();

// Extract course number and name
        String courseInfo = doc.select("div.Table.container.fcontainer strong").text();
        String[] courseParts = courseInfo.split(" ", 2);
        String courseNumber = courseParts[0];
        String courseName = courseParts[1];
        // Remove unwanted parts from the course name
        if (courseName.contains("תאריך בחינה")) {
            courseName = courseName.split("תאריך בחינה")[0].trim();
        }
        String courseDetails = courseName + " - " + courseNumber;
        courseConditions.add(courseDetails);

        // Extract conditions
        Elements rows = doc.select("div.Table.container.ncontainer.WithSearch .row");
        StringBuilder prerequisiteBuilder = new StringBuilder();
        StringBuilder parallelBuilder = new StringBuilder();
        StringBuilder exclusiveBuilder = new StringBuilder();

        for (Element row : rows) {
            String conditionType = extractTextAfterLabel(row.select(".col").get(0));
            String conditionCourse = extractTextAfterLabel(row.select(".col").get(2));

            if (conditionType.contains("תנאי קדם")) {
                if (prerequisiteBuilder.length() > 0) {
                    prerequisiteBuilder.append(", ");
                }
                prerequisiteBuilder.append(conditionCourse);
            }
            else if (conditionType.contains("תנאי מקביל")) {
                if (parallelBuilder.length() > 0) {
                    parallelBuilder.append(", ");
                }
                parallelBuilder.append(conditionCourse);
            }
            else if (conditionType.contains("תנאי אקסקלוסיבי")) {
                if (exclusiveBuilder.length() > 0) {
                    exclusiveBuilder.append(", ");
                }
                exclusiveBuilder.append(conditionCourse);
            }
        }

        courseConditions.add(prerequisiteBuilder.toString());
        courseConditions.add(parallelBuilder.toString());
        courseConditions.add(exclusiveBuilder.toString());

        return courseConditions;
    }

    private static String extractTextAfterLabel(Element col) {
        // Extract text and replace non-breaking spaces with regular spaces
        String text = col.text();

        // Remove any leading label-like text and non-breaking spaces
        text = text.replace("\u00A0", " ").trim();

        text = text.replaceAll("^[^:]*:\\s*", "");

        // Remove any remaining non-breaking spaces or leading/trailing spaces
        return text.trim();
    }

}
