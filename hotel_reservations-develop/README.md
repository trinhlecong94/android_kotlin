# Hotel Reservations App
Back end: https://github.com/trinhlecong94/online-hotel-reservations-backend
## 1. Installing JDK
Before installing Android Studio, make sure you have JDK 6 or higher installed—the JRE alone is not sufficient. When developing for Android 5.0 (API level 21) and higher, you will need to install JDK 7. To check if you have the correct version of the JDK installed, open a terminal and type javac -version. If the JDK is not available or the version is lower than version 6, download the [Java SE Development Kit 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
## 2. [Installing Android Studio](https://developer.android.com/studio?pkg=studio&hl=vi "Installing Android Studio")
Android Studio provides everything you need to start developing apps for Android, including the Android Studio IDE and the Android SDK tools.
### Download Android Studio
http://developer.android.com/sdk/index.html
### Steps to set up Android Studio
- On Windows
  1. Launch the `.exe` file you downloaded.
  2. Follow the setup wizard to install Android Studio and any necessary SDK tools.
     On some Windows systems, the launcher script does not find where the JDK is installed. If you encounter this problem, you need to set an environment variable indicating the correct location.
     Select `Start menu > Computer > System Properties > Advanced System Properties`. Then open `Advanced tab > Environment Variables` and add a new system variable `JAVA_HOME` that points to your JDK folder, for example `C:\Program Files\Java\jdk1.7.0_21`.
     The individual tools and other SDK packages used by Android Studio are installed in a separate directory. If you need to access the tools directly, use a terminal to navigate to the location where they are installed. For example: `\Users\<user>\sdk\`
- On Mac OSX
  1. Launch the `.dmg` file you downloaded.
  2. Drag and drop Android Studio into the Applications folder.
  3. Open Android Studio and follow the setup wizard to install any necessary SDK tools.
     If you need to use the Android SDK tools from a command line, you can access them at: `/Users/<user>/Library/Android/sdk/`
- On Linux
  1. Unpack the .zip file you downloaded to an appropriate location for your applications.
  2. To launch Android Studio, open a terminal, navigate to the android-studio/bin/ directory, and execute studio.sh.
     You may want to add android-studio/bin/ to your PATH environmental variable so that you can start Android Studio from any directory.
  3. If the SDK is not already installed, follow the setup wizard to install the SDK and any necessary SDK tools.
```Note: To support 32-bit apps on a 64-bit machine, you will need to install the ia32-libs, lib32ncurses5-dev, and lib32stdc++6 packages.```
## 3. Pre-requisites
- Android Sdk 29
- Min Sdk Version 21
## 4. Clone source code from Github
### Tools
You can use SourceTree or The Command Line to clone it.
- [Download SourceTree here](https://www.sourcetreeapp.com/)
- [Reference for The Command Line here](https://git-scm.com/)
### Steps to clone source code by The Command Line
```
```
## 5. Import an Android Project
1. Start Android Studio
2. At `Welcome to Android Studio` window, choose `Import project (Eclipse ADT, Gradle, etc.)` in `Quick Start` tab
3. Select path to your project `.../`
4. Import custom settings for android studio
```
```
## 6. Building and running a project
On the toolbar in Android Studio, select ```Build > Make Project```
After that select ```Run > Run 'app'```
`Device Chooser` form will be appeared, choose a device and click `OK` button.
```
Note: make sure your Android device is connected with your computer.
```
## 7. Development Dependencies
- Programing Language: Kotlin 1.3.21
- Architecture: MVVM
- Data, flow, event: RxJava2
- Unit Testing: Junit4, Mockito, MockServer
- Code analysis: detekt
## 8. Use custom setting
Since import custom setting, we have some live template and todo templates
Unit test function(live template):
- Abbreviation: test
- Template:
```
@Test
fun `Given $GIVEN$ - When $WHEN$ - Then $THEN$`() {
    /* Given */
    $END$
    /* When */
    /* Then */
}
```
## 9. Build and tests
This app has build type:
- debug: Development environment

## 10. Build Apk
```
It's depends on which environment we want to build.
$./gradlew clean assembleDebug
$./gradlew clean assembleStaging
$./gradlew clean assembleRelease
```
## 11. Test
- Run unit test and code coverage
```
$./gradlew clean test jacocoTestReport
```
- Code analysis
```
$./gradlew check
```
## 12. Coding style
We are following bellow coding conventions
- https://github.com/JetBrains/kotlin-web-site/blob/master/pages/docs/reference/coding-conventions.md
