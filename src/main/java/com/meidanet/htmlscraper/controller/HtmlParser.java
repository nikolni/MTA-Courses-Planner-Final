package com.meidanet.htmlscraper.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public static List<String> extractUserAndID(String html){
        // Parse the HTML
        Document doc = Jsoup.parse(html);
        List<String> userID = new ArrayList<>();

        // Extract the script tag
        Elements scriptElements = doc.getElementsByTag("script");
        for (Element script : scriptElements) {
            String scriptContent = script.data(); // Get the script content
            // Check if the script content contains both EMAIL and ID
            if (scriptContent.contains("EMAIL") && scriptContent.contains("ID")) {
                userID.add(extractValue(scriptContent, "ID"));
                userID.add((extractValue(scriptContent, "EMAIL")).split("@")[0]);
            }
        }
        return userID;
    }

    private static String extractValue(String script, String key) {
        String prefix = "name=\"" + key + "\" value=\"";
        int startIndex = script.indexOf(prefix);
        if (startIndex != -1) {
            startIndex += prefix.length();
            int endIndex = script.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return script.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    public static List<String> extractCourseNames(String html) {
        List<String> courseNames = new ArrayList<>();
        Document document = Jsoup.parse(html);

        Elements courseElements = document.select(".pagetitle.InRange");

        for (Element element : courseElements) {
            String courseName = element.text().trim();
            courseNames.add(courseName);
        }


        return courseNames;
    }



    public static List<String> extractStudentData(String html) {
        List<String> studentData = new ArrayList<>();
        Document document = Jsoup.parse(html);

        // Extract specific elements
        String firstNameEnglish = "";
        String lastNameEnglish = "";
        String idNumber = "";

        Elements strongElements = document.select("div.col-md-12 strong");

        for (Element strongElement : strongElements) {
            String text = strongElement.text().trim();
            if (text.matches("[A-Za-z]+")) {
                if (lastNameEnglish.isEmpty()) {
                    lastNameEnglish = text;
                } else if (firstNameEnglish.isEmpty()) {
                    firstNameEnglish = text;
                }
            } else if (text.matches("\\d+")) {
                idNumber = text;
            }
        }

        if (!firstNameEnglish.isEmpty()) {
            studentData.add("First Name: " + firstNameEnglish);
        }
        if (!lastNameEnglish.isEmpty()) {
            studentData.add("Last Name: " + lastNameEnglish);
        }
        if (!idNumber.isEmpty()) {
            studentData.add("ID Number: " + idNumber);
        }

        return studentData;
    }
}
