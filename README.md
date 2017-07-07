# Convalida [![](https://jitpack.io/v/WellingtonCosta/convalida.svg)](https://jitpack.io/#WellingtonCosta/convalida)

![Logo](logo.png)

Convalida is a simple, lightweight, powerful and annotation-based way to validate fields in Android.

## Why Convalida?

- Annotation-based;
- 100% reflection-free (compile-time);
- Compatible with other annotation-based libraries and frameworks such as [ButterKnife][1], [AndroidAnnotations][2], etc...

## Quick Start

__Step 1__ - Annotate your fields with [Convalida Annotations][3]:

```java
private static final String PHONE_PATTERN = "^\\([1-9]{2}\\)?([0-9]{9})$";

@NotEmptyValidation(R.string.field_required)
@LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
TextInputLayout nameLayout;

@OnlyNumberValidation(R.string.only_numbers)
TextInputLayout ageLayout;

@EmailValidation(R.string.invalid_email)
TextInputLayout emailLayout;

@PatternValidation(errorMessage = R.string.invalid_phone, pattern = PHONE_PATTERN)
TextInputLayout phoneLayout;

@PasswordValidation(
  min = 3,
  pattern = Patterns.LOWER_UPPER_CASE_NUMERIC_SPECIAL,
  errorMessage = R.string.invalid_password
)
TextInputLayout passwordLayout;

@ConfirmPasswordValidation(R.string.passwords_not_match)
TextInputLayout confirmPasswordLayout;
```

__Step 2__ - Initialize Convalida:

```java
private ConvalidaValidator validator;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    validator = Convalida.init(this);
}
```

__Step 3__ - Run the validations and get the result:

```java
public void validateFields() {
    boolean isValid = validator.validateFields();
    String message = isValid ? "Yay!" : "Something is wrong :(";
    Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).show();
}
```

__Step 4__ - If you want to clear the validations:

```java
public void clearFields() {
    validator.clearValidations();
}
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
  compile 'com.github.WellingtonCosta.convalida:convalida:1.0.1'
  annotationProcessor 'com.github.WellingtonCosta.convalida:convalida-compiler:1.0.1'
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
