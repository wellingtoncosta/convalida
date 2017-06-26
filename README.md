# Convalida

![Logo](logo.png)

Convalida is a simple, lightweight, powerful and annotation-based way to validate fields in Android.

## Why Convalida?

- Annotation-based;
- 100% reflection-free (compile-time);
- Compatible with other annotation-based libraries and frameworks such as [ButterKnife][1], [AndroidAnnotations][2], etc...

## Quick Start

First, annotate your fields with [Convalida Annotations][3]:

```java
private static final String PHONE_PATTERN = "^\\([1-9]{2}\\)?([0-9]{9})$";

@NotEmptyValidation(R.string.field_required)
TextInputLayout nameLayout;

@EmailValidation(R.string.invalid_email)
TextInputLayout emailLayout;

@PatternValidation(errorMessage = R.string.invalid_phone, pattern = PHONE_PATTERN)
TextInputLayout phoneLayout;

@PasswordValidation(R.string.password_required)
TextInputLayout passwordLayout;
```

Now, you must initialize Convalida calling `initialize()` method:

```java
private ConvalidaValidator validator;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    validator = Convalida.initialize(this);
}
```

And then, to execute the validations, you must call `validateFields()` method that returns the current validation state:

```java
public void validateFields() {
    boolean isValid = validator.validateFields();
    String message = isValid ? "Yay!" : "Something is wrong :(";
    Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).show();
}
```

And to clear the fields valitation state, you must call `clearValidations()`:

```java
public void clearFields() {
    validator.clearValidations();
}
```

__Remember: You must initialize the views (e.g [ButterKnife][1]) before apply the validations.__

## Download

```groovy
dependencies {
  compile 'br.com.wellingtoncosta:convalida-library:1.0.0'
  annotationProcessor 'br.com.wellingtoncosta:convalida-compiler:1.0.0'
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
