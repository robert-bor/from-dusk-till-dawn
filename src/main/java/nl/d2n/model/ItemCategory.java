package nl.d2n.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum ItemCategory {
    @XmlEnumValue("Rsc")
    RESOURCES("Resources"),
    @XmlEnumValue("Weapon")
    ARMOURY("Armoury"),
    @XmlEnumValue("Box")
    CONTAINERS_AND_BOXES("Containers and boxes"),
    @XmlEnumValue("Armor")
    DEFENSES("Defenses"),
    @XmlEnumValue("Food")
    FOOD("Food"),
    @XmlEnumValue("Furniture")
    FURNITURE("Furniture"),
    @XmlEnumValue("Drug")
    PHARMACY("Pharmacy"),
    @XmlEnumValue("Misc")
    MISCELLANEOUS("Miscellaneous");

    private String name;

    private ItemCategory(final String name) {
        this.name  = name;
    }
    public String getName() { return this.name; }
}
