package nl.d2n.model;

public enum ExternalApplication {
    ATLAS("atlas"),
    EXTERNAL_MAP("external_map"),
    NITELIGHT("nitelight"),
    OVAL_OFFICE("oval_office"),
    WIKI("wiki"),
    WASTELAND_CARTOGRAPHER("cartographer"),
    MAP_VIEWER("map_viewer");

    private String keyName;

    private ExternalApplication(String keyName) {
        this.keyName = keyName;
    }
    public String getKeyName() { return this.keyName; }
    public static ExternalApplication findApplicationForName(String appName) {
        for (ExternalApplication externalApplication : values()) {
            if (externalApplication.getKeyName().equals(appName)) {
                return externalApplication;
            }
        }
        return null;
    }
}
