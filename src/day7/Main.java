package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

class Entity {
    public Entity parent;
    public List<Entity> children = new ArrayList<>();
    public boolean isFolder;
    public int size;
    public String name;

    public int size() {
        return isFolder ? children.stream().mapToInt(Entity::size).sum() : size;
    }

    public Entity(Entity parent, String name, boolean isFolder, int size) {
        this.parent = parent;
        this.name = name;
        this.isFolder = isFolder;
        this.size = size;
    }

    public Entity addChildOrReturnParentFolder(String target) {
        if (target.equals("..")) {
            return parent;
        } else { //assuming that we will never go two times to same folder
            Entity newFolder = new Entity(this, target, true, 0);
            children.add(newFolder);

            return newFolder;
        }
    }

    public void addFile(int size, String name) {
        children.add(new Entity(this, name, false, size));
    }

    @Override
    public String toString() {
        return "Entity{" +
                "parent=" + parent.name +
                ", name='" + name + '\'' +
                ", isFolder=" + isFolder +
                //", children=" + children +
                ", size=" + size +
                '}';
    }
}


public class Main {
    public static void main(String[] args) throws IOException {
        var root = new Entity(null, "/", true, 0);
        var currentFolder = root;

        for (var line : Files.readAllLines(Paths.get("input7.txt"))) {
            Entity extractFolder = extractFolder(currentFolder, line);
            currentFolder = extractFolder != null ? extractFolder : currentFolder;
            extractFile(currentFolder, line);
        }

        partOne(root);
        partTwo(root);
    }

    private static void partOne(Entity root) {
        AtomicInteger result = new AtomicInteger(0);
        countFoldersWithSizeAtMost100k(result, root, 0);
        System.out.println(result);
    }

    private static void partTwo(Entity root) {
        List<Integer> sizes = new ArrayList<>();
        getSizes(sizes, root);
        Integer systemFreeSpace = 70000000 - root.size();
        System.out.println(sizes.stream().sorted().map(size -> systemFreeSpace + size).filter(size -> size >= 30000000).findFirst().get() - systemFreeSpace);
    }

    static void countFoldersWithSizeAtMost100k(final AtomicInteger count, Entity currentFolder, int lvl) {
        count.getAndAdd(currentFolder.size() <= 100000 ? currentFolder.size() : 0);
        //System.out.println(currentFolder.name + " " + currentFolder.size() + " " + count.get() );
        List<Entity> childFolders = currentFolder.children.stream().filter(child -> child.isFolder).toList();

        for (var folder : childFolders) {
            countFoldersWithSizeAtMost100k(count, folder, lvl + 1);
        }
        //System.out.println(lvl);
    }

    static void getSizes(final List<Integer> foldersSizes, Entity currentFolder) {
        foldersSizes.add(currentFolder.size());
        List<Entity> childFolders = currentFolder.children.stream().filter(child -> child.isFolder).toList();

        for (var folder : childFolders) {
            getSizes(foldersSizes, folder);
        }
    }

    private static void extractFile(Entity currentFolder, String line) {
        var filePattern = "([0-9]+) (.*)";
        var fileCompiledPatter = Pattern.compile(filePattern);
        var fileMatcher = fileCompiledPatter.matcher(line);

        if (fileMatcher.find()) {
            currentFolder.addFile(Integer.parseInt(fileMatcher.group(1)), fileMatcher.group(2));
        }
    }

    private static Entity extractFolder(Entity currentFolder, String line) {
        var folderPattern = "\\$ cd (.*)";
        var folderCompiledPatter = Pattern.compile(folderPattern);
        var folderMatcher = folderCompiledPatter.matcher(line);

        return folderMatcher.find() && !"/".equals(folderMatcher.group(1)) ? currentFolder.addChildOrReturnParentFolder(folderMatcher.group(1)) : null;
    }
}
