package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlEnumValue;
import java.util.Map;
import java.util.TreeMap;

public enum ZoneDanger {

    UNKNOWN      ("unknown", "unknown", "",   false, -1, -1),
    @XmlEnumValue("0")
    NONE         ("danger0", "0",       "",   false,  0,  0),
    @XmlEnumValue("1")
    ONE_TO_THREE ("danger1", "1-3",     "3-", true,   1,  2),
    @XmlEnumValue("2")
    THREE_TO_FIVE("danger2", "2-5",     "5-", true,   3,  4),
    @XmlEnumValue("3")
    FIVE_PLUS    ("danger3", "4+",      "4+", true,   5,  8),
    @XmlEnumValue("4")
    NINE_PLUS    ("danger4", "9+",      "9+", true,   9,  null);

    private String classTag;
    private String zedAmount;
    private String twoDigitRange;
    private boolean showTwoDigitRange;
    private Integer lowTreshold;
    private Integer highTreshold;

    private ZoneDanger(final String classTag, final String zedAmount, final String twoDigitRange,
                       final boolean showTwoDigitRange, Integer lowTreshold, Integer highTreshold) {
        this.classTag = classTag;
        this.zedAmount = zedAmount;
        this.twoDigitRange = twoDigitRange;
        this.showTwoDigitRange = showTwoDigitRange;
        this.lowTreshold = lowTreshold;
        this.highTreshold = highTreshold;
    }
    public String getClassTag() { return this.classTag; }
    public String getZedAmount() { return this.zedAmount; }
    public String getTwoDigitRange() { return this.twoDigitRange; }
    public boolean isShowTwoDigitRange() { return this.showTwoDigitRange; }
    public Integer getLowTreshold() { return this.lowTreshold; }
    public Integer getHighTreshold() { return this.highTreshold; }
    public static ZoneDanger getClassWithZombies(int zombies) {
        for (ZoneDanger danger : ZoneDanger.values()) {
            if (    danger.getLowTreshold() <= zombies &&
                    (danger.getHighTreshold() == null || danger.getHighTreshold() >= zombies)) {
                return danger;
            }
        }
        return ZoneDanger.UNKNOWN;
    }
    // This method exists because GSon does not serialize attributes in an enum
    public static Map<ZoneDanger, Map<String, Object>> getDangersAsMap() {
        Map<ZoneDanger, Map<String, Object>> dangersAsMap = new TreeMap<ZoneDanger, Map<String, Object>>();
        for (ZoneDanger danger : ZoneDanger.values()) {
            Map<String, Object> keyValuePairs = new TreeMap<String, Object>();
            keyValuePairs.put("class", danger.getClassTag());
            keyValuePairs.put("amountNormal", danger.getZedAmount());
            keyValuePairs.put("amountTwoDigits", danger.getTwoDigitRange());
            keyValuePairs.put("showTwoDigits", danger.isShowTwoDigitRange());
            keyValuePairs.put("lowTreshold", danger.getLowTreshold());
            keyValuePairs.put("highTreshold", danger.getHighTreshold());
            dangersAsMap.put(danger, keyValuePairs);
        }
        return dangersAsMap;
    }
}
