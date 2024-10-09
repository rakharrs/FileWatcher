package mg.orange.process.cacheManager;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCache<K, V> implements Cache<K, V> {
    private Map<K, V> cache = new HashMap<>();
    private static final long DEFAULT_EXPIRATION_TIME_SECONDS = 3600; // 1 hour

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
