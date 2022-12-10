package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get("input1.txt"));
        List<Integer> elves = new ArrayList<>();
        AtomicInteger calories = new AtomicInteger(0);

        allLines.stream().forEach((String line) -> {
            if ("".equals(line)) {
                elves.add(calories.intValue());
                calories.getAndSet(0);
            } else {
                calories.getAndAdd(Integer.parseInt(line));
            }
        });
        elves.add(calories.intValue());

        System.out.println(Collections.max(elves));
        System.out.println(elves.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToInt(elf -> elf).sum());
    }
}
