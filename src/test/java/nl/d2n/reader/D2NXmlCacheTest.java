package nl.d2n.reader;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.Info;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class D2NXmlCacheTest extends SpringContextTestCase {

    private static final Integer CITY_ID = 13449;

    @Autowired
    private D2NXmlCache cache;

    @Autowired
    private EhCacheCacheManager cacheManager;

    @Before
    public void cleanCache() {
        cacheManager.getCache("info").clear();
    }

    @Test
    public void readFromFile() throws ApplicationException {
        String xmlText = readFromFile(D2NXmlReaderFromFile.INPUT_PATH, CITY_ID);
        assertNotNull(xmlText);
    }

    @Test
    public void writeToCache() throws ApplicationException {
        Info info = new Info();
        cache.writeToCache(CITY_ID, info);
        info = cache.readFromCache(CITY_ID);
        assertNotNull(info);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @Test
    public void writeToFile() throws ApplicationException {
        String xmlText = readFromFile(D2NXmlReaderFromFile.INPUT_PATH, CITY_ID);
        D2NXmlFile fileAccessor = createFileAccessor("target");
        cache.setD2NXmlFile(fileAccessor);
        cache.writeToFile(CITY_ID, xmlText);
        File writtenFile = new File(D2NXmlFile.getFilePath("target", CITY_ID));
        assertTrue(writtenFile.exists());
        writtenFile.delete();
    }

    @Test
    public void writeToFileThrowsException() {
        cache.setD2NXmlFile(createFileAccessorThrowingException(new IOException()));
        try {
            cache.writeToFile(CITY_ID, "");
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.SYSTEM_ERROR, err.getError());
        }
    }

    @Test
    public void readFromFileThrowsException() {
        readFromFileThrowsException(new IOException());
        readFromFileThrowsException(new FileNotFoundException());
    }

    protected void readFromFileThrowsException(IOException throwThis) {
        cache.setD2NXmlFile(createFileAccessorThrowingException(throwThis));
        try {
            cache.readFromFile(CITY_ID);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.SYSTEM_ERROR, err.getError());
        }
    }

    protected String readFromFile(String path, Integer cityId) throws ApplicationException {
        D2NXmlFile fileAccessor = createFileAccessor(path);
        cache.setD2NXmlFile(fileAccessor);
        String xmlText = cache.readFromFile(cityId);
        assertNotNull(xmlText);
        return xmlText;
    }

    protected D2NXmlFile createFileAccessorThrowingException(final IOException throwThis) {
        return new D2NXmlFile() {
            public void writeFile(final String xmlText, final Integer cityId) throws IOException, FileNotFoundException {
                throw throwThis;
            }
            public String readFile(final Integer cityId) throws IOException, FileNotFoundException {
                throw throwThis;
            }
        };
    }
    protected D2NXmlFile createFileAccessor(String path) {
        return new D2NXmlFile(path);
    }
}
