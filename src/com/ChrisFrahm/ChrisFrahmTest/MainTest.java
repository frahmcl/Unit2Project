package com.ChrisFrahm.ChrisFrahmTest;

import com.ChrisFrahm.FileInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class MainTest {
    private FileInput dateDiff;
    String[] fields;
    Date dateOne = null;
    Date dateTwo = null;
    @Before
    public void setUp() {
        dateDiff = new FileInput("date_diff.csv");
    }
    @Test
    public void testFile() {
        //see if .replace working as it should
        String dateRow;
        String dateRow1;
        dateRow = dateDiff.fileReadLine();
        dateRow1 = dateRow.replace(".", "").trim();
        assertNotSame(dateRow, dateRow1);

        //test if split works
        if (dateRow.startsWith("\"")) {
            fields = dateRow.split("\",", 2);
        } else {
            fields = dateRow.split(",", 2);
        }
        String expectedDate = "8-May-93";
        assertEquals(expectedDate, fields[0]);

        fields[0] = fields[0].replace("\"", "").replace(".", "").trim();
        fields[1] = fields[1].replace("\"", "").replace(".", "").trim();

        //Test if reformat works
        if (fields[0].contains("-")) {
            try {
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                dateOne = new SimpleDateFormat("dd-MMM-yy"
                        , Locale.US).parse(fields[0]);
                fields[0] = targetFormat.format(dateOne);
                String expectedFormat = "May 08, 1993";
                assertEquals(expectedFormat, fields[0]);
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

        long diffDate = Math.abs(dateOne.getTime() - dateTwo.getTime());
        final long MILLISECS_IN_A_DAY = 86400000;
        long daysTotal = diffDate / (MILLISECS_IN_A_DAY);
        long yearsDiff = daysTotal / 365;
        long monthsDiff = (long) Math.floor((daysTotal % 365) / (365 / 12));
        long daysDiff = (long) Math.floor((daysTotal % 365) % (365 / 12));

        //test if totals match up
        int expectedDaysTotal = 7444;
        assertEquals(expectedDaysTotal, daysTotal);

    }



    @After
    public void tearDown(){
        dateDiff.fileClose();
    }
}
