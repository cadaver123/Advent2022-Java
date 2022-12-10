package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input5.txt"));
        List<Stack<String>> partOneStack = new ArrayList<>();
        List<Stack<String>> partTwoStack = new ArrayList<>();

        IntStream.range(0, 9).forEach(e -> {
            partOneStack.add(new Stack<String>());
            partTwoStack.add(new Stack<String>());
        });

        parseStack(lines, partOneStack);
        parseStack(lines, partTwoStack);

        for (int i = 10; i < lines.size(); i++) {
            var line = lines.get(i);
            var movePattern = "move (\\d*) from (\\d*) to (\\d*)";
            var compileMovePattern = Pattern.compile(movePattern);
            var matcher = compileMovePattern.matcher(line);
            matcher.matches();
            var elementsToMoveCount = Integer.parseInt(matcher.group(1));
            var sourceContainer = Integer.parseInt(matcher.group(2)) - 1;
            var targetContainer = Integer.parseInt(matcher.group(3)) - 1;

            var elementsToMove = new Stack<String>();
            for (; elementsToMoveCount > 0; elementsToMoveCount--) {
                partOneStack.get(targetContainer).push(partOneStack.get(sourceContainer).pop());
                elementsToMove.add(0, partTwoStack.get(sourceContainer).pop());
            }
            partTwoStack.get(targetContainer).addAll(elementsToMove);
        }

        System.out.println(partOneStack.stream().map(Stack::peek).reduce((String partialResult, String container) -> partialResult + container));
        System.out.println(partTwoStack.stream().map(Stack::peek).reduce((String partialResult, String container) -> partialResult + container));
    }

    private static void parseStack(List<String> lines, List<Stack<String>> stacks) {
        for (int i = 0; i < 8; i++) {
            var line = lines.get(i);

            for (int column = 0; column < 9; column++) {
                if (line.length() <= column * 4 + 1) {
                    break;
                }

                var container = line.substring(column * 4 + 1, (column + 1) * 4 - 2);

                if (!" ".equals(container)) {
                    stacks.get(column).add(0, container);
                }
            }
        }
    }
}
