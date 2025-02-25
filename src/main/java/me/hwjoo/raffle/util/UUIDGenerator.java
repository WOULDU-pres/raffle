package me.hwjoo.raffle.util;

import java.util.UUID;

public class UUIDGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
} 