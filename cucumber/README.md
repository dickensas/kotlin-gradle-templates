# kotlin-gradle-templates / cucumber
Quick start kotlin jvm project for cucumber browser automation

## Usage
The code is ready to execute provided the the chrome driver downloaded and placed in the root folder "driver" folder

     make sure to download the driver from https://chromedriver.chromium.org/downloads and put inside the "driver" folder

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start test automation

     .\gradlew test

The browser will launch and google search will happen