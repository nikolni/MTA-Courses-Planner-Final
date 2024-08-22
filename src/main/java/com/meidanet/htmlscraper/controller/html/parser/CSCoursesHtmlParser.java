package com.meidanet.htmlscraper.controller.html.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CSCoursesHtmlParser {
    public static List<List<List<String>>> extractCourseDetails(String html) {

        Document document = Jsoup.parse(html);

        // The main list
        List<List<List<String>>> mainList = new ArrayList<>();

        // Get all "פרטים נוספים" buttons
        Elements detailButtons = document.select("input[value=פרטים נוספים]");

        // Iterate through each "פרטים נוספים" section
        for (Element button : detailButtons) {
            List<List<String>> secondaryList = new ArrayList<>();

            // Get course information
            //String groupNumber = button.previousElementSibling().text().split(":")[1].trim();

            String groupNumber = extractTextAfterLabel(button.previousElementSibling());
            String lessonType = button.parent().text().contains("קורס מסוג תרגיל") ? "תרגיל" : "שיעור";


            // Get the parent div containing the rows
            Element rawsDiv = button.parent().nextElementSibling().nextElementSibling();

            // Get all rows within the timetable
            Elements rows = rawsDiv.select(".row");

            // Skip the header row
            for (int i = 1; i < rows.size(); i++) {
                List<String> rowList = new ArrayList<>();
                //rowList.add(courseName); //0
                rowList.add(groupNumber);  //1
                rowList.add(lessonType);  //2

                Elements cols = rows.get(i).select(".col");
                rowList.add(extractTextAfterLabel(cols.get(0))); // semester
                rowList.add(extractTextAfterLabel(cols.get(1))); // day
                rowList.add(extractTextAfterLabel(cols.get(2))); // start time
                rowList.add(extractTextAfterLabel(cols.get(3))); // end time
                rowList.add(extractTextAfterLabel(cols.get(4))); // lecturer name

                secondaryList.add(rowList);
            }

            mainList.add(secondaryList);
        }

        return mainList;
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
