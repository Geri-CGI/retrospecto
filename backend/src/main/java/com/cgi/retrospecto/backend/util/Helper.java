package com.cgi.retrospecto.backend.util;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class Helper {
    private static Random random = new Random();

    // TODO: make it thread safe (use AtomicInteger)
    public static int generateId(List<Integer> ids) {
        int id = generateId();
        while (ids.contains(id)) {
            id = generateId();
        }
        return id;
    }

    public static int generateId() {
        return random.nextInt(900000) + 100000;
    }

    public static boolean isOlderThanXHour(LocalDateTime fromDate, int hour) {
        return ChronoUnit.HOURS.between(fromDate, LocalDateTime.now()) > hour;
    }
}
