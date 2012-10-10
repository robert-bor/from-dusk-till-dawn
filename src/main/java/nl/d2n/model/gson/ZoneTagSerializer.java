package nl.d2n.model.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import nl.d2n.model.ZoneTag;

import java.lang.reflect.Type;

public class ZoneTagSerializer implements JsonSerializer<ZoneTag> {
    public JsonElement serialize(ZoneTag zoneTag, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("serial", zoneTag.getSerial());
        obj.addProperty("description", zoneTag.getDescription());
        return obj;
    }
}
