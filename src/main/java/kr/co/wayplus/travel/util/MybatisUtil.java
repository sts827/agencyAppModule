package kr.co.wayplus.travel.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class MybatisUtil {

    public static boolean isEmpty(Object object){
        if(object instanceof String) return object == null || "".equals(object.toString().trim());
        else if(object instanceof List) return object == null || ((List)object).isEmpty();
        else if(object instanceof Map) return object == null || ((Map)object).isEmpty();
        else if(object instanceof Object[]) return object == null || Array.getLength(object) == 0;
        else return object == null;
    }

    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }
}
