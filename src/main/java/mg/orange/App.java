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

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("test");
        System.out.println(file.exists());
        Path path = Paths.get(file.getAbsolutePath());
        watchFile(path);
    }

    public static void watchFile(Path path) throws IOException, InterruptedException {
        System.out.println("Watching file: " + path.getFileName());
        WatchService watchService = FileSystems.getDefault().newWatchService();
        WatchKey key = path.register(
                watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }
    }
}
