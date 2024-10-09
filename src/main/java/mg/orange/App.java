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

import mg.orange.process.fileManager.FileManager;

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

        // Shutdown hook
        whenShutdown(tempFilePath, fileName);

        // process daemon...
        watchFile(dirPath, fileName, tempFilePath);
    }

    public static void whenShutdown(String tempFilePath, String fileName) {
        String fileCache = buildCacheName(tempFilePath, fileName);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File fileToDelete = new File(fileCache);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            } else {
                System.out.println("File not found.");
            }
        }));
    }

    public static String buildCacheName(String tempFilePath, String fileName){
        return tempFilePath+"/cached+"+fileName;
    }

    public static void watchFile(String pathValue, String fileName, String tempFilePath) throws IOException, InterruptedException {
        System.out.println("Watching file: " + pathValue);

        // caching the file
        Path path = Paths.get(pathValue);

        // Copy
        FileManager.copyFile(pathValue+"/"+fileName, buildCacheName(tempFilePath, fileName));

        // Watch service
        WatchService watchService = FileSystems.getDefault().newWatchService();
        WatchKey key = path.register(
                watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        // Watch for file changes
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                final Path changed = (Path) event.context();

                // Checko for the file
                if (changed.endsWith(fileName)) {

                    // Processing function...
                    System.out.println("Event kind:" + event.kind() + "File changed: " + changed);
                }
            }
            key.reset();
        }
    }
}
