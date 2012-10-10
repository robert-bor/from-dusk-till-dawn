package nl.d2n.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.d2n.model.ZoneTag;
import nl.d2n.model.gson.ZoneTagSerializer;

public class GsonUtil {

    public static String objectToJsonNoEscaping(Object object) {
        return createGson(false).toJson(object);
    }
    public static String objectToJson(Object object) {
        return createGson(true).toJson(object);
    }
    protected static Gson createGson(boolean escaping) {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(ZoneTag.class, new ZoneTagSerializer())
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation();
        if (!escaping) {
            builder = builder.disableHtmlEscaping();
        }
        return builder.create();
    }
}
