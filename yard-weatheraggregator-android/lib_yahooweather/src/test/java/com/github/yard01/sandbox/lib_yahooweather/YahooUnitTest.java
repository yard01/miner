package com.github.yard01.sandbox.lib_yahooweather;

import android.net.Uri;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class YahooUnitTest {
    @Test
    public void addition_isCorrect() {
        String a = "asasas %f & %f&ss=%s";
        System.out.println(String.format(a, 111f, 222f,"ddddd"));
        System.out.println(Float.toString(11.56f));

        assertEquals(4, 2 + 2);
    }
}