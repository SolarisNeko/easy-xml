package com.neko233.easyxml.utils;

import java.util.Collection;

/**
 * @author SolarisNeko
 * Date on 2023-11-19
 */
public class EasyXmlCollectionUtils {


    public static <T> boolean isEmpty(Collection<T> data) {
        return data == null || data.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> data) {
        return !isEmpty(data);
    }
}
