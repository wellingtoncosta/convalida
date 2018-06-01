package convalida.compiler;

import com.squareup.javapoet.ClassName;

/**
 * @author Wellington Costa on 26/10/2017.
 */
class Constants {

    static final ClassName LIST                         = ClassName.get("java.util", "List");
    static final ClassName OVERRIDE                     = ClassName.get("java.lang", "Override");

    static final ClassName UI_THREAD                    = ClassName.get("android.support.annotation", "UiThread");
    static final ClassName NON_NULL                     = ClassName.get("android.support.annotation", "NonNull");
    static final ClassName VIEW                         = ClassName.get("android.view", "View");
    static final ClassName VIEWGROUP                    = ClassName.get("android.view", "ViewGroup");
    static final ClassName BUTTON                       = ClassName.get("android.widget", "Button");
    static final ClassName VIEW_ONCLICK_LISTENER        = ClassName.get("android.view", "View.OnClickListener");
    static final ClassName VIEW_DATA_BINDING            = ClassName.get("android.databinding", "ViewDataBinding");

    static final ClassName VIEW_TAG_UTILS               = ClassName.get("convalida.databinding", "ViewTagUtils");
    static final ClassName CONVALIDA_DATABINDING_R      = ClassName.get("convalida.databinding", "R");
    static final ClassName ABSTRACT_VALIDATOR           = ClassName.get("convalida.validators", "AbstractValidator");
    static final ClassName VALIDATOR_SET                = ClassName.get("convalida.validators", "ValidatorSet");

    static final String EDIT_TEXT_TYPE                  = "android.widget.EditText";

    static final String REQUIRED_ANNOTATION                         = "convalida.annotations.RequiredValidation";
    static final String EMAIL_ANNOTATION                            = "convalida.annotations.EmailValidation";
    static final String CONFIRM_EMAIL_VALIDATION                    = "convalida.annotations.ConfirmEmailValidation";
    static final String PATTERN_ANNOTATION                          = "convalida.annotations.PatternValidation";
    static final String LENGTH_ANNOTATION                           = "convalida.annotations.LengthValidation";
    static final String ONLY_NUMBER_ANNOTATION                      = "convalida.annotations.OnlyNumberValidation";
    static final String PASSWORD_ANNOTATION                         = "convalida.annotations.PasswordValidation";
    static final String CONFIRM_PASSWORD_ANNOTATION                 = "convalida.annotations.ConfirmPasswordValidation";
    static final String CPF_ANNOTATION                              = "convalida.annotations.CpfValidation";
    static final String BETWEEN_ANNOTATION                          = "convalida.annotations.BetweenValidation.Start";
    static final String BETWEEN_START_ANNOTATION                    = "convalida.annotations.BetweenValidation.Start";
    static final String BETWEEN_END_ANNOTATION                      = "convalida.annotations.BetweenValidation.End";
    static final String CREDIT_CARD_ANNOTATION                      = "convalida.annotations.CreditCardValidation";
    static final String VALIDATE_ON_CLICK_ANNOTATION                = "convalida.annotations.ValidateOnClick";
    static final String CLEAR_VALIDATIONS_ON_CLICK_ANNOTATION       = "convalida.annotations.ClearValidationsOnClick";
    static final String ON_VALIDATION_SUCCESS_ANNOTATION            = "convalida.annotations.OnValidationSuccess";
    static final String ON_VALIDATION_ERROR_ANNOTATION              = "convalida.annotations.OnValidationError";

    static final ClassName REQUIRED_VALIDATOR           = ClassName.get("convalida.validators", "RequiredValidator");
    static final ClassName EMAIL_VALIDATOR              = ClassName.get("convalida.validators", "EmailValidator");
    static final ClassName CONFIRM_EMAIL_VALIDATOR      = ClassName.get("convalida.validators", "ConfirmEmailValidator");
    static final ClassName PATTERN_VALIDATOR            = ClassName.get("convalida.validators", "PatternValidator");
    static final ClassName LENGTH_VALIDATOR             = ClassName.get("convalida.validators", "LengthValidator");
    static final ClassName ONLY_NUMBER_VALIDATOR        = ClassName.get("convalida.validators", "OnlyNumberValidator");
    static final ClassName PASSWORD_VALIDATOR           = ClassName.get("convalida.validators", "PasswordValidator");
    static final ClassName CONFIRM_PASSWORD_VALIDATOR   = ClassName.get("convalida.validators", "ConfirmPasswordValidator");
    static final ClassName CPF_VALIDATOR                = ClassName.get("convalida.validators", "CpfValidator");
    static final ClassName BETWEEN_VALIDATOR            = ClassName.get("convalida.validators", "BetweenValidator");
    static final ClassName CREDIT_CARD_VALIDATOR        = ClassName.get("convalida.validators", "CreditCardValidator");

}