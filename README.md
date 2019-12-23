# android-textile

[![Made by Textile](https://img.shields.io/badge/made%20by-Textile-informational.svg?style=popout-square)](https://textile.io)
[![Chat on Slack](https://img.shields.io/badge/slack-slack.textile.io-informational.svg?style=popout-square)](https://slack.textile.io)
[![GitHub license](https://img.shields.io/github/license/textileio/android-textile.svg?style=popout-square)](./LICENSE)
[![CircleCI branch](https://img.shields.io/circleci/project/github/textileio/android-textile/master.svg?style=popout-square)](https://circleci.com/gh/textileio/android-textile)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=popout-square)](https://github.com/RichardLitt/standard-readme)
![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg?style=popout-square)
![Bintray](https://img.shields.io/badge/dynamic/json.svg?label=latest&query=name&style=flat-square&url=https%3A%2F%2Fapi.bintray.com%2Fpackages%2Ftextile%2Fmaven%2Ftextile%2Fversions%2F_latest)

---
**Deprecation Warning**

Textile Threads v1 are being deprecated. Please follow our ongoing work on v2 on both the [go-textile-threads repo](https://github.com/textileio/go-textile-threads) and the [early preview](https://paper.dropbox.com/doc/Threads-v2-Early-Preview-X8fKsMiTyztuQ1L8CnUng). 

Until Threads v2 release and integration into the Android SDK, this repo should be used for experimental purposes only.

---

> Textile provides encrypted, recoverable, schema-based, and cross-application data storage built on IPFS and libp2p. We like to think of it as a decentralized data wallet with built-in protocols for sharing and recovery, or more simply, an open and programmable iCloud.

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Install

The Textile Android library is published in [Textile's Bintray Maven repository](https://dl.bintray.com/textile/maven).
You can install it using Gradle.

First, you'll need to add Textile's Bintray Maven repository to you project's top level `build.gradle` in the `allProjects.repositories` section:

```cmd
allprojects {
    repositories {
        ...
        maven { url 'https://dl.bintray.com/textile/maven' }
        maven { url 'https://jitpack.io' }
        ...
    }
}
```

Next, add the Textile dependency to your app module's `build.gradle` `dependencies` section, specifying the [latest version available](https://bintray.com/textile/maven/textile/_latestVersion):

```cmd
dependencies {
    ...
    implementation 'io.textile:textile:1.1.0'
    ...
}
```

## Usage

To learn about Textile usage, please head over to the [Textile documentation](https://docs.textile.io/) and be sure to read the [Android getting started guide](https://docs.textile.io/develop/clients/android/).

This repo contains an example project. To run it, clone the repo, build and run the example `app` module.

## Maintainers

[@asutula](https://github.com/asutula)
[@sanderpick](https://github.com/sanderpick)

## Contributing

PRs accepted.

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

MIT © 2019 Textile
