package com.meidanet.system.scheduler.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Splitter {

    public static List<String> stringSplitByHyphen(String input) {
        List<String> itemsList = new ArrayList<>();
        if(input != null && !input.isEmpty()) {
            // Split the string by '-'
            String[] parts = input.split(" - ");
            if(parts.length > 2){
                if(isNumeric(parts[2])){
                    itemsList.add(parts[2]);
                    String name = parts[1] + " - " + parts[0];
                    itemsList.add(name);
                }
                else {
                    itemsList.add(parts[0]);
                    String name = parts[1] + " - " + parts[2];
                    itemsList.add(name);
                }
            }
            else{
                if(isNumeric(parts[0])){
                    itemsList.add(parts[0]);
                    itemsList.add(parts[1]);
                }
                else {
                    itemsList.add(parts[1]);
                    itemsList.add(parts[0]);
                }
            }
        }
        return itemsList;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    public static List<String> stringSplitByComma(String input) {
        List<String> itemsList = null;
        if(input != null && !input.isEmpty()){
            String[] itemsArray = input.split(",");

            // Trim whitespace and convert array to list
            itemsList = Arrays.asList(itemsArray);

            // Optionally, trim each item in the list to remove leading/trailing spaces
            itemsList.replaceAll(String::trim);
        }
        return itemsList;
    }
}
