 package convalida.databinding;

 import android.widget.Button;
 import android.widget.EditText;
 import androidx.annotation.NonNull;
 import androidx.databinding.BindingAdapter;
 import convalida.validators.*;

/**
 * @author WellingtonCosta on 29/03/18.
 */
public class ValidationBindings {

    @BindingAdapter(value = {
            "requiredErrorMessage",
            "requiredAutoDismiss"
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
            "emailErrorMessage",
            "emailAutoDismiss",
            "emailRequired"
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
            "confirmEmailEmailField",
            "confirmEmailErrorMessage",
            "confirmEmailAutoDismiss"
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
            "patternErrorMessage",
            "patternPattern",
            "patternAutoDismiss",
            "patternRequired"
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
            "lengthMin",
            "lengthMax",
            "lengthErrorMessage",
            "lengthAutoDismiss",
            "lengthRequired"
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
                errorMessage,
                min,
                max,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "onlyNumberErrorMessage",
            "onlyNumberAutoDismiss",
            "onlyNumberRequired"
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
            "passwordErrorMessage",
            "passwordMinLength",
            "passwordPattern",
            "passwordAutoDismiss"
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
                errorMessage,
                minLength != null ? minLength : 0,
                pattern != null ? pattern : "",
                autoDismiss != null ? autoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "confirmPasswordPasswordField",
            "confirmPasswordErrorMessage",
            "confirmPasswordAutoDismiss"
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
            "cpfErrorMessage",
            "cpfAutoDismiss",
            "cpfRequired"
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
            "cnpjErrorMessage",
            "cnpjAutoDismiss",
            "cnpjRequired"
    }, requireAll = false)
    public static void cnpjValidationBindings(
            @NonNull EditText cnpj,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        cnpj.setTag(R.id.validation_type, new CnpjValidator(
                cnpj,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "isbnErrorMessage",
            "isbnAutoDismiss",
            "isbnRequired"
    }, requireAll = false)
    public static void isbnValidationBindings(
            @NonNull EditText isbnField,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        isbnField.setTag(R.id.validation_type, new IsbnValidator(
                isbnField,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "betweenStartErrorMessage",
            "betweenStartAutoDismiss",
            "betweenLimitField",
            "betweenLimitErrorMessage",
            "betweenLimitAutoDismiss"
    }, requireAll = false)
    public static void betweenValidationBindings(
            @NonNull EditText startField,
            @NonNull String startErrorMessage,
            Boolean startAutoDismiss,
            EditText limitField,
            @NonNull String limitErrorMessage,
            Boolean limitAutoDismiss
    ) {
        startField.setTag(R.id.validation_type, new BetweenValidator(
                startField,
                limitField,
                startErrorMessage,
                limitErrorMessage,
                startAutoDismiss != null ? startAutoDismiss : true,
                limitAutoDismiss != null ? limitAutoDismiss : true
        ));
    }

    @BindingAdapter(value = {
            "creditCardErrorMessage",
            "creditCardAutoDismiss",
            "creditCardRequired"
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

    @BindingAdapter(value = {
            "numericLimitErrorMessage",
            "numericLimitAutoDismiss",
            "numericLimitMin",
            "numericLimitMax",
            "numericLimitRequired"
    }, requireAll = false)
    public static void numericLimitValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            @NonNull String min,
            @NonNull String max,
            Boolean required
    ) {
        field.setTag(R.id.validation_type, new NumericLimitValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                min,
                max,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = "validationAction")
    public static void validationActionBindings(Button button, Integer action) {
        button.setTag(R.id.validation_action, action);
    }

}
