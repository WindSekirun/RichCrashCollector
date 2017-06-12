## RichCrashCollector
Auto write crash-report documents (markdown) when app is force closed.

### Features
* Auto write crash-report documents
* Generate code block of Stacktrace
* Customize options to produce crash-report documents
* Lightweight(11KB) library
* Customize format of crash-report documents (Now support Markdown, HTML)

### Sample
Provide 'sample' module

### Usages
1. Download (or clone) project, copy rich-crash-collector.aar located /rich-crash-collector/aars file into /your_project_folder/app/arrs
2. add repositories on module build.gradle
```` 
 allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
   } 
````
3. add compile on module build.gradle
````
    compile 'com.github.PyxisDev:RichCrashCollector:1.1.0'
````
4. Initialize CrashCollector in Application class 
````
   CrashCollector.initCrashCollector(this);
````
   or
````
    CrashConfig config = new CrashConfig.Builder()
                .setDisplayDeviceInfo(true)
                .setLogLevel(LogLevel.STACKTRACE)
                .setLogLocation(getExternalFilesDir("crash").toString())
                .setPackageName(getPackageName())
                .setTimeFormat("yyyy-MM-dd (E) a hh:mm:ss.SSS")
                .setVersionStr(VersionStrUtils.getVersionStr(this))
                .build(this);

    CrashCollector.initCrashCollector(this, config);
````
5. That's it! Crash log will saved in LogLocation.

### Document format
* Markdown
* HTML (v 1.1.0 available)

##### Example
````
## Crash Log in com.github.windsekirun.richcrashcollector.sample

### Application Info
* Package Name: **com.github.windsekirun.richcrashcollector.sample**
* Version: **1.0(1)**

### Device Info
* Device: **SM-G920K (a.k.a zerofltektt or zerofltektt)**
* Version: **23 (23)**
* Manufacturer: **samsung**

### Crash Info
* When: **2017-03-17 (금) 오후 05:32:30.881**
* Message: **test**
* Localized Message: **test**

#### Stack Trace
java.lang.ArrayIndexOutOfBoundsException: test
	at com.github.windsekirun.richcrashcollector.sample.MainActivity$5.onClick(MainActivity.java:59)
	at android.view.View.performClick(View.java:5702)
	at android.widget.TextView.performClick(TextView.java:10888)
	at android.view.View$PerformClick.run(View.java:22541)
	at android.os.Handler.handleCallback(Handler.java:739)
	at android.os.Handler.dispatchMessage(Handler.java:95)
	at android.os.Looper.loop(Looper.java:158)
	at android.app.ActivityThread.main(ActivityThread.java:7229)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1230)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1120)
````

### License
MIT License
