package com.example.bespinaf.a2d2;

import junit.runner.Version;

import org.junit.Test;
import org.junit.Rule;

import static org.junit.Assert.*;

public class RequestRideAsUserTest {
    @Test

    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getVersion(){
        System.out.println(Version.id());
    }
}