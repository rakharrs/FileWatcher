package mg.orange.filewatcher.process.watcher;

public interface WatcherInterface {

    // Called when shutting down the application
    void atShutdown(Runnable action);

    // Called when starting the application
    void atStart(Runnable action);

    // Daemon that call the two above function with actions moreover
    public void watch(Runnable... actions);

    // Daemon that watch file
    void watchFile(String pathValue, String fileName, String tempFilePath, Runnable... actions) throws Exception;

}
