package nl.d2n.controller;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.SiteKeyChecker;
import nl.d2n.service.ExternalApplicationAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class DomainTableControllerTest extends SpringContextTestCase {

    @Autowired
    private DomainTableController domainTableController;

    @Autowired
    ExternalApplicationAuthenticator authenticator;

    @Before
    public void insertTestKeys() {
        Properties properties = new Properties();
        properties.setProperty(SiteKeyChecker.EXTERNAL_APPLICATION_PREFIX+".wiki", "CAFEBABE");
        SiteKeyChecker checker = new SiteKeyChecker(properties);
        authenticator.setSiteKeyChecker(checker);
    }

    @Test
    public void getDistinctions() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        domainTableController.getDistinctions(response, "CAFEBABE");
        assertEquals("{}", response.getContentAsString());
    }

    @Test
    public void getItems() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        domainTableController.getItems(response, "CAFEBABE");
        assertEquals("{}", response.getContentAsString());
    }

    @Test
    public void getItemImagesById() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        domainTableController.getItemIdsByImageName(response);
        assertEquals("{}", response.getContentAsString());
    }

    @Test
    public void getInsideBuildings() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        domainTableController.getInsideBuildings(response, "CAFEBABE");
        assertEquals("{}", response.getContentAsString());
    }

    @Test
    public void getOutsideBuildings() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        domainTableController.getOutsideBuildings(response, "CAFEBABE");
        assertEquals("{}", response.getContentAsString());
    }
}
