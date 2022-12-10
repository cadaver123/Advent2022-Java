package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input4.txt"));

        System.out.println(lines.stream()
                .filter(line -> {
                    String[] rangesString = line.split(",");
                    List<Integer> firstRange = parseRange(rangesString, 0);
                    List<Integer> secondRange = parseRange(rangesString, 1);

                    return (firstRange.get(0) <= secondRange.get(0) && secondRange.get(1) <= firstRange.get(1)) ||
                            ((secondRange.get(0) <= firstRange.get(0) && firstRange.get(1) <= secondRange.get(1)));
                }).count());

        System.out.println(lines.stream()
                .filter(line -> {
                    String[] rangesString = line.split(",");
                    List<Integer> firstRange = parseRange(rangesString, 0);
                    List<Integer> secondRange = parseRange(rangesString, 1);

                    return Math.max(firstRange.get(0), secondRange.get(0)) <= Math.min(firstRange.get(1), secondRange.get(1));
                }).count());
    }

    private static List<Integer> parseRange(String[] rangesString, int index) {
        List<Integer> firstRange = Arrays.stream(rangesString[index].split("-"))
                .map(Integer::parseInt).toList();
        return firstRange;
    }
}
