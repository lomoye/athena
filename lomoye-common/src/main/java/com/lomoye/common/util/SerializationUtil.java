package com.lomoye.common.util;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerializationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtil.class);

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();

    public static final Gson longDateGson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).setDateFormat(DateFormat.LONG).create();

    public static JsonObject string2JsObj(String jsonStr) {
        return gson.fromJson(jsonStr, JsonElement.class).getAsJsonObject();
    }

    public static String gson2String(Object object) {
        return gson.toJson(object);
    }

    /**
     * new TypeToken<Collection<Integer>>(){}.getType();
     **/
    public static <T> T gson2Object(String jsonString, Type type) {
        try {
            return gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            LOGGER.warn("Gson string to object error, string : {} to object type {}", jsonString, type);
            throw e;
        }
    }

    public static <T> T gson2Object(String jsonString, Class<T> clazz) {
        try {
            return gson.fromJson(jsonString, clazz);
        } catch (JsonSyntaxException e) {
            LOGGER.warn("Gson string to object error, string : {} to object type {}", jsonString, clazz.getCanonicalName());
            throw e;
        }
    }
//
//	public static String object2xml(Object object) {
//		try {
//			xstream.setMode(XStream.NO_REFERENCES);
//			return xstream.toXML(object);
//		} catch (XStreamException e) {
//			LOGGER.warn("xstream object to string error, object {}", object.toString());
//			throw new CDBException(CDBErrors.INTERNAL_SERVER_ERROR,e);
//		}
//	}
//
//	public static Object xml2Object(String xmlString) {
//		try {
//			xstream.setMode(XStream.NO_REFERENCES);
//			return xstream.fromXML(xmlString);
//		} catch (XStreamException e) {
//			LOGGER.warn("xstream string to object error, String {}", xmlString);
//			throw new CDBException(CDBErrors.INTERNAL_SERVER_ERROR,e);
//		}
//	}

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// "yyyy-MM-dd"
        String date_ = sdf.format(date);
        return date_;
    }
}
