package com.appdynamics.tools.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abey.tom on 10/19/16.
 */
public class Mapper {

    public static <K, V> Map<K, V> toMap(V[] values, KeyGetter<K, V> keyGetter) {
        if (values != null) {
            Map<K, V> map = new HashMap<K, V>();
            for (V value : values) {
                map.put(keyGetter.getKey(value), value);
            }
            return map;
        } else {
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(Collection<V> values, KeyGetter<K, V> keyGetter) {
        if (values != null) {
            Map<K, V> map = new HashMap<K, V>();
            for (V value : values) {
                map.put(keyGetter.getKey(value), value);
            }
            return map;
        }
        return Collections.emptyMap();
    }

    public static <K,V> Map<K,V> toMap(ArrayNode values, KeyValueMapper<JsonNode> mapper) {
        if (values != null) {
            Map<K, V> map = new HashMap<K,V>();
            for (JsonNode value : values) {
                mapper.put(value, map);
            }
            return map;
        } else {
            return Collections.emptyMap();
        }
    }



    public interface KeyGetter<K, V> {
        K getKey(V value);
    }

    public interface KeyValueMapper<T> {
        void put(T value, Map map);
    }
}
