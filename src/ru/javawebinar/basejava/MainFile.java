package ru.javawebinar.basejava;

import java.io.File;
import java.util.Optional;

public class MainFile {

    private static int i = 0;

    public static void main(String[] args) {
        File directory = new File("./");

        System.out.println(directory.isDirectory());
        System.out.println(directory.getAbsolutePath());
        System.out.println();

        showFiles(directory);
        System.out.println();
    }

    public static void showFiles(File directory) {
        Optional<File[]> files = Optional.ofNullable(directory.listFiles());
        StringBuilder spaces = new StringBuilder();
        spaces.append(" ".repeat(i));
        if (files.isPresent()) {
            for (File f : files.get()) {
                if (f.isFile()) {
                    System.out.print(spaces + "File: " + f.getName() + "\n");
                } else {
                    System.out.print(spaces + "Directory: " + f.getName() + "\n");
                    i++;
                    showFiles(f);
                    i--;
                }
            }
        }
    }
}