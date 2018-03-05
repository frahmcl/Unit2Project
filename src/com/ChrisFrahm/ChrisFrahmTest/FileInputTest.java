package com.ChrisFrahm.ChrisFrahmTest;

import com.ChrisFrahm.FileInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class FileInputTest {
    private FileInput dateDiff;

    @Before
    public void setUp() {
        dateDiff = new FileInput("date_diff.csv");
    }
    @Test
    public void testFile() {

        assertNotNull("Reader should return data.  ",
                dateDiff.fileReadLine());
        dateDiff.fileReadLine();
        }


    @After
    public void tearDown(){
            dateDiff.fileClose();

    }
}
