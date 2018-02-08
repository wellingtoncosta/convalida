# Convalida [![Build Status](https://travis-ci.org/WellingtonCosta/convalida.svg?branch=master)](https://travis-ci.org/WellingtonCosta/convalida) [![codecov](https://codecov.io/gh/WellingtonCosta/convalida/branch/master/graph/badge.svg)](https://codecov.io/gh/WellingtonCosta/convalida) [![](https://jitpack.io/v/WellingtonCosta/convalida.svg)](https://jitpack.io/#WellingtonCosta/convalida) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Convalida-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6289) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

![Logo](logo.png)

__Convalida__ - (Italian for "validation")

Convalida is a simple, lightweight and powerful field validation library for Android.

## Why Convalida?

- Annotation-based;
- Compile-time;
- Compatible with other annotation-based libraries and frameworks such as [ButterKnife][1], [AndroidAnnotations][2], etc;
- Works with __Stock Android Widgets__;
- Based on [Material Design Error Patterns][4];

## Quick Start

__Step 1__ - Annotate your fields with [Convalida Annotations][3]:

```java
@NotEmptyValidation(R.string.field_required)
TextInputLayout nameLayout;

@LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
TextInputLayout nickNameLayout;

@OnlyNumberValidation(R.string.only_numbers)
TextInputLayout ageLayout;

@EmailValidation(R.string.invalid_email)
TextInputLayout emailLayout;

@ConfirmEmailValidation(R.string.emails_not_match)
TextInputLayout confirmEmailLayout;

@PatternValidation(errorMessage = R.string.invalid_phone, pattern = PHONE_PATTERN)
TextInputLayout phoneLayout;

@PasswordValidation(min = 3, errorMessage = R.string.invalid_password)
TextInputLayout passwordLayout;

@ConfirmPasswordValidation(R.string.passwords_not_match)
TextInputLayout confirmPasswordLayout;

@ValidateOnClick
Button validateButton;
```

__Step 2__ - Initialize Convalida:

```java

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    SampleActivityFieldsValidation.init(this);
}
```

__Step 3__ - Run the validations by clicking in the button mapped with ```@ValidateOnClick``` and handle success or error:

```java
@OnValidationSuccess
public void onValidationSuccess() {
    Toast.makeText("Yay!", Toast.LENGTH_LONG).show();
}

@OnValidationError
public void onValidationError() {
    Toast.makeText("Something is wrong :(", Toast.LENGTH_LONG).show();
}
```

*Note: Only the method annotated with ```@OnValidationSuccess``` is required.*

__Step 4__ - If you want to clear the validations:

```java
@ClearValidationsOnClick
Button clearValidationsButton;
```

__Remember: You must initialize the views (e.g [ButterKnife][1]) before apply the validations.__

## Download

__Step 1__ - Add the JitPack repository to your root build.gradle file:

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

__Step 2__ - Add the dependencies:

```groovy
dependencies {
  compile 'com.github.WellingtonCosta.convalida:convalida:1.2.1'
  annotationProcessor 'com.github.WellingtonCosta.convalida:convalida-compiler:1.2.1'
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
[2]: https://github.com/androidannotations/androidannotations
[3]: https://github.com/WellingtonCosta/convalida/tree/master/convalida-annotations/src/main/java/convalida/annotations
[4]: https://material.io/guidelines/patterns/errors.html