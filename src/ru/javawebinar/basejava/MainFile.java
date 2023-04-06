package ru.javawebinar.basejava;

import java.io.File;
import java.util.Optional;

public class MainFile {
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
        if (files.isPresent()) {
            for (File f : files.get()) {
                if (f.isFile()) {
                    System.out.println("File: " + f.getName());
                } else {
                    System.out.println();
                    System.out.println("Directory: " + f.getName());
                    showFiles(f);
                }
            }
        }
    }
}
