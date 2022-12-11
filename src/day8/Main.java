package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input8.txt"));
        int[][] forest = new int[lines.size()][lines.size()];
        boolean[][] visibleTrees = new boolean[lines.size()][lines.size()];

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.size(); col++) {
                forest[row][col] = Integer.parseInt(String.valueOf(lines.get(row).charAt(col)));
            }
        }

        countByRowsFromLeft(lines, forest, visibleTrees);
        countByRowsFromRight(lines, forest, visibleTrees);
        countByColumnsFromTop(lines, forest, visibleTrees);
        countByColumnsFromBottom(lines, forest, visibleTrees);

        int count = 0;
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.size(); col++) {
                count += visibleTrees[row][col] ? 1 : 0;
            }
        }
        System.out.println(count);

        int max = 0;
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.size(); col++) {
                max = Math.max(scenicScore(forest, row, col), max);
            }
        }

        System.out.println(max);
    }

    private static int scenicScore(int[][] forest, int row, int col) {
        if (row == 0 || row == forest.length - 1 || col == 0 || col == forest[0].length - 1) {
            return 0;
        }

        int toTop = 0, toBottom = 0, toRight = 0, toLeft = 0;

        for (int newRow = row + 1; newRow < forest.length; newRow++) {
            toBottom++;
            if (forest[newRow][col] >= forest[row][col]) {
                break;
            }
        }

        for (int newRow = row - 1; newRow >= 0; newRow--) {
            toTop++;
            if (forest[newRow][col] >= forest[row][col]) {
                break;
            }
        }

        for (int newCol = col + 1; newCol < forest[0].length; newCol++) {
            toRight++;
            if (forest[row][newCol] >= forest[row][col]) {
                break;
            }
        }


        for (int newCol = col - 1; newCol >= 0; newCol--) {
            toLeft++;
            if (forest[row][newCol] >= forest[row][col]) {
                break;
            }
        }


        return toLeft * toRight * toTop * toBottom;
    }

    private static void countByRowsFromLeft(List<String> lines, int[][] forest, boolean[][] visibleTrees) {
        for (int row = 0; row < lines.size(); row++) {
            int max = -1;

            for (int col = 0; col < lines.size(); col++) {
                if (forest[row][col] > max) {
                    visibleTrees[row][col] = true;
                    max = forest[row][col];
                }
            }
        }
    }

    private static void countByRowsFromRight(List<String> lines, int[][] forest, boolean[][] visibleTrees) {
        for (int row = 0; row < lines.size(); row++) {
            int max = -1;

            for (int col = lines.size() - 1; col >= 0; col--) {
                if (forest[row][col] > max) {
                    visibleTrees[row][col] = true;
                    max = forest[row][col];
                }
                ;
            }
        }
    }

    private static void countByColumnsFromTop(List<String> lines, int[][] forest, boolean[][] visibleTrees) {
        for (int col = 0; col < lines.size(); col++) {
            int max = -1;

            for (int row = 0; row < lines.size(); row++) {
                if (forest[row][col] > max) {
                    visibleTrees[row][col] = true;
                    max = forest[row][col];
                }
            }
        }
    }


    private static void countByColumnsFromBottom(List<String> lines, int[][] forest, boolean[][] visibleTrees) {
        for (int col = 0; col < lines.size(); col++) {
            int max = -1;

            for (int row = lines.size() - 1; row >= 0; row--) {
                if (forest[row][col] > max) {
                    visibleTrees[row][col] = true;
                    max = forest[row][col];
                }
            }
        }
    }
}
