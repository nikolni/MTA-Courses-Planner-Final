package com.meidanet.system.scheduler.helper;

import java.util.Arrays;
import java.util.List;

public class Splitter {

    public static List<String> stringSplitByHyphen(String input) {
        // Split the string by '-'
        String[] parts = input.split(" - ");

        // Ensure that the split was successful
        if (parts.length == 2) {
            String id = parts[0].trim(); // First part
            String name = parts[1].trim(); // Second part
        }

        // Trim whitespace and convert array to list
        List<String> itemsList = Arrays.asList(parts);

        // Optionally, trim each item in the list to remove leading/trailing spaces
        itemsList.replaceAll(String::trim);

        return itemsList;
    }

    public static List<String> stringSplitByComma(String input) {
        String[] itemsArray = input.split(",");

        // Trim whitespace and convert array to list
        List<String> itemsList = Arrays.asList(itemsArray);

        // Optionally, trim each item in the list to remove leading/trailing spaces
        itemsList.replaceAll(String::trim);

        return itemsList;
    }

    public static String removeLessonFromCourseID(String course_id_name){
        // Remove the last two digits
        return course_id_name.substring(0, course_id_name.length() - 2);
    }
}
