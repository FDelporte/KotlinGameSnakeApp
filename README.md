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

## Install Android Command Line Tools

Install Android Studio (Recommended - Full IDE)
Download and install from: https://developer.android.com/studio

OR (if it works...):

```shell
# Install via Homebrew
brew install --cask android-commandlinetools

# Create SDK directory
mkdir -p ~/Library/Android/sdk

# Set environment variable
export ANDROID_HOME=$HOME/Library/Android/sdk
echo "sdk.dir=$ANDROID_HOME" > local.properties

# Add to your shell profile
echo 'export ANDROID_HOME=$HOME/Library/Android/sdk' >> ~/.zshrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools' >> ~/.zshrc
source ~/.zshrc

# Accept licenses
yes | sdkmanager --licenses

# Install required components
sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"

sdkmanager --licenses
```