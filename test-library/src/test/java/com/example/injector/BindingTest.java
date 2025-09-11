package com.example.injector;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.powermock.*", "org.mockito.*", "org.robolectric.*", "android.*", "javax.xml.*",
        "com.sun.org.apache.xerces.*"})
@Config(manifest = "AndroidManifest.xml", application = Application.class, sdk = 21)
public class BindingTest {

    @Before
    public void setUp() {

    }

    @Test
    public void bindingTest() {
        TestActivity testActivity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
        TextView m1MyTest = (TextView) ReflectUtil.getObjField(testActivity, "m1MyTest");
        Assert.assertEquals("我爱北京天安门", m1MyTest.getText());

        TextView m2MyTest = (TextView) ReflectUtil.getObjField(testActivity, "m2MyTest");
        Assert.assertEquals("伟大领袖毛主席", m2MyTest.getText());

        TextView m3MyTest = (TextView) ReflectUtil.getObjField(testActivity, "m3MyTest");
        Assert.assertEquals("五星红旗阴风飘扬", m3MyTest.getText());
    }

    @Test
    public void bindingParentTest() {
        TestActivity testActivity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
        View rootLayout = (View)ReflectUtil.getObjField(testActivity, "rootLayout");

        TextView m1MyTest = (TextView) ReflectUtil.getObjField(testActivity, "m1MyTest");
        Assert.assertEquals("我爱北京天安门", m1MyTest.getText());
        Assert.assertTrue(rootLayout == m1MyTest.getParent().getParent());
    }

    @Test
    public void bindingVisibleTest() {
        TestActivity testActivity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();

        TextView m1MyTest = (TextView) ReflectUtil.getObjField(testActivity, "m1MyTest");
        Assert.assertEquals("我爱北京天安门", m1MyTest.getText());
        Assert.assertTrue(m1MyTest.getVisibility() == View.GONE);
    }

}