package mg.orange.filewatcher.process.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Watcher implements WatcherInterface {
    private Runnable startAction;
    private Runnable shutdownAction;
    private String fileDirectory;
    private String fileName;
    private String cacheDirPath;

    public Watcher(){}

    public Watcher(
        String fileDirectory,
        String fileName,
        String cacheDirPath,
        Runnable startAction,
        Runnable shutdownAction)
    {
        this.cacheDirPath = cacheDirPath;
        this.fileDirectory = fileDirectory;
        this.fileName = fileName;
        this.startAction = startAction;
        this.startAction = startAction;
        this.shutdownAction = shutdownAction;
    }

    public Watcher(
        Runnable startAction, 
        Runnable shutdownAction
    ){
        this.startAction = startAction;
        this.shutdownAction = shutdownAction;
    }

    @Override
    public void watch(Runnable... actions) {
        atShutdown(getShutdownAction());
        atStart(getStartAction());

        for (Runnable action : actions) {
            action.run();
        }
    }

    @Override
    public void atShutdown(Runnable shutdownAction) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->shutdownAction.run()));
    }

    @Override
    public void atStart(Runnable startAction) {
        startAction.run();
    }

    @Override
    public void watchFile(String pathValue, String fileName, String tempFilePath, Runnable... actions)
            throws IOException, InterruptedException {
        System.out.println("Watching file: " + pathValue);

        // caching the file
        Path path = Paths.get(pathValue);

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
                    
                    for (Runnable action : actions) {
                        action.run();
                    }
                }
            }
            key.reset();
        }
    }

    // Getter(s) & Setter(s)
    public Runnable getStartAction() {
        return startAction;
    }

    public void setStartAction(Runnable startAction) {
        this.startAction = startAction;
    }

    public Runnable getShutdownAction() {
        return shutdownAction;
    }

    public void setShutdownAction(Runnable shutdownAction) {
        this.shutdownAction = shutdownAction;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getCacheDirPath() {
        return cacheDirPath;
    }
    
    public void setCacheDirPath(String cacheDirPath) {
        this.cacheDirPath = cacheDirPath;
    }

}
