package com.tenpay.injector;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 调用生成的Bind文件
 */
public class Injector {

    /**
     * 调用生成的Bind文件
     */
    public static void inject(Activity activity) {
        String clsName = activity.getClass().getSimpleName() + "Binding";
        String pkg = activity.getClass().getPackage().getName();
        try {
            Class cls = Class.forName(String.format("%s.%s", pkg, clsName));
            Constructor conn = cls.getConstructor(Activity.class);
            Object obj = conn.newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
