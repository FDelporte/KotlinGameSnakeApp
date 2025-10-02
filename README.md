# Emoji Snake Game App Created with Kotlin

To build and run:

```shell
sdk use java 21-zulu

# Desktop
./gradlew :desktopApp:run

# Android
./gradlew :androidApp:assembleRelease
# Or install directly on connected device/emulator
./gradlew :androidApp:installDebug

./gradlew build
java -jar build/libs/KotlinGameSnakeApp.jar
```