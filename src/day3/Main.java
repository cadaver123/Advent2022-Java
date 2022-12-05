package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

class Rucksack {
    public String compartmentA;
    public String compartmentB;

    public Character wrongItem;
    public int priority;

    @Override
    public String toString() {
        return "day3.Rucksack{" +
                "compartmentA='" + compartmentA + '\'' +
                ", compartmentB='" + compartmentB + '\'' +
                ", wrongItem=" + wrongItem +
                ", priority=" + priority +
                '}';
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        firstSolution();
        secondSolution();
    }

    private static void firstSolution() throws IOException {
        int result = Files.readAllLines(Paths.get("input3.txt"))
                .stream()
                .map((String rucksackEncoded) -> {
                    Rucksack rucksack = new Rucksack();

                    int compartmentSize = rucksackEncoded.length() / 2;
                    rucksack.compartmentA = rucksackEncoded.substring(0, compartmentSize);
                    rucksack.compartmentB = rucksackEncoded.substring(compartmentSize);

                    return rucksack;
                })
                .peek((Rucksack rucksack) -> rucksack.wrongItem = rucksack.compartmentA.chars().mapToObj(i -> (Character) (char) i)
                        .filter((Character item) -> rucksack.compartmentB.indexOf(item) >= 0)
                        .findAny().get())
                .peek((Rucksack rucksack) -> rucksack.priority = Integer.valueOf(rucksack.wrongItem) - 96 > 0 ? Integer.valueOf(rucksack.wrongItem) - 96 : Integer.valueOf(rucksack.wrongItem) - 38)
                .peek(System.out::println)
                .mapToInt((Rucksack rucksack) -> rucksack.priority)
                .sum();

        System.out.println(result);
    }

    private static void secondSolution() throws IOException {
        AtomicInteger index = new AtomicInteger(-1);
        int result = Files.readAllLines(Paths.get("input3.txt"))
                .stream()
                .peek((String line) -> index.getAndIncrement())
                .collect(groupingBy((String line) -> index.get() / 3, toList())) // group by 3
                .values()
                .stream()
                .map((List<String> lines) -> lines.get(0).chars().mapToObj(i -> (Character) (char) i)
                        .filter((Character item) -> lines.get(1).indexOf(item) >= 0 && lines.get(2).indexOf(item) >= 0)
                        .findAny().get())
                .mapToInt((Character item) -> Integer.valueOf(item) - 96 > 0 ? Integer.valueOf(item) - 96 : Integer.valueOf(item) - 38)
                .sum();


        System.out.println(result);
    }
}