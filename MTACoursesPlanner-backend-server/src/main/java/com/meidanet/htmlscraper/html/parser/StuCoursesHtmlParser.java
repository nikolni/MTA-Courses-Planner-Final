package com.meidanet.htmlscraper.html.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StuCoursesHtmlParser {

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

    public static String extractID(String html) {
        // Parse the HTML using JSoup
        Document document = Jsoup.parse(html);

        // Select all script elements
        Elements scriptElements = document.getElementsByTag("script");

        // Define a regex pattern to match the ID value in the script content
        Pattern pattern = Pattern.compile("ID=(\\d+)");

        // Iterate over script elements to find the ID
        for (Element element : scriptElements) {
            String scriptContent = element.html();
            Matcher matcher = pattern.matcher(scriptContent);
            if (matcher.find()) {
                return matcher.group(1); // Return the first captured group which is the ID
            }
        }

        // Return null or throw an exception if ID is not found
        return null;
    }

    public static String extractName(String html) {
        // Parse the HTML using JSoup
        Document document = Jsoup.parse(html);

        // Select the elements that match the <strong> tag within the <h3> tag with class 'protext'
        Elements nameElements = document.select("h3.protext strong");

        // Return the text content of the first matching element if found
        return nameElements.size() > 0 ? nameElements.get(0).text() : null;
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

}
