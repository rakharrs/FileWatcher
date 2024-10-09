package mg.orange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mg.orange.filewatcher.diff.DiffMatch;
import mg.orange.filewatcher.diff.Diff;
import mg.orange.filewatcher.diff.Operation;
import mg.orange.filewatcher.process.watcher.Watcher;
import mg.orange.filewatcher.utils.Misc;
import mg.orange.filewatcher.utils.filemanager.FileManager;

/**
 * Hello world purposes
 *
 */
public class App {
    public static List<String> parseDiff(List<Diff> diffList) {
        List<String> result = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        Operation lastOperation = null;
        for (Diff diff : diffList) {
            String text = diff.text;
            lastOperation = diff.operation;
            if(text.startsWith("\n")){
                text = text.substring(1);
                result.add(currentString.toString().trim());
                currentString = new StringBuilder();
            }
            
            if (diff.operation == Operation.EQUAL) {
                String[] lines = text.split("\n");
                if(!text.endsWith("\n")){
                    currentString = currentString.append(lines[lines.length - 1]);
                }
            } else if (diff.operation == Operation.INSERT) {
                
                String[] lines = text.split("\n");

                for (int i = 0; i < lines.length ; i++) {
                    currentString = currentString.append(lines[i]);
                    System.out.println("is it ? "+lines[i]);
                    System.out.println("INSERT : " + currentString);
                    if(lines.length > i + 1){
                        result.add(currentString.toString().trim());
                        currentString = new StringBuilder();
                    }else if(text.endsWith("\n")){
                        result.add(currentString.toString().trim());
                        currentString = new StringBuilder();
                    }
                }
            }

        }

        if (!currentString.isEmpty() && lastOperation == Operation.INSERT) {
            result.add(currentString.toString().trim());
        }

        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        DiffMatch diffMatch = new DiffMatch();

        // Cache dir path
        String tempFilePath = "test/cache";

        // FileName to listen in the dir
        String fileName = "test.csv";

        // File dir path
        String dirPath = "test";

        System.out.println(Files.readString(Paths.get(dirPath + "/" + fileName)));

        // Instantiate the watcher class
        Watcher watcher = new Watcher(
                // dir path
                dirPath,
                // file name
                fileName,
                // cache dir path
                tempFilePath,
                // on start
                () -> FileManager.copyFile(dirPath + "/" + fileName, Misc.buildCacheName(tempFilePath, fileName)),
                // on shutdown
                () -> {
                    File fileToDelete = new File(Misc.buildCacheName(tempFilePath, fileName));

                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    } else {
                        System.out.println("File not found.");
                    }
                });

        // process daemon...
        watcher.watch(
                () -> {
                    try {
                        watcher.watchFile(
                                dirPath,
                                fileName,
                                tempFilePath,
                                () -> {
                                    Path filePath = Paths.get(dirPath + "/" + fileName);
                                    Path cachePath = Paths.get(Misc.buildCacheName(tempFilePath, fileName));

                                    try {
                                        LinkedList<Diff> diffs = diffMatch.diff_main(Files.readString(cachePath), Files.readString(filePath));
                                        System.out.println(parseDiff(diffs));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    FileManager.copyFile(dirPath + "/" + fileName,
                                            Misc.buildCacheName(tempFilePath, fileName));
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
