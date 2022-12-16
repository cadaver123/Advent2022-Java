package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern ADDX_COMPILED_PATTER = Pattern.compile("addx (-?\\d+)");

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input10.txt"));
        int position = 0;
        int cycle = 0;
        int signalStrength = 0;
        List<Character> pixels = new ArrayList<>();

        for (var line : lines) {
            var addxMatcher = ADDX_COMPILED_PATTER.matcher(line);
            int innerCycles = 0;

            while (true) {
                if ("noop".equals(line)) {
                    drawPixel(position, cycle, pixels);
                    cycle++;
                    signalStrength = checkSignal(position + 1, cycle, signalStrength);
                    break;

                } else if (addxMatcher.matches()) {
                    drawPixel(position, cycle, pixels);
                    innerCycles++;
                    cycle++;
                    signalStrength = checkSignal(position + 1, cycle, signalStrength);

                    if (innerCycles == 2) {
                        position += Integer.parseInt(addxMatcher.group(1));
                        break;
                    }
                }
            }
        }

        System.out.println(signalStrength);

        var pixelIndex = 1;
        for (var pixel : pixels) {
            System.out.print(pixel);
            if (pixelIndex % 40 == 0) {
                System.out.print("\n");
            }
            pixelIndex++;
            if (pixelIndex > 240) {
                break;
            }
        }
    }

    private static void drawPixel(int position, int cycle, List<Character> pixels) {
        if (position <= cycle % 40 && cycle % 40 < position + 3) {
            pixels.add('#');
        } else {
            pixels.add('.');
        }
    }

    private static int checkSignal(int x, int cycles, int signalStrength) {
        if ((cycles + 20) % 40 == 0 && cycles <= 220) {
            signalStrength += x * cycles;
        }
        return signalStrength;
    }
}
