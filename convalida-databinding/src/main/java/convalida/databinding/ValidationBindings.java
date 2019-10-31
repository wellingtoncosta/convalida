 package convalida.databinding;

 import android.widget.Button;
 import android.widget.EditText;

 import androidx.annotation.NonNull;
 import androidx.databinding.BindingAdapter;

 import convalida.validators.BetweenValidator;
 import convalida.validators.CnpjValidator;
 import convalida.validators.ConfirmEmailValidator;
 import convalida.validators.ConfirmPasswordValidator;
 import convalida.validators.CpfValidator;
 import convalida.validators.CreditCardValidator;
 import convalida.validators.EmailValidator;
 import convalida.validators.FutureDateValidator;
 import convalida.validators.Ipv4Validator;
 import convalida.validators.Ipv6Validator;
 import convalida.validators.IsbnValidator;
 import convalida.validators.LengthValidator;
 import convalida.validators.NumericLimitValidator;
 import convalida.validators.OnlyNumberValidator;
 import convalida.validators.PasswordValidator;
 import convalida.validators.PastDateValidator;
 import convalida.validators.PatternValidator;
 import convalida.validators.RequiredValidator;
 import convalida.validators.UrlValidator;

 import static convalida.databinding.ViewTagUtils.addValidatorToField;

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
        addValidatorToField(field, new RequiredValidator(
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
        addValidatorToField(field, new EmailValidator(
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
        addValidatorToField(confirmEmailField, new ConfirmEmailValidator(
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
        addValidatorToField(field, new PatternValidator(
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
        addValidatorToField(field, new LengthValidator(
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
        addValidatorToField(field, new OnlyNumberValidator(
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
        addValidatorToField(field, new PasswordValidator(
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
        addValidatorToField(confirmPasswordField, new ConfirmPasswordValidator(
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
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new CpfValidator(
                field,
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
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new CnpjValidator(
                field,
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
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new IsbnValidator(
                field,
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
        addValidatorToField(startField, new BetweenValidator(
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
        addValidatorToField(field, new CreditCardValidator(
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
        addValidatorToField(field, new NumericLimitValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                min,
                max,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "ipv4ErrorMessage",
            "ipv4AutoDismiss",
            "ipv4Required"
    }, requireAll = false)
    public static void ipv4ValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new Ipv4Validator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "ipv6ErrorMessage",
            "ipv6AutoDismiss",
            "ipv6Required"
    }, requireAll = false)
    public static void ipv6ValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new Ipv6Validator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "urlErrorMessage",
            "urlAutoDismiss",
            "urlRequired"
    }, requireAll = false)
    public static void urlValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new UrlValidator(
                field,
                errorMessage,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

    @BindingAdapter(value = {
            "pastDateErrorMessage",
            "pastDateDateFormat",
            "pastDateLimitDate",
            "pastDateAutoDismiss",
            "pastDateRequired"
    }, requireAll = false)
    public static void pastDateValidationBindings(
            @NonNull EditText field,
            @NonNull String errorMessage,
            @NonNull String dateFormat,
            @NonNull String limitDate,
            Boolean autoDismiss,
            Boolean required
    ) {
        addValidatorToField(field, new PastDateValidator(
                field,
                errorMessage,
                dateFormat,
                limitDate,
                autoDismiss != null ? autoDismiss : true,
                required != null ? required : true
        ));
    }

     @BindingAdapter(value = {
             "futureDateErrorMessage",
             "futureDateDateFormat",
             "futureDateLimitDate",
             "futureDateAutoDismiss",
             "futureDateRequired"
     }, requireAll = false)
     public static void futureDateValidationBindings(
             @NonNull EditText field,
             @NonNull String errorMessage,
             @NonNull String dateFormat,
             @NonNull String limitDate,
             Boolean autoDismiss,
             Boolean required
     ) {
         addValidatorToField(field, new FutureDateValidator(
                 field,
                 errorMessage,
                 dateFormat,
                 limitDate,
                 autoDismiss != null ? autoDismiss : true,
                 required != null ? required : true
         ));
     }

    @BindingAdapter(value = "validationAction")
    public static void validationActionBindings(Button button, Integer action) {
        button.setTag(R.id.validation_action, action);
    }

}
