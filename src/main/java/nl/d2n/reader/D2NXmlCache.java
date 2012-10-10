package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class D2NXmlCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(D2NXmlCache.class);

    @Autowired
    private EhCacheCacheManager cacheManager;

    @Autowired
    private D2NXmlFile fileAccessor;

    protected Cache getInfoCache() {
        return cacheManager.getCache("info");
    }

    public void writeToCache(Integer cityId, Info genericInfo) throws ApplicationException {
        getInfoCache().put(cityId, genericInfo);
    }

    public void writeToFile(Integer cityId, String xmlText) throws ApplicationException {
        try {
            fileAccessor.writeFile(xmlText, cityId);
        } catch (Exception err) {
            LOGGER.error(err.getMessage());
            throw new ApplicationException(D2NErrorCode.SYSTEM_ERROR);
        }
    }

    public Info readFromCache(Integer cityId) throws ApplicationException {
        Cache.ValueWrapper wrapper = getInfoCache().get(cityId);
        return wrapper == null ? null : (Info)wrapper.get();
    }

    public String readFromFile(Integer cityId) throws ApplicationException {
        try {
            return fileAccessor.readFile(cityId);
        } catch (FileNotFoundException err) {
            LOGGER.error("Could not find XML for city #"+cityId+": "+err.getMessage());
            throw new ApplicationException(D2NErrorCode.SYSTEM_ERROR);
        } catch (Exception err) {
            LOGGER.error("Reading from cache for city #"+cityId+": "+err.getMessage());
            throw new ApplicationException(D2NErrorCode.SYSTEM_ERROR);
        }
    }

    public void setD2NXmlFile(D2NXmlFile fileAccessor) { this.fileAccessor = fileAccessor; }
}
