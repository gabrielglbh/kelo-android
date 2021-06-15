# KELO

[![master](https://img.shields.io/circleci/build/github/gabrielglbh/tfm-android/main)](https://app.circleci.com/pipelines/github/gabrielglbh/tfm-android?branch=main) [![master](https://codecov.io/gh/gabrielglbh/tfm-android/branch/main/graph/badge.svg?token=J29KTCIZVY)](https://codecov.io/gh/gabrielglbh/tfm-android/branch/main) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/f65a342064ea42bd99d4661b7b94aa17)](https://www.codacy.com/gh/gabrielglbh/tfm-android/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=gabrielglbh/tfm-android&amp;utm_campaign=Badge_Grade)

## Disclaimer

There's currently (15 Jun 2021) an issue with Firebase Messaging with some emulators and devices (see [#1286](https://github.com/firebase/firebase-android-sdk/issues/1286)). As for this, all UI tests fails upon execution on Circle CI (locally they do not fail). Therefore, as we use CircleCI for our continuous integration tool, __all UI tests have been left out of execution__ in order to get a proper coverage number. Proper adjustments to the coverage report files have also been made.

## For Developers

Remember to change the `versionCode` and `versionName` on the `gradle:app` file when releasing a new version through tags

## Application Description

KELO is a project for managing your house chores with your friends!

-   Add a new chores and their expire date
-   Tag yourself and others in the chores
-   Make rewards for doing a lot of chores
-   Notify your friends that they need to make a chore

## Objectives of this Project

This project is a Master Thesis for the Mobile App Development Master @ UPM. It is developed for [Android](https://github.com/gabrielglbh/tfm-android) and [iOS](https://github.com/olmedocr/tfm-ios) using the latest technology and dependencies. The Master Thesis purpose is to identify, explain and argue the differences between Android and iOS development for the same application whilst exploring a real-world environment using continuous integration. 

<img src="https://www.upm.es/sfs/Rectorado/Gabinete%20del%20Rector/Logos/UPM/CEI/LOGOTIPO%20leyenda%20color%20JPG%20p.png" alt="UPM Logo" width="827" height="384">

It makes use of [Circle CI](https://app.circleci.com/pipelines/github/gabrielglbh/tfm-android) and [Fastlane](https://fastlane.tools/) for automatic deployments to the Google Play Store and automatic tests using [Cucumber](https://cucumber.io/docs/installation/), [JUnit](https://junit.org/junit4/) and [Espresso](https://developer.android.com/training/testing/espresso). For code coverage (what portion of code we covered with testing) we used [Codecov](https://app.codecov.io/gh/gabrielglbh/tfm-android).
