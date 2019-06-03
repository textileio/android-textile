# android-textile

[![standard-readme compliant](https://img.shields.io/badge/standard--readme-OK-green.svg?style=flat)](https://github.com/RichardLitt/standard-readme)
![Bintray](https://img.shields.io/bintray/v/textile/maven/textile.svg)
![GitHub](https://img.shields.io/github/license/textileio/android-textile.svg)
![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg)
[![CircleCI](https://circleci.com/gh/textileio/android-textile.svg?style=svg)](https://circleci.com/gh/textileio/android-textile)

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

## Usage

To learn about Textile usage, please head over to the [Textile documentation](https://docs.textile.io/) and be sure to read the [Android getting started guide](https://docs.textile.io/develop/clients/android/).

This repo contains an example project. To run it, clone the repo, build and run the example `app` module.

## Maintainers

[@asutula](https://github.com/asutula)

## Contributing

PRs accepted.

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

MIT © 2019 Textile
