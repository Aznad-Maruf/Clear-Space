package com.maruf;

import Models.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileManager {
    private static final Scanner sc = new Scanner(System.in);
    public static Path getFilePathFromUser(){
        String enteredPath;
        do {
            System.out.print("\nEnter Directory path: ");
            enteredPath = sc.nextLine();
            try {
                if(Files.exists(Path.of(enteredPath)) && Files.isDirectory(Path.of(enteredPath))){
                    return Path.of(enteredPath);
                }
                System.err.println("\nPlease enter a valid directory path.\n");
            } catch (Exception e) {
                System.err.println("\nPlease try again with a valid directory path.\n");
            }
        }while (true);
    }

    public static Stream<Path> getFilesInDirectory(Path path){
        try {
            return Files.walk(path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("\nWe are sorry for the error.Please try again.");
            System.exit(1);
        }
        return null;
    }

    public static void deleteFiles(List<File> chosenFiles) {
        for (File chosenFile : chosenFiles) {
            if(Files.exists(chosenFile.getPath())){
                try {
                    boolean successfullyDeleted = deleteFile(chosenFile);
                    if(successfullyDeleted) System.out.println("Deleted Successfully.");
                    else System.out.println("File skipped.");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Could not delete file.");
                }
            }
        }
    }

    private static boolean deleteFile(File chosenFile) throws IOException {
        System.out.println("File: " + chosenFile.getPath().getFileName());
        System.out.println("Size: " + Utility.getFormattedFileSize(chosenFile.getSize()));
        System.out.print("Are you sure you want to delete? (yes/no): ");
        if(sc.nextLine().toLowerCase().contains("yes")){
            Files.delete(chosenFile.getPath());
            return true;
        }
        else return false;
    }
}
