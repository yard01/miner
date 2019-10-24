package com.github.yard01.sandbox.weatherforecast;

import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;

import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {


        Date d = new Date(1567069200);
        System.out.println(d.toString());
        Date d1 = new Date();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date d2 = new Date();

        LiveweatherTools.dateSubtractionInMinutes(d1, d2);

        int s = 128;
        s = s | -1;

        System.out.println("s = " +s);
        Random r = new Random();
        System.out.println(r.nextInt(2));
        System.out.println(r.nextInt(2));
        System.out.println(r.nextInt(2));
        System.out.println(r.nextInt(2));
        System.out.println(r.nextInt(2));

        System.out.println(~(127-1));

        assertEquals(4, 2 + 2);
    }
}