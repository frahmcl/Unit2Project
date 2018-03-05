package com.ChrisFrahm;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;


/**
 * This program takes a .csv file of dates and calculates the difference between the two.
 * Then the number of instances per month is collected and displayed by number of times
 * the date is found in a given month
 * @author Chris Frahm
 *
 */


public class Main {
    //date_diff.csv is the file being used.  Dates are delimited by ','
    private final static FileInput dateDiff = new FileInput("date_diff.csv");


    public static void main(String[] args) {
        //variables for total number of occurrences
        String[] months = new String[2];
        Object monthFound;
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();

        //variables for date difference
        //String to hold one line of .csv file
        String dateRow;
        //fields for the dates
        String[] fields;
        //variable to hold dates when dateRow gets split
        Date dateOne = null;
        Date dateTwo = null;

        //header for date difference from .csv file
        System.out.format("%-15s %6s %5s %5s\n", "Total Days", "Years,", "Months,", "Days");
        System.out.format("%-15s %6s %5s %5s\n", "========", "======", "======", "======");

        //read through the file date_diff.csv as long as there is a next line
        while ((dateRow = dateDiff.fileReadLine()) != null) {

            //replace periods with nothing and get rid of extra whitespace
            dateRow = dateRow.replace(".", "").trim();

            //split line and limit number of splits to two
            if (dateRow.startsWith("\"")) {
                fields = dateRow.split("\",", 2);
            } else {
                fields = dateRow.split(",", 2);
            }

            //once the fields have been split into two date remove quotes.  Leaving quotes in makes dates harder to parse
            fields[0] = fields[0].replace("\"", "");
            fields[1] = fields[1].replace("\"", "");

           /*
           parse the split fields  from string into date object. Force uniform formatting to "MMM dd, yyyy"
            forcing the formatting makes it easier to parse out the Months for the collection of
           number of instances
           */
            if (fields[0].contains("-")) {
                try {
                    DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    dateOne = new SimpleDateFormat("dd-MMM-yy"
                            , Locale.US).parse(fields[0]);
                    fields[0] = targetFormat.format(dateOne);
                } catch (ParseException e) {
                    System.out.println("Exception: " + e);
                }
            } else {
                try {
                    DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    dateOne = new SimpleDateFormat("MMM dd, yyyy"
                            , Locale.US).parse(fields[0]);
                    fields[0] = targetFormat.format(dateOne);
                } catch (ParseException e) {
                    System.out.println("Exception: " + e);
                }
            }
            if (fields[1].contains("-")) {
                try {
                    DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    dateTwo = new SimpleDateFormat("dd-MMM-yy"
                            , Locale.US).parse(fields[1]);
                    fields[1] = targetFormat.format(dateTwo);
                } catch (ParseException e) {
                    System.out.println("Exception: " + e);
                }
            } else {
                try {
                    DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    dateTwo = new SimpleDateFormat("MMM d, yyyy"
                            , Locale.US).parse(fields[1]);
                    fields[1] = targetFormat.format(dateTwo);
                } catch (ParseException e) {
                    System.out.println("Exception: " + e);
                }
            }
            /**
             * Take absolute value of dateOne minus dateTwo.  Returns answer in Milliseconds
             * Total Millisecs in a day = (1000 * 60 * 60 * 24)
             * get results for difference of years, months and days
             */
            long diffDate = Math.abs(dateOne.getTime() - dateTwo.getTime());
            final long MILLISECS_IN_A_DAY = 86400000;
            long daysTotal = diffDate / (MILLISECS_IN_A_DAY);
            long yearsDiff = daysTotal / 365;
            long monthsDiff = (long) Math.floor((daysTotal % 365) / (365 / 12));
            long daysDiff = (long) Math.floor((daysTotal % 365) % (365 / 12));

            //output calculations
            System.out.format("%-15s %6s %5s %5s\n", daysTotal, yearsDiff, monthsDiff, daysDiff);


            /**
             * Start of collections work.  Get substring of month to pass to TreeMap as the value
             */
            months[0] = fields[0].substring(0, 3);
            months[1] = fields[1].substring(0, 3);

            /*Alternate method if no forced formatting
            months[0] = new SimpleDateFormat("MMM").format(dateOne);
            months[1] = new SimpleDateFormat("MMM").format(dateTwo);
            */

            //for each string in the months array see if it is already a variable.  If so, add 1 to total, if not create new entry
            for (String s : months) {
                monthFound = map.get(s);
                if (monthFound == null) {
                    map.put(s, new Integer(1));
                } else {
                    map.put(s, map.get(s) + 1);
                }
            }

        }
        //close file
        dateDiff.fileClose();
        //output header of instances outside of while loop
        System.out.println("\nInstances of date entries per month:");
        System.out.format("%5s %11s\n", "Month", "Instances");
        System.out.format("%5s %11s\n", "=====", "=========");

        //for every entry in new map display the name and value
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.format("%-5s %11d\n", entry.getKey(), entry.getValue());
        }

    }


}



