package com.ls.luava.config;

import com.ls.luava.common.Types2;

import java.util.Optional;

public class Properties2 extends SafeProperties{

    public <T> Optional<T> getValues(String key, Class<T> targetClass) {
        return Types2.cast(getProperty(key), targetClass);
    }

    public  Optional<String> getString(String key) {
        return Types2.cast(getProperty(key), String.class);
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

    public void setValue(String key, Object value){
        this.put(key,value);
    }

    public void setValue(String key, Object value, String line){
        this.put(key, value, line);
    }

}
