package mg.orange.process.fileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileManager {
    public static void copyFile(String source, String destination) {
        Path sourcePath = Path.of(source);
        Path targetPath = Path.of(destination);
        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully!");
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());  

        }
    }

}
