package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ExternalApplicationTest {

    @Test
    public void getApp() {
        assertEquals(ExternalApplication.ATLAS, ExternalApplication.findApplicationForName("atlas"));
        assertEquals(ExternalApplication.EXTERNAL_MAP, ExternalApplication.findApplicationForName("external_map"));
        assertEquals(ExternalApplication.NITELIGHT, ExternalApplication.findApplicationForName("nitelight"));
        assertEquals(ExternalApplication.OVAL_OFFICE, ExternalApplication.findApplicationForName("oval_office"));
        assertEquals(ExternalApplication.WIKI, ExternalApplication.findApplicationForName("wiki"));
        assertEquals(ExternalApplication.WASTELAND_CARTOGRAPHER, ExternalApplication.findApplicationForName("cartographer"));
        assertEquals(ExternalApplication.MAP_VIEWER, ExternalApplication.findApplicationForName("map_viewer"));
    }
}
