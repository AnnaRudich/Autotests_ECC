package com.scalepoint.automation.utils;

import java.util.Random;

public class RandomUtils {

    public static String randomName(String name) {

        Random random = new Random();
        int randomInt = random.nextInt(999999);
        return name + Integer.toString(randomInt);
    }

    public static int randomInt() {

        return randomInt(10000000);
    }

    public static int randomInt(int upperLimit) {

        Random random = new Random();
        return random.nextInt(upperLimit);
    }

    public static int randomDelay(float min, float max){

        int random = (int)(max * Math.random() + min);

        try {

            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        return random;
    }
}

