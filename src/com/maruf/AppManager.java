package com.maruf;

import Models.File;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AppManager {
    private static final int MAX_SUGGESTED_FILES_TO_DELETE = 10;
    public void Start(){
        Path directory = FileManager.getFilePathFromUser();
        Stream<Path> allFilesInDirectoryAsStream = FileManager.getFilesInDirectory(directory);
        List<File> suggestedFiles = Utility.getSuggestedFilesForDeletion(allFilesInDirectoryAsStream, MAX_SUGGESTED_FILES_TO_DELETE);
        Utility.printSuggestedFilesForDeletion(suggestedFiles, directory);
        if(suggestedFiles.size() == 0) return;
        List<File> chosenFiles = Utility.GetChosenFileForDeletion(suggestedFiles);

        FileManager.deleteFiles(chosenFiles);

    }
}
