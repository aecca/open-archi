package com.araguacaima.open_archi.persistence.commons;

import java.util.Random;

public class Utils {

    public String randomHexColor() {

        // create random object - reuse this as often as possible
        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(256 * 256 * 256);

        // format it as hexadecimal string (with hashtag and leading zeros)

        return (String.format("#%06x", nextInt));
    }

}