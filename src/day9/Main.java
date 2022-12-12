package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Position other) {
        return Math.sqrt(Math.pow((this.x - other.x), 2) + Math.pow((this.y - other.y), 2));
    }

    public boolean isDiagonal(Position other) {
        return (this.x - other.x == 0) && (this.y - other.y == 0);
    }

    public Position move(Direction dir) {
        return switch (dir) {
            case Up -> new Position(this.x, this.y + 1);
            case Down -> new Position(this.x, this.y - 1);
            case Left -> new Position(this.x - 1, this.y);
            case Right -> new Position(this.x + 1, this.y);
            case TopLeft -> new Position(this.x - 1, this.y + 1);
            case TopRight -> new Position(this.x + 1, this.y + 1);
            case DownRight -> new Position(this.x + 1, this.y - 1);
            case DownLeft -> new Position(this.x - 1, this.y - 1);
            case NoMove -> new Position(this.x , this.y);
        };
    }


    public Position copy() {
        return new Position(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

enum Direction {
    Up,
    Down,
    Left,
    Right,
    TopLeft,
    TopRight,
    DownRight,
    DownLeft,
    NoMove;

    public static Direction get(String s) throws IllegalStateException {
        return switch (s) {
            case "R" -> Direction.Right;
            case "L" -> Direction.Left;
            case "U" -> Direction.Up;
            case "D" -> Direction.Down;
            default -> throw new IllegalStateException("We should not be here");
        };
    }
}

class State {
    public List<Position> rope;

    Set<Position> visited = new LinkedHashSet<>();

    public State(int length) {
        this.rope = new ArrayList<>(length);
        for (; length > 0; length--) {
            this.rope.add(new Position(0, 0));
        }
    }
}

public class Main {
    private static final Pattern MOVE_PATTERN = Pattern.compile("([RLUD]) (\\d*)");
    private static final double EPSILON = 0.00000000001d;

    public static void main(String[] args) throws IOException {
        var statePartOne = new State(2);
        var statePartTwo = new State(10);
        var lines = Files.readAllLines(Paths.get("input9.txt"));

        for (var line : lines) {
            var moveMatcher = MOVE_PATTERN.matcher(line);

            if (!moveMatcher.matches()) {
                throw new IllegalStateException("We should not be here");
            }
            var direction = Direction.get(moveMatcher.group(1));
            var howManyMoves = Integer.parseInt(moveMatcher.group(2));

            move(statePartOne, direction, howManyMoves);
            move(statePartTwo, direction, howManyMoves);
        }

        System.out.println(statePartOne.visited.size());
        System.out.println(statePartTwo.visited.size());

    }

    private static void move(State state, Direction dir, int howManyMoves) {
        for (; howManyMoves > 0; howManyMoves--) {
            state.rope.set(0, state.rope.get(0).move(dir)); //set a new head

            for (int knotIndex = 1; state.rope.size() > knotIndex; knotIndex++) { //move other knots
                Position newKnot;
                Position currentKnot = state.rope.get(knotIndex);
                Position knotInFrontOfCurrent = state.rope.get(knotIndex - 1);
                double newDistance = currentKnot.distance(knotInFrontOfCurrent);

                if (newDistance >= (2.0d - EPSILON)) {
                    Direction optimalDirection = Arrays.stream(Direction.values()).min(Comparator.comparingDouble((direction) -> currentKnot.move(direction).distance(knotInFrontOfCurrent))).get();
                    newKnot = currentKnot.move(optimalDirection);
                } else {
                    newKnot = currentKnot.copy();
                }

                state.rope.set(knotIndex, newKnot);

                if (knotIndex == state.rope.size() - 1) {
                    state.visited.add(newKnot); //set visited location of the rope's tail;
                }
            }
        }
    }
}