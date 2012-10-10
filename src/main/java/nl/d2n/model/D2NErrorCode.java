package nl.d2n.model;

import nl.d2n.util.GsonUtil;

import javax.xml.bind.annotation.XmlEnumValue;
import java.util.Map;
import java.util.TreeMap;

public enum D2NErrorCode {
    BUILDING_DOES_NOT_EXIST("That building does not exist"),
    CITIZEN_SHUNNED("The Shunned are not allowed to update"),
    CITIZEN_NOT_OUTSIDE("You must be outside to update your zone", false, true),
    CITY_DOES_NOT_EXIST("That town does not exist"),
    COULD_NOT_ACCESS_SITE("The D2N site could not be accessed", true),
    COULD_NOT_PARSE_XML("The XML could not be parsed properly", true),
    CHAOS_MODE("Your town is in chaos!"),
    CORRIDOR_ALREADY_EXISTS("The corridor already exists"),
    CORRIDOR_MAY_NOT_BE_REMOVED("The corridor may not be removed"),
    EXTERNAL_APPLICATION_NOT_ALLOWED_ACCESS("The external application is not allowed access to this method"),
    HARD_TOWN("Your town is a hard town -- forget it!", false, true),
    ILLEGAL_COORDINATES("The coordinates are illegal"),
    ILLEGAL_ZOMBIE_AMOUNT("The amount you entered is illegal"),
    KEY_NOT_SECURE("Your D2N key does not allow updates"),
    MISSING_PARAMETER("Missing parameter: "),
    NO_DOOR_ALLOWED_IN_ENTRANCE("Door not allowed here"),
    NO_KEY("A key must be passed"),
    NOT_IN_TOWN("You are no longer in this town"),
    OVAL_OFFICE_ERROR("Oval Office error: "),
    PEEK_TEXT_TOO_LONG("Text must be less than 100 chars"),
    ROOM_ALREADY_EXISTS("The room already exists"),
    ROOM_DOES_NOT_EXIST("The room does not exist"),
    ROOM_HAS_NO_ADJOINING_ROOMS("The new room has no adjoining rooms"),
    ROOM_MAY_NOT_BE_DELETED("Room may not be deleted, because other rooms depend on it"),
    SYSTEM_ERROR("Something went wrong - BerZerg's on it!"),
    TAMPERED_KEY("Stop frolicking with keys!"),
    UNKNOWN_SITE_KEY("Site key not known"),
    UNSUPPORTED_OPERATION("Something went wrong - BerZerg's on it!"),
    USER_BANNED("You have been banned"),
    WRONG_VALUE("Wrong value: "),
    YOU_ARE_DEAD("You are dead"),
    ZONE_DOES_NOT_EXIST("Zone does not exist"),
    @XmlEnumValue("xml_not_found")
    XML_NOT_FOUND("Your XML could not be found"),
    @XmlEnumValue("version_check")
    VERSION_CHECK("D2N is undergoing a version upgrade", true),
    @XmlEnumValue("horde_attacking")
    HORDE_ATTACKING("The horde is currently attacking", true),
    @XmlEnumValue("user_not_found")
    USER_NOT_FOUND("You do not exist in the D2N database"),
    @XmlEnumValue("not_in_game")
    NOT_IN_GAME("You are currently not in a game", false, true),
    @XmlEnumValue("invalid_keys")
    INVALID_KEYS("The site key is invalid for you");

    private String message;
    private boolean allowCacheRead;
    private boolean skipLogging;

    private D2NErrorCode(String message) {
        this(message, false);
    }
    private D2NErrorCode(String message, boolean allowCacheRead) {
        this(message, allowCacheRead, false);
    }
    private D2NErrorCode(String message, boolean allowCacheRead, boolean skipLogging) {
        this.message = message;
        this.allowCacheRead = allowCacheRead;
        this.skipLogging = skipLogging;
    }
    public String getMessage() { return this.message; }
    public boolean isAllowCacheRead() { return this.allowCacheRead; }
    public boolean isSkipLogging() { return this.skipLogging; }

    public String toJson() {
        return toJson(null);
    }
    public String toJson(String argument) {
        Map<String,String> error = new TreeMap<String,String>();
        error.put("errorCode", toString());
        error.put("errorMessage", getMessage()+(argument == null ? "" : argument));
        return GsonUtil.objectToJson(error);
    }
}
