package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get("input2.txt"));

        System.out.println(allLines.stream()
                .mapToInt(line ->
                        firstScore(Character.getNumericValue(line.charAt(0)) - 10,
                                Character.getNumericValue(line.charAt(2)) - 33))
                .sum());

        System.out.println(allLines.stream()
                .mapToInt(line ->
                        secondScore(Character.getNumericValue(line.charAt(0)) - 10,
                                Character.getNumericValue(line.charAt(2)) - 33))
                .sum());
    }

    private static int firstScore(int elf, int hero) {
        int score = hero + 1;

        return switch (hero - elf) {
            case 2, -1 -> score;
            case 1, -2 -> score + 6;
            case 0 -> score + 3;
            default -> -1000;
        };
    }

    private static int secondScore(int elf, int hero) {
        int score = hero * 3;

        return switch (hero) {
            case 0 -> score + (elf + 2) % 3 + 1;
            case 1 -> score + elf + 1;
            case 2 -> score + (elf + 1) % 3 + 1;
            default -> -1000;
        };
    }
}