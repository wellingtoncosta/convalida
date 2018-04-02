# Convalida [![Build Status](https://travis-ci.org/WellingtonCosta/convalida.svg?branch=master)](https://travis-ci.org/WellingtonCosta/convalida) [![codecov](https://codecov.io/gh/WellingtonCosta/convalida/branch/master/graph/badge.svg)](https://codecov.io/gh/WellingtonCosta/convalida) [![](https://jitpack.io/v/WellingtonCosta/convalida.svg)](https://jitpack.io/#WellingtonCosta/convalida) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Convalida-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6289) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

![Logo](logo.png)

**Convalida** - (Italian for "validation")

Convalida is a simple, lightweight and powerful field validation library for Android.

## Why Convalida?

-   Annotation-based;
-   Compile-time;
-   Compatible with other annotation-based libraries and frameworks such as [ButterKnife][1], [AndroidAnnotations][2], etc;
-   Works with **Stock Android Widgets**;
-   Based on [Material Design Error Patterns][4];

## Quick Start

**Step 1** - Annotate your fields with [Convalida Annotations][3]:

```java
@RequiredValidation(R.string.field_required)
EditText nameField;

@LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
EditText nickNameField;

@OnlyNumberValidation(R.string.only_numbers)
EditText ageField;

@EmailValidation(R.string.invalid_email)
EditText emailField;

@ConfirmEmailValidation(R.string.emails_not_match)
EditText confirmEmailField;

@PatternValidation(errorMessage = R.string.invalid_phone, pattern = PHONE_PATTERN)
EditText phoneField;

@PasswordValidation(min = 3, pattern = MIXED_CASE_NUMERIC, errorMessage = R.string.invalid_password)
EditText passwordField;

@ConfirmPasswordValidation(R.string.passwords_not_match)
EditText confirmPasswordField;

@ValidateOnClick
Button validateButton;
```

**Step 2** - Initialize Convalida (notice that the it starts with the same name of the Activity):

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    SampleActivityFieldsValidation.init(this);
}
```

**Step 3** - Run the validations by clicking in the button mapped with `@ValidateOnClick` and handle success or error:

```java
@OnValidationSuccess
public void onValidationSuccess() {
    Toast.makeText("Yay!", Toast.LENGTH_LONG).show();
}

@OnValidationError
public void error() {
    Toast.makeText("Something is wrong :(", Toast.LENGTH_LONG).show();
}
```

_Note: Only the method annotated with `@OnValidationSuccess` is required._

**Step 4** - If you want to clear the validations:

```java
@ClearValidationsOnClick
Button clearValidationsButton;
```

**Remember: You must initialize the views (e.g [ButterKnife][1]) before apply the validations.**

## Download

**Step 1** - Add the JitPack repository to your root build.gradle file:

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

**Step 2** - Add the dependencies:

```groovy
dependencies {
  compile 'com.github.WellingtonCosta.convalida:convalida:1.3.7'
  annotationProcessor 'com.github.WellingtonCosta.convalida:convalida-compiler:1.3.7'
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
