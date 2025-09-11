package com.example.injector;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static int getIntField(Object object, String fidld) {
        Class clazz = object.getClass();
        try {
            Field mFiled = clazz.getDeclaredField(fidld);
            mFiled.setAccessible(true);
            return mFiled.getInt(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    public static Object getObjField(Object object, String fidld) {
        Class clazz = object.getClass();
        try {
            Field mFiled = clazz.getDeclaredField(fidld);
            mFiled.setAccessible(true);
            return mFiled.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return null;
    }

}
