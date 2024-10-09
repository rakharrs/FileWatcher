package mg.orange;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import mg.orange.filewatcher.process.fileManager.FileManager;
import mg.orange.filewatcher.process.watcher.Watcher;
import mg.orange.filewatcher.utils.Misc;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Cache dir path
        String tempFilePath = "test/cache";

        // FileName to listen in the dir
        String fileName = "test.txt";

        // File dir path
        String dirPath = "test";

        // Instantiate the watcher class
        Watcher watcher = new Watcher(
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
