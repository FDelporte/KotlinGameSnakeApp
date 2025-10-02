# Complete Setup Guide

## Step 1: Create Android Keystore

Generate a keystore for signing your app:

```bash
keytool -genkey -v -keystore release-keystore.jks \
  -alias snake-game \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

You'll be prompted for:
- Keystore password
- Key password (can be same as keystore)
- Your name and organization details

**Important:** Save these passwords securely! You'll need them for GitHub Secrets.

## Step 2: Encode Keystore to Base64

```bash
# On macOS:
base64 -i release-keystore.jks | pbcopy

# On Linux:
base64 -w 0 release-keystore.jks

# On Windows (PowerShell):
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release-keystore.jks")) | Set-Clipboard
```

## Step 3: Set Up Google Play Console

### 3.1 Create Your App
1. Go to [Google Play Console](https://play.google.com/console)
2. Click "Create app"
3. Fill in app details:
    - App name: "Snake Game"
    - Default language: English
    - App type: Game
    - Category: Casual
    - Free/Paid: Free

### 3.2 Create Service Account
1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create new project or select existing
3. Enable "Google Play Android Developer API"
4. Go to "IAM & Admin" → "Service Accounts"
5. Click "Create Service Account"
    - Name: "github-actions-deploy"
    - Role: "Service Account User"
6. Click on created service account
7. Go to "Keys" tab
8. Click "Add Key" → "Create new key"
9. Choose JSON format
10. Download the JSON file

### 3.3 Grant Permissions in Play Console
1. Go back to Play Console
2. Settings → API access
3. Link the service account you created
4. Grant permissions: "Admin (all permissions)"

## Step 4: Configure GitHub Secrets

Go to your GitHub repository → Settings → Secrets and variables → Actions

Add these secrets:

| Secret Name | Value | How to Get |
|------------|-------|------------|
| `ANDROID_KEYSTORE_BASE64` | Base64 string | From Step 2 |
| `KEYSTORE_PASSWORD` | Your keystore password | From Step 1 |
| `KEY_ALIAS` | snake-game | From Step 1 |
| `KEY_PASSWORD` | Your key password | From Step 1 |
| `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` | Full JSON content | From Step 3.2 |

### Setting up secrets:
1. Click "New repository secret"
2. Enter name exactly as shown above
3. Paste the value
4. Click "Add secret"

## Step 5: Initial Play Store Setup

Before automated deployment works, you need to upload the first version manually:

### 5.1 Build locally
```bash
./gradlew bundleRelease
```

### 5.2 Upload to Play Console
1. Go to Play Console → Your App → Release → Production
2. Click "Create new release"
3. Upload the AAB from: `app/build/outputs/bundle/release/app-release.aab`
4. Fill in release notes
5. Save (don't publish yet)
6. Complete all required setup:
    - Content rating
    - Target audience
    - Privacy policy
    - App content
    - Store listing

### 5.3 Submit for Review
1. Once everything is complete, submit for review
2. Wait for approval (can take a few days)

## Step 6: Automated Deployments

After the first manual upload, GitHub Actions will handle subsequent releases:

### Deploy via Git Tag
```bash
git tag v1.0.1
git push origin v1.0.1
```

This will:
1. Build signed APK and AAB
2. Create GitHub release
3. Upload to Play Store (internal track)

### Manual Workflow Dispatch
1. Go to GitHub → Actions
2. Select "Android Release Build and Deploy"
3. Click "Run workflow"
4. Choose release track (internal/alpha/beta/production)

## Step 7: Testing

### Test Local Build
```bash
# Debug build
./gradlew assembleDebug
./gradlew installDebug

# Release build (requires keystore)
export KEYSTORE_FILE=./release-keystore.jks
export KEYSTORE_PASSWORD=your_password
export KEY_ALIAS=snake-game
export KEY_PASSWORD=your_password
./gradlew assembleRelease
```

### Test on Device
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Common Issues and Solutions

### Issue: Build fails with "KEYSTORE_FILE not found"
**Solution:** Make sure environment variables are set correctly:
```bash
export KEYSTORE_FILE=/absolute/path/to/release-keystore.jks
```

### Issue: "Invalid keystore format"
**Solution:** Re-generate keystore with correct parameters from Step 1

### Issue: GitHub Actions fails on deploy
**Solution:**
1. Verify all secrets are set correctly
2. Check service account has correct permissions
3. Ensure first version was uploaded manually

### Issue: ProGuard removes needed classes
**Solution:** Add keep rules in `app/proguard-rules.pro`

## Project Structure Overview

```
KotlinSnakeGame/
├── .github/workflows/          # CI/CD automation
├── app/
│   ├── src/main/
│   │   ├── java/com/snakegame/
│   │   │   ├── MainActivity.kt       # Entry point
│   │   │   ├── GameScreen.kt         # UI components
│   │   │   ├── SnakeGame.kt          # Game logic
│   │   │   └── models/               # Data models
│   │   └── res/                      # Resources
│   ├── build.gradle.kts              # App build config
│   └── proguard-rules.pro            # Obfuscation rules
├── build.gradle.kts                  # Project build config
└── settings.gradle.kts               # Project settings
```

## Version Management

Update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Increment for each release
    versionName = "1.0.1"  // User-visible version
}
```

## Release Checklist

Before each release:
- [ ] Test on multiple devices
- [ ] Update versionCode and versionName
- [ ] Update release notes
- [ ] Test ProGuard doesn't break functionality
- [ ] Check all assets are included
- [ ] Verify permissions are correct
- [ ] Test on different Android versions (min API 24)

## Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)