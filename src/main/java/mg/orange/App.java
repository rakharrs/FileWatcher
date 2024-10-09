package mg.orange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import mg.orange.filewatcher.diff.DiffMatch;
import mg.orange.filewatcher.process.fileManager.FileManager;
import mg.orange.filewatcher.process.watcher.Watcher;
import mg.orange.filewatcher.utils.Misc;

/**
 * Hello world purposes
 *
 */
public class App {
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
                                System.out.println(
                                    diffMatch.diff_main(Files.readString(filePath), Files.readString(cachePath))
                                );
                                
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            FileManager.copyFile(dirPath + "/" + fileName, Misc.buildCacheName(tempFilePath, fileName));
                        }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
