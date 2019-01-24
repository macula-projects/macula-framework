package org.macula.boot.core.utils;

import java.util.UUID;

public class BatchNoUtils {
    public static String gen() {
        return UUID.randomUUID().toString();
    }
}
