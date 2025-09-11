package com.tenpay.injector;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Tenpay Method Logger for, this class will be injected to class file when transform.
 */
public class TMLogger {

    private final StringBuilder result = new StringBuilder();
    private static final String DIVIDER = "|";
    private String tag = "";
    private static ILogger sLogger = new LoggerImpl();

    private static void init(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("tlogger.cfg");
            String s = readTextFile(inputStream);
            //todo parse file by line
            String clsName = getProperties("tlogger.impl");
            if (!TextUtils.isEmpty(clsName)) {
                try {
                    Class clazz = Class.forName(clsName);
                    sLogger = (ILogger)clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
    }

    private static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    private static String getProperties(String key) {
        //for test
        return "com.delan.test.TestLogger";
    }

    /**
     * register self defined logger implement.
     *
     * @param clazz the Class which implement ILogger interface.
     */
    public static void register(Class<? extends ILogger> clazz) {
        try {
            sLogger = clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public TMLogger(String tag, String methodName) {
        this.tag = tag;
        result.append(sLogger.getStartMark()).append(methodName).append("[");
    }

    public TMLogger format(String name, Object val) {
        if (result.length() > 0) {
            result.append(DIVIDER);
        }
        result.append(String.format("%s=%s", name, val));
        return this;
    }

    public TMLogger add(String name, int val) {
        return format(name, val);
    }

    public TMLogger add(String name, boolean val) {
        return format(name, val);
    }

    public TMLogger add(String name, short val) {
        return format(name, val);
    }

    public TMLogger add(String name, byte val) {
        return format(name, val);
    }

    public TMLogger add(String name, char val) {
        return format(name, val);
    }

    public TMLogger add(String name, long val) {
        return format(name, val);
    }

    public TMLogger add(String name, double val) {
        return format(name, val);
    }

    public TMLogger add(String name, float val) {
        return format(name, val);
    }

    public TMLogger add(String name, Object val) {
        if (result.length() > 0) {
            result.append(DIVIDER);
        }
        if (val != null && val.getClass().isArray()) {
            result.append(String.format("%s=%s", name, arrayToString(val)));
        } else {
            result.append(String.format("%s=%s", name, val));
        }
        return this;
    }

    public void log() {
        result.append("]");
        sLogger.log(tag, result);
    }

    /********************************print result****************************************/

    public static void ret(String className, String methodName, long cost, byte retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, char retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, short retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, int retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, boolean retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, long retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, float retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, double retValue) {
        sLogger.ret(className, methodName, cost, retValue);
    }

    public static void ret(String className, String methodName, long cost, Object retValue) {
        if (retValue != null && retValue.getClass().isArray()) {
            sLogger.ret(className, methodName, cost, arrayToString(retValue));
        } else {
            sLogger.ret(className, methodName, cost, retValue);
        }
    }

    /**
     * convert array to string.
     */
    protected static String arrayToString(Object val) {
        if (!(val instanceof Object[])) {
            if (val instanceof int[]) {
                return Arrays.toString((int[]) val);
            } else if (val instanceof char[]) {
                return Arrays.toString((char[]) val);
            } else if (val instanceof boolean[]) {
                return Arrays.toString((boolean[]) val);
            } else if (val instanceof byte[]) {
                return Arrays.toString((byte[]) val);
            } else if (val instanceof long[]) {
                return Arrays.toString((long[]) val);
            } else if (val instanceof double[]) {
                return Arrays.toString((double[]) val);
            } else if (val instanceof float[]) {
                return Arrays.toString((float[]) val);
            } else if (val instanceof short[]) {
                return Arrays.toString((short[]) val);
            } else {
                return Arrays.toString((String[]) val);
            }
        } else {
            return Arrays.deepToString((Object[]) val);
        }
    }

}
