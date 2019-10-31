# Convalida
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14) [![CircleCI Build Status](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/WellingtonCosta/convalida) [![codecov](https://codecov.io/gh/WellingtonCosta/convalida/branch/master/graph/badge.svg)](https://codecov.io/gh/WellingtonCosta/convalida) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.wellingtoncosta/convalida-runtime/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.github.wellingtoncosta/convalida-runtime) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Convalida-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6289) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23305-brightgreen.svg)](https://androidweekly.net/issues/issue-305)

![Logo](logo.png)

**Convalida** - (Italian for "validation")

Convalida is a simple, lightweight and powerful field validation library for Android.

### Documentation

Go to the [website][1] for more information.

### Download

To use Convalida with annotations or Data Binding support, and compile-time code generation:

```groovy
dependencies {
  implementation 'io.github.wellingtoncosta:convalida-runtime:3.2.0'
  annotationProcessor 'io.github.wellingtoncosta:convalida-compiler:3.2.0'
}
```

*If you are using Kotlin, replace `annotationProcessor` with `kapt`.*

Or if you want to use only the Convalida Kotlin Dsl:

```groovy
dependencies {
  implementation 'io.github.wellingtoncosta:convalida-ktx:3.2.0'
}
```

*Snapshots of the development version are available in [Sonatype's `snapshots` repository][2].*

### License

    Copyright 2017 Wellington Costa

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://wellingtoncosta.github.io/convalida
[2]: https://oss.sonatype.org/content/repositories/snapshots/
