package com.meidanet.htmlscraper.controller.html.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class StuDataHtmlParser {

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
