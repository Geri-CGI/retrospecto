package com.cgi.retrospecto.backend.util;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Helper {
    private static Random random = new Random();
    private static AtomicInteger currentId = new AtomicInteger(random.nextInt(900000) + 100000);

    public static int generateId() {
        return currentId.getAndAdd(random.nextInt(900) + 100);
    }

    public static boolean isOlderThanXHour(LocalDateTime fromDate, int hour) {
        return ChronoUnit.HOURS.between(fromDate, LocalDateTime.now()) > hour;
    }
}
