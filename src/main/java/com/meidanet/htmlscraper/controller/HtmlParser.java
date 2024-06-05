package com.meidanet.htmlscraper.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

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
