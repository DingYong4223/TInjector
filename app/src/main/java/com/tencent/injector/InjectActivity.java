package com.tencent.injector;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tenpay.injector.Injector;
import com.tenpay.injector.TMLogger;
import com.tenpay.injector.TBinding;
import com.tenpay.injector.TLogger;
import com.tenpay.injector.TReporter;

public class InjectActivity extends Activity {

    private static final String TAG = "InjectActivity";

    @TBinding(value = R.id.root_layout)
    private View rootLayout;

    @TBinding(value = R.id.my_test, visiable = View.GONE)
    private TextView m1MyTest;

    @TBinding(value = R.id.my_test, visiable = View.GONE, parent = {R.id.root_layout, R.id.root_layout_sub2})
    private TextView m2MyTest;

    @TBinding(value = R.id.my_test, parent = {R.id.root_layout_sub3})
    private TextView m3MyTest;

    @TBinding(value = R.id.my_test, text = "我爱北京天安门")
    private TextView m4MyTest;

    @TBinding(value = R.id.my_test)
    private TextView oneField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.inject(this);
        m1MyTest.setText("我爱北京天安门");

        m2MyTest.setText("伟大领袖毛主席");

        m3MyTest.setText("五星红旗阴风飘扬");

        injectorLoggerTest(savedInstanceState);
    }

    private void injectorLoggerTest(@Nullable Bundle savedInstanceState) {
        TMLogger.register(TestLogger.class);
        setContentView(R.layout.activity_main);
        boolean bool = true;
        byte byteV = 1;
        char charV = 2;
        short shortV = 3;
        int intValue = 4;
        long longValue = 5;
        float floatValue = 6;
        double doubleV = 7;
        String stringValue = "string";
        int[] intArr = new int[]{1, 2, 3};
        parameterMethod(bool, byteV, charV, shortV, intValue, longValue, floatValue,
                doubleV, stringValue, intArr, savedInstanceState);
        emptyMethod();
        arrayMethod();
        booleanMethod();
        byteMethod();
        charMethod();
        doubleMethod();
        floatMethod();
        intMethod();
        longMethod();
        shortMethod();
        methodStatic("parameter value");
    }

//    @TLogger
//    private int parameterMethod(boolean boolV, byte byteV, char charV, short shortV,
//                                int intV, long longV, float floatV, double doubleV,
//                                String stringV, int[] arr, Bundle savedInstanceState) {
//        int insideLocal = 5;
//        int insideLocal2 = 6;
//        return insideLocal + insideLocal2;
//    }

    @TLogger
    private int parameterMethod(boolean z, byte b, char c, short s, int i, long j, float f, double d,
                                String str, int[] iArr, Bundle savedInstanceState) {
        TMLogger tMLogger = new TMLogger(TAG, "parameterMethod");
        tMLogger.add("boolV", z);
        tMLogger.add("byteV", b);
        tMLogger.add("charV", c);
        tMLogger.add("shortV", s);
        tMLogger.add("intV", i);
        tMLogger.add("longV", j);
        tMLogger.add("floatV", f);
        tMLogger.add("doubleV", d);
        tMLogger.add("stringV", str);
        tMLogger.add("arr", iArr);
        tMLogger.add("savedInstanceState", savedInstanceState);
        tMLogger.log();
        int i2 = 5 + 6;
        TMLogger.ret(TAG, "parameterMethod", System.currentTimeMillis() - System.currentTimeMillis(), i2);
        return i2;
    }


    @TLogger
    private void emptyMethod() {

    }

    @TLogger
    private boolean booleanMethod() {
        return true;
    }

    @TLogger
    private char charMethod() {
        return 'c';
    }

    @TLogger
    private byte byteMethod() {
        return 0x01;
    }

    @TLogger
    private short shortMethod() {
        return 2;
    }

    @TLogger
    private int intMethod() {
        return 2;
    }

    @TLogger
    private long longMethod() {
        return 2L;
    }

    @TLogger
    private double doubleMethod() {
        return 2;
    }

    @TLogger
    private float floatMethod() {
        return 2.0f;
    }

    @TLogger
    private int[] arrayMethod() {
        return new int[]{1, 2, 3};
    }

    @TLogger
    private static Object methodStatic(String str) {
        return "object string" + str;
    }

    private static Object normanMethod(String str) {
        return "normanMethod string" + str;
    }

    @TReporter(value = "user_click_req")
    private static String bntClick(String str) {
        return "normanMethod string" + str;
    }

}
