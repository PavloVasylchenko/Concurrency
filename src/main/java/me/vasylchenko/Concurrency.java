package me.vasylchenko;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lets assume that we need to translate big file
 * Translate API allow us to have 4 concurrent calls.
 * We need to translate file as fast as we can and service must work with 10M Xmx with provided file.
 */
public class Concurrency {

    private static final int THREADS = 4;

    private static final ThreadLocal<Integer> delay = ThreadLocal.withInitial(() -> new Random().nextInt(1000));

    static AtomicInteger statsValue = new AtomicInteger();
    static AtomicInteger linesValue = new AtomicInteger();

    public static void main(String[] args) throws IOException, InterruptedException {
        long l = System.currentTimeMillis();
        implementMe();
        System.out.println("Time: " + (System.currentTimeMillis() - l));
        System.out.println("Expected: " + (statsValue.get() / THREADS));
    }

    /**
     * This method need to be implemented.
     * All lines need to be processed as fast as possible without changing other methods
     * Service should consume not more then 10M Xmx memory with provided file.
     * Current implementation works as expected except memory consumption
     * @throws IOException
     * @throws InterruptedException
     */
    private static void implementMe() throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        Files.lines(Path.of("file.txt")).forEach(line -> {
            linesValue.incrementAndGet();
            System.out.println(line.substring(0, 10) + " submitted");
            executorService.submit(() -> {
                System.out.println(translate(line.substring(0, 10)));
            });
        });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

    /**
     * This method should not be changed. It just emulated api call.
     *
     * @param input string that need to be translated
     * @return translated string
     */
    private static String translate(String input) {
        try {
            Integer millis = delay.get();
            statsValue.addAndGet(millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input + " translated";
    }
}
