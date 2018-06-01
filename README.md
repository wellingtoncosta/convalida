# Convalida [![CircleCI Build Status](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/WellingtonCosta/convalida) [![codecov](https://codecov.io/gh/WellingtonCosta/convalida/branch/master/graph/badge.svg)](https://codecov.io/gh/WellingtonCosta/convalida) [![](https://jitpack.io/v/WellingtonCosta/convalida.svg)](https://jitpack.io/#WellingtonCosta/convalida) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Convalida-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6289) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

![Logo](logo.png)

**Convalida** - (Italian for "validation")

Convalida is a simple, lightweight and powerful field validation library for Android.

## Why Convalida?

-   You can use with annotations or with data binding;
-   Compile-time;
-   Compatible with other popular libraries such as [ButterKnife][1], [Android Data Binding][2], [Dagger 2][3], etc;
-   Works with **Stock Android Widgets**;
-   Based on [Material Design Error Patterns][4];

## Documentation

See the [wiki][5] for more information.

## Download

**Step 1** - Add the JitPack repository to your root build.gradle file:

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

**Step 2** - Add the dependencies:

```groovy
dependencies {
  implementation 'com.github.WellingtonCosta.convalida:convalida:2.1.0'
  annotationProcessor 'com.github.WellingtonCosta.convalida:convalida-compiler:2.1.0'
}
```

## License

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

[1]: https://github.com/JakeWharton/butterknife

[2]: https://developer.android.com/topic/libraries/data-binding/index.html

[3]: https://github.com/google/dagger

[4]: https://material.io/guidelines/patterns/errors.html

[5]: https://github.com/WellingtonCosta/convalida/wiki
