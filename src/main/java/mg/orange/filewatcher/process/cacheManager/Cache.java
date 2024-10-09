package mg.orange.filewatcher.process.cacheManager;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void remove(K key);
    void clear();
}
