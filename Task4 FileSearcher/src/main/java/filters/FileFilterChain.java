package filters;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileFilterChain {

    private Filter filter;
    private File folder;

    private String nameConstraint;
    private String extensionConstraint;
    private long minSize;
    private long maxSize;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void getParametersFromConsole() {
        Scanner sc = new Scanner(System.in);
        getFolder(sc);

        System.out.println(">Искать по имени файла? (0\\1)");
        if (sc.nextInt() == 1) addNameFilter(sc);

        System.out.println(">Искать по расширению файла? (0\\1)");
        if (sc.nextInt() == 1) addExtensionFilter(sc);

        System.out.println(">Искать по размеру файла? (0\\1)");
        if (sc.nextInt() == 1) addSizeFilter(sc);

        System.out.println(">Искать по последней дате изменения файла? (0\\1)");
        if (sc.nextInt() == 1) addDateFilter(sc);
    }

    private void getFolder(Scanner sc) {
        System.out.println("Hello, enter folder path, please.");
        String filePath = sc.nextLine();
        folder = new File(filePath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Input should be a folder.");
        }
    }

    private void addNameFilter(Scanner sc) {
        System.out.println(">Введите имя (или часть имени)");
        nameConstraint = sc.next();
        filter = new NameFilter(filter, nameConstraint);
    }

    private void addExtensionFilter(Scanner sc) {
        System.out.println(">Введите расширение файла");
        extensionConstraint = sc.next();
        filter = new ExtensionFilter(filter, extensionConstraint);
    }

    private void addSizeFilter(Scanner sc) {
        System.out.println(">Введите минимальный размер файла (в байтах)");
        minSize = sc.nextLong();
        System.out.println(">Введите ммаксимальный размер файла (в байтах)");
        maxSize = sc.nextLong();
        filter = new SizeFilter(filter, minSize, maxSize);
    }

    private void addDateFilter(Scanner sc) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        System.out.println(">Введите левую границу последнего изменения (yyyy-MM-dd HH:mm:ss)");
        sc.nextLine();
        startDate = LocalDateTime.parse(sc.nextLine(), formatter);
        System.out.println(">Введите правую границу последнего изменения (yyyy-MM-dd HH:mm:ss)");
        endDate = LocalDateTime.parse(sc.nextLine(), formatter);
        filter = new DateFilter(filter, startDate, endDate);
    }

    public List<File> getResultFiles() {
        List<File> result = new ArrayList<>();
        Arrays.asList(folder.listFiles()).stream()
                .filter(file -> filter.doChain(file))
                .forEach(result::add);
        return result;
    }
}
