package com.maruf;

import Exceptions.InputOfRangeException;
import Models.File;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Utility {
    private static final Scanner sc = new Scanner(System.in);

    public static List<File> getSuggestedFilesForDeletion(Stream<Path> filesStream, int maxSuggestion) {
        ArrayList<File> files = getFileListFromStream(filesStream);
        files.sort(Collections.reverseOrder());
        return files.subList(0, Math.min(maxSuggestion, files.size()));
    }

    public static ArrayList<File> getFileListFromStream(Stream<Path> filesStream){
        List<Path> filePaths = null;
        try{
            filePaths = filesStream.filter(Files::isReadable).toList();
        }catch (Exception e){
            System.out.println(e);
        }
        ArrayList<File> files = new ArrayList<>();
        for (Path filePath : filePaths) {
            File file;
            try {
                if(Files.isDirectory(filePath)) continue;
                BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                file = new File(filePath, Utility.getSize(filePath),
                        new Date(attributes.creationTime().toMillis()),
                        new Date(attributes.lastModifiedTime().toMillis()),
                        new Date(attributes.lastAccessTime().toMillis()));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.err.println("Error while reading: " + filePath);
                continue;
            }
            files.add(file);
        }

        return files;
    }

    private static long getSize(Path filePath) {
        long size = 0;
        try {
            size = Files.walk(filePath)
                    .filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void printSuggestedFilesForDeletion(List<File> suggestedFiles, Path rootDirectory) {
        if(suggestedFiles.size() == 0) {
            System.out.println("\nDirectory is empty.");
            return;
        }
        System.out.println("\nThese are the files consuming most space:");
        for (int i = 0; i < suggestedFiles.size(); i++) {
            File file = suggestedFiles.get(i);
            Path relativePath = rootDirectory.relativize(suggestedFiles.get(i).getPath());
            System.out.printf("#%d:\n\tName:%s\n\tSize:%s\n\tPath:%s\n"
                    ,i+1, file.getPath().getFileName(), Utility.getFormattedFileSize(file.getSize()), relativePath);
        }
    }

    public static List<File> GetChosenFileForDeletion(List<File> files) {

        boolean allValidNumber = false;
        int totalFiles = files.size();
        List<File> chosenFiles = new ArrayList<>();

        do {
            System.out.println("\nChoose files to delete. or type \"exit\" to exit.");
            System.out.print("Enter space separated serial numbers: ");
            String[] indexesAsStr = sc.nextLine().split("\s");
            if(Arrays.stream(indexesAsStr).anyMatch(x -> x.toLowerCase().contains("exit"))) return chosenFiles;
            for (String s : indexesAsStr) {
                try {
                    allValidNumber = true;
                    int i = Integer.parseInt(s);
                    checkFileIndexValidity(i, 1, totalFiles);
                    chosenFiles.add(files.get(i-1));
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid numbers. Try again.\n");
                    allValidNumber = false;
                    break;
                }
                catch (InputOfRangeException e) {
                    System.out.println("\n" +e.getMessage()+ "Try again.\n");
                    allValidNumber = false;
                    break;
                }

            }
        }while (!allValidNumber);

        return chosenFiles;
    }

    public static void checkFileIndexValidity(int index, int lo, int hi) {
        if( !(index >= lo && index <= hi) ) throw new InputOfRangeException();
    }

    public static String getFormattedDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String getFormattedFileSize(double size) {
        int byteMultiply = 1, maxMultiply = 5;
        while (size >= 1024 && byteMultiply <= maxMultiply) {
            size /= 1024;
            byteMultiply++;
        }
        return String.format("%.2f", size) + getUnit(byteMultiply);
    }

    private static String getUnit(int byteMultiply) {
        switch (byteMultiply) {
            case 1 -> {
                return "Byte";
            }
            case 2 -> {
                return "KB";
            }
            case 3 -> {
                return "MB";
            }
            case 4 -> {
                return "GB";
            }
            case 5 -> {
                return "TB";
            }
            default -> {
                return "";
            }
        }
    }

    public static long getDaysFromMilliSec(long ms) {
        return ms / (1000 * 60 * 60 * 24);
    }
}
