package nl.d2n.model;

import java.util.Map;
import java.util.TreeMap;

public enum CampingTopology {
    L1_SUICIDE       ("L1", "Sleeping somewhere like this is basically a form of suicide, it's that simple. You'll see...", true),
    L2_SHORTAGE      ("L2", "There's a distinct shortage of shelter here. You feel a little bit exposed.", true, 10),
    L3_MINIMAL       ("L3", "This zone offers nothing more than minimal \"natural\" protection. You'll just need to make do with what comes to hand.", true),
    L4_HIDING_PLACE  ("L4", "After a quick look around, it looks like you could find a good hiding place here.", true),
    L5_FEW_HIDEOUTS  ("L5", "For those looking to spend the night, there are a few hideouts in this zone...", true),
    L6_TOP_HIDEOUTS  ("L6", "If required, there are some top-notch hideouts here...", true),
    L7_IDEAL         ("L7", "This seems like the ideal place to spend the night, as it looks like there is no shortage of hiding places.", true),
    UNKNOWN          ("L?", "The camping topology for this zone is unknown", false);

    private String level;
    private String text;
    private Boolean comparable;
    private Integer startText;

    private CampingTopology(String level, String text, Boolean comparable, Integer startText) {
        this.level = level;
        this.text = text;
        this.comparable = comparable;
        this.startText = startText;
    }
    private CampingTopology(String level, String text, Boolean comparable) {
        this(level, text, comparable, 0);
    }

    public String getLevel() { return this.level; }
    public String getText() { return this.text; }
    public Boolean isComparable() { return this.comparable; }
    public Integer getStartText() { return this.startText; }
    
    public String getDescription() {
        return getLevel() + " - " + getText();
    }
    
    static public Map<String, CampingTopology> getTopologyByStartText() {
        Map<String, CampingTopology> topologies = new TreeMap<String, CampingTopology>();
        for (CampingTopology campingTopology : values()) {
            if (campingTopology.isComparable()) {
                topologies.put(
                        campingTopology.getText()
                                .substring(campingTopology.getStartText(), 20 + campingTopology.getStartText()),
                        campingTopology);
            }
        }
        return topologies;
    }
    
    static public Map<CampingTopology, String> getCampingTopologyMap() {
        Map<CampingTopology, String> campingTopologyMap = new TreeMap<CampingTopology, String>();
        for (CampingTopology campingTopology : values()) {
            campingTopologyMap.put(campingTopology, campingTopology.getDescription());
        }
        return campingTopologyMap;
    }
}
