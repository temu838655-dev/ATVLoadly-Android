# ATVLoadly Android

Android companion project for exploring Apple TV IPA sideloading over Wi-Fi.

## Status

This repository currently contains an Android build scaffold and UI prototype. It does **not yet** implement Apple TV pairing, IPA signing, or installation. Those capabilities require porting/integrating the atvloadly/PlumeImpactor core and validating it against real Apple TV hardware.

## Build

Push to `main` or run the **Build APK** workflow from Actions. The workflow builds a debug APK and publishes it as a GitHub Release on pushes to `main`.

## Requirements

- Android Studio / JDK 17 for local builds
- Android SDK 35
