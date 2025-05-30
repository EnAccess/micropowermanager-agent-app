<p align="center">
  <a href="https://github.com/EnAccess/micropowermanager-agent-app">
    <img
      src="https://micropowermanager.io/mpmlogo_raw.png"
      alt="MicroPowerManager Customer Registration App"
      width="320"
    >
  </a>
</p>
<p align="center">
    <em>Decentralized utility management made simple. Manage customers, revenues and assets with this all-in one open source platform.</em>
</p>
<p align="center">
  <img
    alt="Project Status"
    src="https://img.shields.io/badge/Project%20Status-stable-green"
  >
  <img
    alt="GitHub Workflow Status"
    src="https://img.shields.io/github/actions/workflow/status/EnAccess/micropowermanager-agent-app/check-generic.yaml"
  >
  <a href="https://github.com/EnAccess/micropowermanager-agent-app/blob/main/LICENSE" target="_blank">
    <img
      alt="License"
      src="https://img.shields.io/github/license/EnAccess/micropowermanager-agent-app"
    >
  </a>
</p>

---

# MicroPowerManager - Agent App

MicroPowerManager (MPM) is a decentralized utility and customer management tool.
Manage customers, revenues and assets with this all-in one Open Source platform.

## Get Started

This repository contains the source code for the [MicroPowerManager Agent App](https://micropowermanager.io/usage-guide/android-apps.html).

### Prerequsites

- Install [Android Studio](https://developer.android.com/studio)
- Git clone the repository
- (Optional, but recommended) Install [direnv](https://direnv.net/)

### Build and run the app locally

To build the app

- Open the project in Android Studio
- Configure `temurin-11` as Gradle JDK.

  - Open Setting (**Android Studio > Settings > Build, Execution, Deployment > Build Tools > Gradle**)
  - If not installed yet, select **Download JDK...** from the **Gradle JDK** dropdown and select

    | Field    | Value                                  |
    | -------- | -------------------------------------- |
    | Version  | `11`                                   |
    | Vendor   | `Eclipse Temurin AdoptOpenJDK HotSpot` |
    | Location | `<default>`                            |

    ![Android Studio Temurin Runtime](docs/images/android-studio-adopt-openjdk-11.png)

    **NOTE:** For ideal performance be sure to select the correct architecture.
    For example for users of Mac's with M-chips select `aarch64`.

  - If already installed, select **temurin-11** from the **Gradle JDK** dropdown

- (Optional, but recommended) Copy `.envrc.sample` to `.envrc` and adjust `JAVA_HOME=` to match the Gradle JDK path from above.
- Click **Sync Project with Gradle files**

**Run on a Device or Emulator:**

- Minimum `minSdkVersion` is 21 (Android 5.0).
- The app uses location permissions, so ensure the emulator or device has **Google Play Services** and location set up.

### Create a release APK locally

To create a release APK locally

1. Open a terminal and confirm `$JAVE_HOME` is set correctly.
2. Run

   ```sh
   ./gradlew assembleRelease
   ```

3. The output APK will be located at `app/build/outputs/apk/release`
