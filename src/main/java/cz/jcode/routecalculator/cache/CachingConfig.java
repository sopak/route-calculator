package cz.jcode.routecalculator.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache setup
 */
@Configuration
@EnableCaching
public class CachingConfig {

    private static final String ROUTES = "routes";
    private static final int CACHE_DURATION_IN_SECONDS = 10;
    private static final int MAXIMUM_SIZE = 100;

    @Bean
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager(ROUTES) {
            @Override
            protected ConcurrentMapCache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder().expireAfterWrite(CACHE_DURATION_IN_SECONDS, TimeUnit.SECONDS).maximumSize(MAXIMUM_SIZE).build().asMap(),
                        true
                );
            }
        };

    }
}
