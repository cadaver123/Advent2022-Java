package day11;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Monkey {
    LinkedList<BigInteger> items = new LinkedList<>();
    String operation;
    BigInteger devisibilityTestNumber;
    Integer positiveTarget;
    Integer negativeTarget;
    BigInteger numberOfInspections = BigInteger.ZERO;

    @Override
    public String toString() {
        return "Monkey{" +
                "items=" + items +
                ", operation='" + operation + '\'' +
                ", testValue=" + devisibilityTestNumber +
                ", positiveTarget=" + positiveTarget +
                ", negativeTarget=" + negativeTarget +
                ", numberOfInspections=" + numberOfInspections +
                '}';
    }
}

public class Main {
    private static final Pattern MONKEY_PATTERN = Pattern.compile("Monkey (\\d*):");
    private static final Pattern ITEMS_PATTERN = Pattern.compile("Starting items: (.*)");
    private static final Pattern OPERATION_PATTERN = Pattern.compile("Operation: new = (.*)");
    private static final Pattern VALUE_PATTERN = Pattern.compile("Test: divisible by (\\d*)");
    private static final Pattern POSITIVE_TARGET_PATTERN = Pattern.compile("If true: throw to monkey (\\d*)");
    private static final Pattern NEGATIVE_TARGET_PATTERN = Pattern.compile("If false: throw to monkey (\\d*)");

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input11.txt"));
        partOne(lines);
        partTwo(lines);
    }

    private static void partOne(List<String> lines) {
        List<Monkey> monkeys = new ArrayList<>();

        for (var monkey = 0; monkey < 8; monkey++) {
            monkeys.add(new Monkey());
        }

        Monkey currentMonkey = null;

        for (var line : lines) {
            currentMonkey = parseInput(monkeys, currentMonkey, line);
        }

        BigInteger modulo = getModulo(monkeys);

        for (var round = 0; round < 20; round++) {
            calculateRound(monkeys, 3, modulo);
        }

        System.out.println(getResult(monkeys));
    }

    private static BigInteger getModulo(List<Monkey> monkeys) {
        return BigInteger.valueOf(monkeys.stream().mapToInt(m -> m.devisibilityTestNumber.intValue()).reduce((a, b) -> a * b).getAsInt());
    }

    private static Monkey parseInput(List<Monkey> monkeys, Monkey currentMonkey, String line) {
        line = line.trim();
        Matcher monkeyMatcher = MONKEY_PATTERN.matcher(line);
        Matcher itemsMatcher = ITEMS_PATTERN.matcher(line);
        Matcher operationMatcher = OPERATION_PATTERN.matcher(line);
        Matcher valueMatcher = VALUE_PATTERN.matcher(line);
        Matcher positiveTargetMatcher = POSITIVE_TARGET_PATTERN.matcher(line);
        Matcher negativeTargetMatcher = NEGATIVE_TARGET_PATTERN.matcher(line);

        if (monkeyMatcher.matches()) {
            currentMonkey = monkeys.get(Integer.parseInt(monkeyMatcher.group(1)));
        } else if (itemsMatcher.matches()) {
            for (var item : itemsMatcher.group(1).split(",")) {
                currentMonkey.items.add(new BigInteger(item.trim()));
            }
        } else if (operationMatcher.matches()) {
            currentMonkey.operation = operationMatcher.group(1);
        } else if (valueMatcher.matches()) {
            currentMonkey.devisibilityTestNumber = new BigInteger(valueMatcher.group(1));
        } else if (positiveTargetMatcher.matches()) {
            currentMonkey.positiveTarget = Integer.parseInt(positiveTargetMatcher.group(1));
        } else if (negativeTargetMatcher.matches()) {
            currentMonkey.negativeTarget = Integer.parseInt(negativeTargetMatcher.group(1));
        }
        return currentMonkey;
    }

    private static void partTwo(List<String> lines) {
        List<Monkey> monkeys = new ArrayList<>();

        for (var monkey = 0; monkey < 8; monkey++) {
            monkeys.add(new Monkey());
        }

        Monkey currentMonkey = null;

        for (var line : lines) {
            currentMonkey = parseInput(monkeys, currentMonkey, line);
        }

        BigInteger modulo = getModulo(monkeys);

        for (var round = 0; round < 10000; round++) {
            calculateRound(monkeys, 1, modulo);

            if (round % 100 == 0)
                System.out.println(round);
        }


        System.out.println(getResult(monkeys));
    }

    private static Optional<BigInteger> getResult(List<Monkey> monkeys) {
        return monkeys.stream().map(m -> m.numberOfInspections)
                .sorted(Collections.reverseOrder())
                .limit(2)
                .reduce(BigInteger::multiply);
    }

    private static void calculateRound(List<Monkey> monkeys, Integer divider, BigInteger modulo) {
        BigInteger worryLvl;
        for (var monkey : monkeys) {
            while (monkey.items.size() > 0) {
                var item = monkey.items.removeFirst();
                worryLvl = operation(monkey, item).divide(BigInteger.valueOf(divider));

                int target = worryLvl.divideAndRemainder(monkey.devisibilityTestNumber)[1].equals(BigInteger.ZERO) ? monkey.positiveTarget : monkey.negativeTarget;
                monkeys.get(target).items.add(worryLvl.divideAndRemainder(modulo)[1]);
                monkey.numberOfInspections = monkey.numberOfInspections.add(BigInteger.ONE);
            }
        }
    }

    private static BigInteger operation(Monkey monkey, BigInteger item) {
        BigInteger firstValue = getValue(monkey, item, 0);
        BigInteger secondValue = getValue(monkey, item, 1);
        if (monkey.operation.indexOf('*') > -1) {
            return firstValue.multiply(secondValue);
        } else if (monkey.operation.indexOf('+') > -1) {
            return firstValue.add(secondValue);
        } else {
            throw new IllegalStateException("We shouldn't be here");
        }
    }

    private static BigInteger getValue(Monkey monkey, BigInteger item, int valueNum) {
        String valueString = monkey.operation.split("[*+]")[valueNum].trim();
        return valueString.contains("old") ? item : new BigInteger(valueString);
    }
}
