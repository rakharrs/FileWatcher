package mg.orange.filewatcher.utils;

public class Misc {
    public static String buildCacheName(String tempFilePath, String fileName){
        return tempFilePath+"/cached+"+fileName;
    }
}
