package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlEnumValue;

public enum ZoneTag {

    BLANK                   (0,  ""),
    @XmlEnumValue("1")
    CALL_FOR_HELP           (1,  "Call for help"),
    @XmlEnumValue("2")
    RESOURCES               (2,  "Resources (wood, metal, ...)"),
    @XmlEnumValue("3")
    ITEMS                   (3,  "Item(s)"),
    @XmlEnumValue("4")
    IMPORTANT_OBJECTS       (4,  "Important object(s)"),
    @XmlEnumValue("5")
    DEPLETED                (5,  "Depleted zone"),
    @XmlEnumValue("6")
    TEMPORARILY_SECURED     (6,  "Temporarily secured zone"),
    @XmlEnumValue("7")
    UNCLEARED_ZONE          (7,  "Uncleared zone"),
    @XmlEnumValue("8")
    BETWEEN_5_AND_8_ZOMBIES (8,  "Between 5 and 8 zombies"),
    @XmlEnumValue("9")
    ZOMBIES_9_OR_MORE       (9,  "9 zombies or more!"),
    @XmlEnumValue("10")
    LIKELY_CAMPSITE         (10, "Likely campsite"),
    @XmlEnumValue("11")
    RUIN_TO_EXPLORE         (11, "A ruin to explore");

    @Expose
    final private Integer serial;

    @Expose
    final private String description;

    private ZoneTag(Integer serial, String description) {
        this.serial = serial;
        this.description = description;
    }
    public Integer getSerial() {
        return this.serial;
    }
    public String getDescription() {
        return this.description;
    }
    
}
