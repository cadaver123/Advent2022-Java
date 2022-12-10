package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(getIndex(4));
            System.out.println(getIndex(14));
    }

    private static int getIndex(int differentCharCount) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input6.txt"));
        int repeatedCharIndex = 0;
        int index = 0;
        String input = lines.get(0);

        outer:
        for (Character letter: input.chars().mapToObj(i -> (Character) (char) i).toList()) {
            String substring = input.substring(repeatedCharIndex, index);

            if (substring.length() == differentCharCount) {
                break;
            }

            if (substring.indexOf(letter) > -1) {
                repeatedCharIndex = substring.indexOf(letter) + repeatedCharIndex + 1;
            }

            index++;
        }
        return index;
    }
}