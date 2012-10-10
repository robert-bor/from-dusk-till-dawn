package nl.d2n.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class SiteKeyChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteKeyChecker.class);

    public static final String EXTERNAL_APPLICATION_PREFIX = "external_application";

    Map<String, ExternalApplication> keys = new TreeMap<String, ExternalApplication>();

    public SiteKeyChecker(Properties properties) {
        for (String propertyName : properties.stringPropertyNames()) {
            if (!propertyName.startsWith(EXTERNAL_APPLICATION_PREFIX)) {
                continue;
            }
            String key = (String)properties.get(propertyName);
            ExternalApplication externalApplication = ExternalApplication.findApplicationForName(propertyName.substring(EXTERNAL_APPLICATION_PREFIX.length()+1));
            LOGGER.info(externalApplication+"="+key);
            keys.put(key, externalApplication);
        }
    }
    public ExternalApplication getExternalApplication(String key) throws ApplicationException {
        ExternalApplication externalApplication = keys.get(key);
        if (externalApplication == null) {
            LOGGER.error("Site key \""+key+"\" is not known");
            throw new ApplicationException(D2NErrorCode.UNKNOWN_SITE_KEY);
        }
        return keys.get(key);
    }
}
