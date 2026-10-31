# ATVLoadly Android

Android companion project for an IPA manager targeting Apple TV and iPhone/iPad.

## Current progress

- Android UI prototype with target selection (Apple TV / iPhone / iPad).
- Android system file picker for selecting an IPA.
- Input fields for an Apple TV address and pairing PIN.
- File picker controls for a P12 signing identity and provisioning profile.
- Basic validation/status messages.

**Not implemented yet:** Apple TV pairing/install backend, IPA signing, certificate/profile validation, and iPhone/iPad installation. The certificate/password inputs are only UI; the app does not read, upload, or use those credentials. The app currently does not install any IPA. The Apple TV and iOS/iPadOS flows require different backends and must be tested on real devices.

## iPhone / iPad constraints

IPA installation requires a valid Apple-issued signature and a compatible installation path. A plain IPA file cannot simply be copied into iOS and installed by an Android app. The signing/install workflow must be implemented separately; free Apple development signing also has restrictions and expiry.

## Build

Push to `main` or run the **Build APK** workflow from Actions. The workflow builds a debug APK and publishes it as a GitHub Release on pushes to `main`.

## Requirements

- Android Studio / JDK 17 for local builds
- Android SDK 35
