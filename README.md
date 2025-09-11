# TInjector

## <a target=_blank href="https://git.code.oa.com/QwalletAndroid/TInjector">项目介绍</a>
	TInjector是一款代码注入框架，在不影响源码的情况下实现代码注入，其主要目的是帮助开发人员简化代码编写，提高效率。框架将java注解和代码注入结合，用注解代替真实意图从而简化编码复杂度，框架主要由下面几部分组成：
	TKeeping：用于防止类/方法被混淆，凡是被该注解标识的类/方法，都不会被混淆。
	TBinding：Android View对象与ID绑定的框架，实现java层对象与layout中id绑定，简化开发使用findViewById方法调用。
	TLogger：简化Log打印的框架，标注了该注解的方法，将打印方法入参和返回值，方便在debug模式下定位分析问题。
	...


## <a target=_blank href="https://git.code.oa.com/QwalletAndroid/TInjector">快速上手</a>
### TKeeping注解框架
#### Library工程接入流程

    1、在build.gradle文件中，添加gradle插件
        apply plugin: 'com.tenpay.plugin'

    2、在根目录build.gradle文件中，加入如下依赖：
        implementation 'upload:injector-annotation:0.0.7'
        annotationProcessor 'upload:injector-compiler:1.0.2'
        implementation 'androidx.annotation:annotation:1.1.0'

    3、重写setContentView方法，加入Injector.inject(this);，例：
        @Override
        public void setContentView(View view) {
            super.setContentView(view);
            Injector.inject(this);
        }

    4、在java代码中，通过如下代码实现对象与id的绑定即可
        @TBinding(TR.id.ivTitleName) private TextView mTitle;

#### Application工程接入流程
    1、在根目录build.gradle文件中，加入如下依赖：
        implementation 'upload:injector-annotation:0.0.7'
        annotationProcessor 'upload:injector-compiler:1.0.2'
        implementation 'androidx.annotation:annotation:1.1.0'

    2、重写setContentView方法，加入Injector.inject(this);，例：
        @Override
        public void setContentView(View view) {
            super.setContentView(view);
            Injector.inject(this);
        }

    3、在java代码中，通过如下代码实现对象与id的绑定即可
        @TBinding(R.id.ivTitleName) private TextView mTitle;

### TLogger注解框架

    1、在根目录build.gradle文件中，添加如下插件dependency
    dependencies {
        classpath 'com.tenpay.injector:injector-logger:0.0.5'
        //classpath 'upload:injector-logger:0.0.5'
    }

    2、在根目录build.gradle文件中，加入如下插件依赖
        apply plugin: 'injector-logger'
        InjectorExt {
            variant = 'DEBUG'
        }
    3、在java代码中，添加方法注解，例：
        @TLogger
        private int parameterMethod(boolean boolV, byte byteV, char charV, short shortV,
                                    int intV, long longV, float floatV, double doubleV,
                                    String stringV, int[] arr, Bundle savedInstanceState) {
            int insideLocal = 5;
            int insideLocal2 = 6;
            Log.i(TAG, "insideLocal " + insideLocal);
            return insideLocal + insideLocal2;
        }
    4、在日志中搜索如下关键字过滤打印：
        String MARK_START = "-> ";

## 常见问题
### <a target=_blank href="https://git.code.oa.com/QwalletAndroid/TInjector/tree/master/injector-runtime">TBinding</a>

	1、普通用法，实现java对象与资源id的绑定（根据在library和Application中，使用不同的R类），如：
	    Library中：@TBinding(TR.id.ivTitleName) private TextView mTitle;
	    Application中：@TBinding(R.id.ivTitleName) private TextView mTitle;

    2、当一个layout中存在多个相同的id时，可以指定父id，如：
        --父ID只有一个，数组parent中填父id即可
        @TBinding(value = R.id.my_test, parent = {R.id.root_layout_sub3})
        private TextView m3MyTest;

        --父ID也存在同值的情况时，需要依次指定多个父ID，如：
        @TBinding(value = R.id.my_test, parent = {R.id.root_layout, R.id.root_layout_sub2})
        private TextView m2MyTest;

    3、设置组件的可见属性：
        @TBinding(value = R.id.my_test, visiable = View.GONE)
        private TextView m2MyTest;

### <a target=_blank href="https://git.code.oa.com/QwalletAndroid/TInjector/tree/master/injector-logger">TLogger</a>

	1、TLogger注解可作用于各类函数，包括：有参、无参、有返回值、无返回值的情况

	2、TLogger默认使用DefLoggerImpl类作为日志打印，如需自定义日志打印，可以实现com.tenpay.injector.ILogger接口，并在程序入口调用如下代码实现注册：
	    TMLogger.register(TestLogger.class);
