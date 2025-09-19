package com.example.borrow.util;

import java.util.UUID;

public class Util {
    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
