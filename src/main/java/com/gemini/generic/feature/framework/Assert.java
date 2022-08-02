package com.gemini.generic.feature.framework;

public class Assert {

    /*
    equals
    contains
    in
    not equal
    not contain
     */

        private final String PASS =  "PASS";
        private final String FAIL = "FAIL";
    public Assert(){

    }

    public String assertEquals(String value1, String value2){
        return value1.equals(value2) ? PASS : FAIL;
    }

    public String assertIngnoreCaseEquals(String value1, String value2){
        return value1.equalsIgnoreCase(value2) ? PASS : FAIL;
    }
}
