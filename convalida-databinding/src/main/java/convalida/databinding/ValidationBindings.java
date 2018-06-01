 package convalida.databinding;

 import android.databinding.BindingAdapter;
 import android.support.annotation.NonNull;
 import android.widget.Button;
 import android.widget.EditText;

 import convalida.validators.BetweenValidator;
 import convalida.validators.ConfirmEmailValidator;
 import convalida.validators.ConfirmPasswordValidator;
 import convalida.validators.CpfValidator;
 import convalida.validators.CreditCardValidator;
 import convalida.validators.EmailValidator;
 import convalida.validators.LengthValidator;
 import convalida.validators.OnlyNumberValidator;
 import convalida.validators.PasswordValidator;
 import convalida.validators.PatternValidator;
 import convalida.validators.RequiredValidator;

/**
 * @author WellingtonCosta on 29/03/18.
 */
public class ValidationBindings {

    @BindingAdapter(value = {
            "requiredValidationErrorMessage",
            "requiredValidationAutoDismiss"
    }, requireAll = false)
    public static void requiredValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss
    ) {
        field.setTag(R.id.validation_type, new RequiredValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "emailValidationErrorMessage",
            "emailValidationAutoDismiss",
            "emailValidationRequired"
    }, requireAll = false)
    public static void emailValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new EmailValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "confirmEmailValidationEmailField",
            "confirmEmailValidationErrorMessage",
            "confirmEmailValidationAutoDismiss"
    }, requireAll = false)
    public static void confirmEmailValidationBindings(
            @NonNull EditText confirmEmailField,
            @NonNull EditText emailField,
            @NonNull String errorMessage,
            Boolean autoDismiss
    ) {
        confirmEmailField.setTag(R.id.validation_type, new ConfirmEmailValidator(
                emailField,
                confirmEmailField,
                errorMessage,
                autoDismiss != null ? autoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "patternValidationErrorMessage",
            "patternValidationPattern",
            "patternValidationAutoDismiss",
            "patternValidationRequired"
    }, requireAll = false)
    public static void patternValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            @NonNull String pattern,
            Boolean autoDismiss,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new PatternValidator(
                field,
                errorMessage,
                pattern,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "lengthValidationMin",
            "lengthValidationMax",
            "lengthValidationErrorMessage",
            "lengthValidationAutoDismiss",
            "lengthValidationRequired"
    }, requireAll = false)
    public static void lengthValidationBindings(
            @NonNull EditText field,
            int min,
            int max,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new LengthValidator(
                field,
                min,
                max,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "onlyNumberValidationErrorMessage",
            "onlyNumberValidationAutoDismiss",
            "onlyNumberValidationRequired"
    }, requireAll = false)
    public static void onlyNumberValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new OnlyNumberValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "passwordValidationErrorMessage",
            "passwordValidationMinLength",
            "passwordValidationPattern",
            "passwordValidationAutoDismiss"
    }, requireAll = false)
    public static void passwordValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Integer minLength,
            String pattern,
            Boolean autoDismiss
    ) {
        field.setTag(R.id.validation_type, new PasswordValidator(
                field,
                minLength != null ? minLength : 0,
                pattern != null ? pattern : "",
                errorMessage,
                autoDismiss != null ? autoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "confirmPasswordValidationPasswordField",
            "confirmPasswordValidationErrorMessage",
            "confirmPasswordValidationAutoDismiss"
    }, requireAll = false)
    public static void confirmPasswordValidationBindings(
            @NonNull EditText confirmPasswordField,
            @NonNull EditText passwordField,
            @NonNull String errorMessage,
            Boolean autoDismiss
    ) {
        confirmPasswordField.setTag(R.id.validation_type, new ConfirmPasswordValidator(
                passwordField,
                confirmPasswordField,
                errorMessage,
                autoDismiss != null ? autoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "cpfValidationErrorMessage",
            "cpfValidationAutoDismiss",
            "cpfValidationRequired"
    }, requireAll = false)
    public static void cpfValidationBindings(
            @NonNull EditText cpfField,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        cpfField.setTag(R.id.validation_type, new CpfValidator(
                cpfField,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "betweenValidationStartErrorMessage",
            "betweenValidationStartAutoDismiss",
            "betweenValidationEndField",
            "betweenValidationEndErrorMessage",
            "betweenValidationEndAutoDismiss"
    }, requireAll = false)
    public static void betweenValidationBindings(
            @NonNull EditText startField,
            @NonNull String startErrorMessage,
            Boolean startAutoDismiss,
            EditText endField,
            @NonNull String endErrorMessage,
            Boolean endAutoDismiss
    ) {
        startField.setTag(R.id.validation_type, new BetweenValidator(
                startField,
                endField,
                startErrorMessage,
                endErrorMessage,
                startAutoDismiss != null ? startAutoDismiss : true,
                endAutoDismiss != null ? endAutoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "creditCardValidationErrorMessage",
            "creditCardValidationAutoDismiss",
            "creditCardValidationRequired"
    }, requireAll = false)
    public static void creditCardValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new CreditCardValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = "validationAction")
    public static void validationActionBindings(Button button, Integer action) {
        button.setTag(R.id.validation_action, action);
    }

}
