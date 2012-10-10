package nl.d2n.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheReporter.class);

    @Autowired
    private EhCacheCacheManager cacheManager;

    /* http://docs.oracle.com/cd/E14003_01/doc/doc.1014/e12030/cron_expressions.htm */

    //    @Scheduled(cron="0/5 * * * * *")
    @Scheduled(cron="0 0/10 * * * *")
    public void reportOnCache() {
        reportOnCache((net.sf.ehcache.Cache)cacheManager.getCache("info").getNativeCache());
        reportOnCache((net.sf.ehcache.Cache)cacheManager.getCache("distinctions").getNativeCache());
        reportOnCache((net.sf.ehcache.Cache)cacheManager.getCache("cities").getNativeCache());
        LOGGER.info(
                "@MEM Free / total MB memory: "+
                        convertBytesToMb(Runtime.getRuntime().freeMemory())+" / " +
                        convertBytesToMb(Runtime.getRuntime().totalMemory())+" MB");
    }

    protected long convertBytesToMb(long bytes) {
        return bytes / 1048576;
    }

    protected void reportOnCache(net.sf.ehcache.Cache cache) {
        long cacheHits = cache.getStatistics().getCacheHits();
        long cacheMisses = cache.getStatistics().getCacheMisses();
        LOGGER.info(
                "@MEM "+cache.getName()+" -> "+
                        "# of items in cache: "+cache.getSize()+ ", " +
                        "# of evictions: "+cache.getStatistics().getEvictionCount() + ", " +
                        "# cache hits/total: "+ cacheHits + "/" + (cacheHits + cacheMisses)
        );
    }

}
