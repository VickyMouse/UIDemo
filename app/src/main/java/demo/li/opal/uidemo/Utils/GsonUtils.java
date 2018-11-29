package demo.li.opal.uidemo.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {

    private static final String TAG = GsonUtils.class.getName();

    private static Gson gson;

    static {
        gson = new GsonBuilder().create();
    }

    /**
     * json to object
     */
    public static <T> T json2Obj(Gson gson, String json, Type cls) {
        T obj = null;
        try {
            obj = gson.fromJson(json, cls);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return obj;
    }

    public static <T> T json2Obj(String json, Type cls) {
        T obj = null;
        try {
            obj = gson.fromJson(json, cls);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return obj;
    }

    public static <T> T json2Obj(Gson gson, String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            LogUtils.e(TAG, e);
        }
        return null;
    }

    public static <T> T json2Obj(String json, Class<T> clazz) {
        return json2Obj(gson, json, clazz);
    }


    /**
     * object to json
     */
    public static <T> String obj2Json(Gson gson, T obj) {
        String json = null;
        try {
            json = gson.toJson(obj);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return json;
    }

    public static <T> String obj2Json(T obj) {
        return obj2Json(gson, obj);
    }

    public static <T> String obj2Json(Gson gson, T obj, Type tTYpe) {
        String json = null;
        try {
            json = gson.toJson(obj, tTYpe);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return json;
    }

    public static <T> String obj2Json(T obj, Type tTYpe) {
        return obj2Json(gson, obj, tTYpe);
    }

    /**
     * json to object list
     */
    public static <T> List<T> json2ObjList(Gson gson, String jsonString, Class<T> clazz) {
        List<T> list = null;
        try {
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = jsonParser.parse(jsonString).getAsJsonArray();
            list = new ArrayList<>();
            for (int i = 0, size = jsonArray.size(); i < size; i++) {
                list.add(gson.<T>fromJson(jsonArray.get(i), clazz));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return list;
    }

    public static <T> List<T> json2ObjList(String jsonString, Class<T> clazz) {
        return json2ObjList(gson, jsonString, clazz);
    }

    /**
     * object list to json
     */
    public static <T> String objList2Json(Gson gson, List<T> list) {
        String json = null;
        try {
            json = gson.toJson(list);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return json;
    }

    public static <T> String objList2Json(List<T> list) {
        return objList2Json(gson, list);
    }

    // XML.toJSONObject转出来的JSON,如果一个父节点只有一个孩子节点的话,转出来的是Object,但有时期望的是array
    public static void confirmValueIsArray(JsonObject fatherNode, String key) {
        if (fatherNode == null || key == null) {
            return;
        }
        JsonElement element = fatherNode.get(key);
        if (element == null) {
            return;
        }
        if (element.isJsonArray()) {
            return;
        }
        JsonArray array = new JsonArray();
        array.add(element);
        fatherNode.remove(key);
        fatherNode.add(key, array);
    }
}
