package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WarsztatKamilWyjadlowski {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;


    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        tasks = tab(FILE_NAME);
        printOptions(OPTIONS);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "add" -> dodaj();
                case "remove" -> {
                    usun(tasks, numer());
                    System.out.println("Value was successfully deleted.");
                }
                case "list" -> lista(tasks);
                case "exit" -> {
                    zapisz(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                }
                default -> System.out.println("Please select a correct option.");
            }
            printOptions(OPTIONS);
        }
    }

    public static String[][] tab(String fileName) {
        Path dir = Paths.get(fileName);
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                System.arraycopy(split, 0, tab[i], 0, split.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    private static void dodaj() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please add task description");
        String description = scanner.nextLine();

        boolean isDateValid = false;
        String date = "";
        while (!isDateValid) {
            System.out.println("Please add task due date (yyyy-mm-dd)");
            date = scanner.nextLine();
            isDateValid = poprawnaData(date);

            if (!isDateValid) {
                System.out.println("Invalid date format. Please enter a valid date.");
            }
        }

        System.out.println("Is your task important: true/false");
        boolean poprawnyBoolean = false;
        String important = "";
        while (!poprawnyBoolean) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                important = input;
                poprawnyBoolean = true;
            } else {
                System.out.println("Write true or false");
            }
        }

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = date;
        tasks[tasks.length - 1][2] = important;
    }

    private static boolean poprawnaData(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static void usun(String[][] tab, int index) {
        index -= 1;
        boolean isIndexValid = index >= 0 && index < tab.length;

        while (!isIndexValid) {
            System.out.println("Invalid index. Please provide a valid index.");
            index = numer() - 1;
            isIndexValid = index >= 0 && index < tab.length;
        }
        tasks = ArrayUtils.remove(tab, index);
    }

    public static void lista(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print((i + 1) + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int numer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Select right rowe number");
        }
        return scanner.nextInt();
    }

    public static void zapisz(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}