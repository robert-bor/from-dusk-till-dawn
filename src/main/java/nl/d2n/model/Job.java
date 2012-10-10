package nl.d2n.model;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Job {

    // http://data.die2nite.com/gfx/icons/item_[IMAGE_NAME].gif

    @XmlEnumValue("collec")
    SCAVENGER("pelle", "Scavenger"),
    @XmlEnumValue("basic")
    CITIZEN("basic_suit", "Resident"),
    @XmlEnumValue("guardian")
    GUARDIAN("shield", "Guardian"),
    @XmlEnumValue("eclair")
    SCOUT("vest_on", "Scout"),
    @XmlEnumValue("shaman")
    SHAMAN("shaman", "Shaman"),
    @XmlEnumValue("tamer")
    TAMER("tamed_pet", "Tamer"),
    @XmlEnumValue("tech")
    TECHNICIAN("tech", "Technician"),
    @XmlEnumValue("hunter")
    SURVIVALIST("hunter", "Survivalist"),
    UNKNOWN("", "Unknown");

    private String image;
    private String jobText;

    private Job(final String image, final String jobText) {
        this.image = image;
        this.jobText = jobText;
    }

    public String getImage() {
        return this.image;
    }
    public String getJobText() {
        return this.jobText;
    }
}
