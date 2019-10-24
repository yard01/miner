package com.github.yard01.sandbox.liveweather;

import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static junit.framework.TestCase.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println("---");

        Date d = new Date(1567088100l*1000);

        Instant i = d.toInstant();
        //i.
        //ZonedDateTime zdt = instant.atZone() ;

        System.out.println(d.toString());
        System.out.println(TimeZone.getDefault().getRawOffset());
        System.out.println(i);
        System.out.println();

        Date d2 = new Date(1567069200l*1000 - TimeZone.getDefault().getRawOffset());
        System.out.println(d2.toString());
        System.out.println();

        //ZoneOffset.of("UTC");
        //LocalDateTime ldt = LocalDateTime. (d);
        Date d3 = new Date(1567069200l*1000);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(1567069200l*1000);// .setTime(d3);


        c.setTimeZone(TimeZone.getTimeZone("UTC"));

        System.out.println(TimeZone.getDefault().getOffset(System.currentTimeMillis()));

        System.out.println("tz " +TimeZone.getTimeZone("UTC").getOffset(System.currentTimeMillis()));

        System.out.println(c.getTime());

        System.out.println("***");

        Integer offset  = ZonedDateTime.now().getOffset().getTotalSeconds();

        System.out.println("***");

        Date theEnd = new Date(1567069200l*1000);
        TimeZone tz = TimeZone.getDefault();
        //tz.
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            System.out.println("::" +dateFormat.parse("1567069200000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String text = dateFormat.format(theEnd);

        System.out.println(dateFormat.getCalendar().getTime());

        System.out.println(text);

        assertEquals(4, 2 + 2);

    }
}