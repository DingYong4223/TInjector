package com.example.injector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tenpay.injector.Injector;
import com.tenpay.injector.TBinding;


public class TestActivity extends Activity {

    @TBinding(value = R2.id.root_layout)
    private View rootLayout;

    @TBinding(value = R2.id.my_test, visiable = View.GONE, parent = {R2.id.root_layout, R2.id.root_layout_sub1})
    private TextView m1MyTest;

    @TBinding(value = R2.id.my_test, visiable = View.GONE, parent = {R2.id.root_layout, R2.id.root_layout_sub2})
    private TextView m2MyTest;

    @TBinding(value = R2.id.my_test, parent = {R2.id.root_layout_sub3})
    private TextView m3MyTest;

    @TBinding(value = R2.id.my_test)
    private TextView oneField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Injector.inject(this);
        m1MyTest.setText("我爱北京天安门");

        m2MyTest.setText("伟大领袖毛主席");

        m3MyTest.setText("五星红旗阴风飘扬");
    }

}
