package com.ls.luava.config;

import com.ls.luava.common.Types2;

import java.util.Optional;

public class Properties2 extends SafeProperties{

    public <T> Optional<T> getValues(String key, Class<T> targetClass) {
        return Types2.cast(getProperty(key), targetClass);
    }

    public  Optional<Integer> getInt(String key) {
        return Types2.cast(getProperty(key), Integer.class);
    }

    public  Optional<Long> getLong(String key) {
        return Types2.cast(getProperty(key), Long.class);
    }

    public  Optional<Double> getDouble(String key) {
        return Types2.cast(getProperty(key), Double.class);
    }

    public  Optional<Float> getFloat(String key) {
        return Types2.cast(getProperty(key), Float.class);
    }

    public  Optional<Boolean> getBoolean(String key) {
        return Types2.cast(getProperty(key), Boolean.class);
    }


}
