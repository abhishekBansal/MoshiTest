# MoshiTest
This is a sample trying to make `PolymorphicJsonAdapterFactory` from Moshi and `SkipBadElementsListAdapter` from this stackoverflow answer https://stackoverflow.com/a/54190660/1107755

This sample reproduces following crash stack

```
 java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.moshitest/com.example.moshitest.MainActivity}: java.lang.IllegalArgumentException: No JsonAdapter for E (with no annotations)
    for E
    for interface java.util.List
    for class java.lang.Object
    for class com.example.moshitest.Tyre
    for java.util.List<com.example.moshitest.Tyre> tyres
    for class com.example.moshitest.Car
    for class com.example.moshitest.Vehicle
    for java.util.List<com.example.moshitest.Vehicle>
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2951)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3086)
        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:78)
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1816)
        at android.os.Handler.dispatchMessage(Handler.java:106)
        at android.os.Looper.loop(Looper.java:193)
        at android.app.ActivityThread.main(ActivityThread.java:6718)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
     Caused by: java.lang.IllegalArgumentException: No JsonAdapter for E (with no annotations)
    for E
    for interface java.util.List
    for class java.lang.Object
    for class com.example.moshitest.Tyre
    for java.util.List<com.example.moshitest.Tyre> tyres
    for class com.example.moshitest.Car
    for class com.example.moshitest.Vehicle
    for java.util.List<com.example.moshitest.Vehicle>
        at com.squareup.moshi.Moshi$LookupChain.exceptionWithLookupStack(Moshi.java:348)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:149)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:67)
        at com.example.moshitest.MainActivity$SkipBadElementsListAdapter$Factory.create(MainActivity.kt:62)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:71)
        at com.squareup.moshi.StandardJsonAdapters$ObjectJsonAdapter.<init>(StandardJsonAdapters.java:288)
        at com.squareup.moshi.StandardJsonAdapters$1.create(StandardJsonAdapters.java:56)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:71)
        at com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory.create(PolymorphicJsonAdapterFactory.java:165)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:67)
        at com.example.moshitest.MainActivity$SkipBadElementsListAdapter$Factory.create(MainActivity.kt:62)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.example.moshitest.CarJsonAdapter.<init>(CarJsonAdapter.kt:21)
        at java.lang.reflect.Constructor.newInstance0(Native Method)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:343)
        at com.squareup.moshi.internal.Util.generatedAdapter(Util.java:478)
        at com.squareup.moshi.StandardJsonAdapters$1.create(StandardJsonAdapters.java:60)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:67)
        at com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory.create(PolymorphicJsonAdapterFactory.java:162)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:67)
        at com.example.moshitest.MainActivity$SkipBadElementsListAdapter$Factory.create(MainActivity.kt:62)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:137)
2019-06-13 18:59:11.606 24022-24022/? E/AndroidRuntime:     at com.squareup.moshi.Moshi.adapter(Moshi.java:97)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:67)
        at com.example.moshitest.MainActivity.onCreate(MainActivity.kt:34)
        at android.app.Activity.performCreate(Activity.java:7144)
        at android.app.Activity.performCreate(Activity.java:7135)
        at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1271)
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2931)
        	... 11 more
     Caused by: java.lang.IllegalArgumentException: No JsonAdapter for E (with no annotations)
        at com.squareup.moshi.Moshi.adapter(Moshi.java:147)
        	... 49 more
          ```
          
