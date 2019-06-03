# Textile

[![CircleCI](https://circleci.com/gh/textileio/android-textile.svg?style=svg)](https://circleci.com/gh/textileio/android-textile)
![Bintray](https://img.shields.io/bintray/v/textile/maven/textile.svg)
![GitHub](https://img.shields.io/github/license/textileio/android-textile.svg)
![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg)

Textile provides encrypted, recoverable, schema-based, and cross-application data storage built on IPFS and libp2p. We like to think of it as a decentralized data wallet with built-in protocols for sharing and recovery, or more simply, an open and programmable iCloud.

To learn more, please head over to the [Textile documentation](https://docs.textile.io/).

## Getting Started

[Click here to read the Android getting started guide](https://docs.textile.io/develop/clients/android/).

## Installation

The Textile Android library is published in [Textile's Bintray Maven repository](https://dl.bintray.com/textile/maven).
You can install it using Gradle.

First, you'll need to add Textile's Bintray Maven repository to you project's top level `build.gradle` in the `allProjects.repositories` section:

```cmd
allprojects {
    repositories {
        ...
        maven { url "https://dl.bintray.com/textile/maven" }
        ...
    }
}
```

Next, add the Textile dependency to your app module's `build.gradle` `dependencies` section, specifying the [latest version available](https://bintray.com/textile/maven/textile/_latestVersion):

```cmd
dependencies {
    ...
    implementation 'io.textile:textile:0.1.20'
    ...
}
```

## Example

To run the example project, clone the repo, build and run the example `app` module.

## Author

Textile, contact@textile.io

## License

Textile is available under the MIT license. See the LICENSE file for more info.
