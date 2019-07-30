package convalida.compiler;

import com.squareup.javapoet.ClassName;

import java.util.List;

/**
 * @author Wellington Costa on 26/10/2017.
 */
class Constants {

    static final ClassName LIST                                     = ClassName.get(List.class);
    static final ClassName OVERRIDE                                 = ClassName.get(Override.class);

    static final ClassName UI_THREAD                                = ClassName.get("androidx.annotation", "UiThread");
    static final ClassName NON_NULL                                 = ClassName.get("androidx.annotation", "NonNull");
    static final ClassName VIEW                                     = ClassName.get("android.view", "View");
    static final ClassName VIEWGROUP                                = ClassName.get("android.view", "ViewGroup");
    static final ClassName EDIT_TEXT                                = ClassName.get("android.widget", "EditText");
    static final ClassName BUTTON                                   = ClassName.get("android.widget", "Button");
    static final ClassName VIEW_ONCLICK_LISTENER                    = ClassName.get("android.view", "View.OnClickListener");
    static final ClassName VIEW_DATA_BINDING                        = ClassName.get("androidx.databinding", "ViewDataBinding");

    static final ClassName VIEW_TAG_UTILS                           = ClassName.get("convalida.databinding", "ViewTagUtils");
    static final ClassName CONVALIDA_DATABINDING_R                  = ClassName.get("convalida.databinding", "R");
    static final ClassName ABSTRACT_VALIDATOR                       = ClassName.get("convalida.validators", "AbstractValidator");
    static final ClassName VALIDATOR_SET                            = ClassName.get("convalida.validators", "ValidatorSet");
    static final ClassName VALIDATION_ERROR                         = ClassName.get("convalida.validators.error", "ValidationErrorSet");

    static final String REQUIRED_ANNOTATION                         = "convalida.annotations.Required";
    static final String EMAIL_ANNOTATION                            = "convalida.annotations.Email";
    static final String CONFIRM_EMAIL_VALIDATION                    = "convalida.annotations.ConfirmEmail";
    static final String PATTERN_ANNOTATION                          = "convalida.annotations.Pattern";
    static final String LENGTH_ANNOTATION                           = "convalida.annotations.Length";
    static final String ONLY_NUMBER_ANNOTATION                      = "convalida.annotations.OnlyNumber";
    static final String PASSWORD_ANNOTATION                         = "convalida.annotations.Password";
    static final String CONFIRM_PASSWORD_ANNOTATION                 = "convalida.annotations.ConfirmPassword";
    static final String CPF_ANNOTATION                              = "convalida.annotations.Cpf";
    static final String CNPJ_ANNOTATION                             = "convalida.annotations.Cnpj";
    static final String ISBN_ANNOTATION                             = "convalida.annotations.Isbn";
    static final String BETWEEN_ANNOTATION                          = "convalida.annotations.Between";
    static final String CREDIT_CARD_ANNOTATION                      = "convalida.annotations.CreditCard";
    static final String NUMERIC_LIMIT_ANNOTATION                    = "convalida.annotations.NumericLimit";
    static final String IPV4_ANNOTATION                             = "convalida.annotations.Ipv4";
    static final String IPV6_ANNOTATION                             = "convalida.annotations.Ipv6";

    static final ClassName REQUIRED_VALIDATOR                       = ClassName.get("convalida.validators", "RequiredValidator");
    static final ClassName EMAIL_VALIDATOR                          = ClassName.get("convalida.validators", "EmailValidator");
    static final ClassName CONFIRM_EMAIL_VALIDATOR                  = ClassName.get("convalida.validators", "ConfirmEmailValidator");
    static final ClassName PATTERN_VALIDATOR                        = ClassName.get("convalida.validators", "PatternValidator");
    static final ClassName LENGTH_VALIDATOR                         = ClassName.get("convalida.validators", "LengthValidator");
    static final ClassName ONLY_NUMBER_VALIDATOR                    = ClassName.get("convalida.validators", "OnlyNumberValidator");
    static final ClassName PASSWORD_VALIDATOR                       = ClassName.get("convalida.validators", "PasswordValidator");
    static final ClassName CONFIRM_PASSWORD_VALIDATOR               = ClassName.get("convalida.validators", "ConfirmPasswordValidator");
    static final ClassName CPF_VALIDATOR                            = ClassName.get("convalida.validators", "CpfValidator");
    static final ClassName CNPJ_VALIDATOR                           = ClassName.get("convalida.validators", "CnpjValidator");
    static final ClassName ISBN_VALIDATOR                           = ClassName.get("convalida.validators", "IsbnValidator");
    static final ClassName BETWEEN_VALIDATOR                        = ClassName.get("convalida.validators", "BetweenValidator");
    static final ClassName CREDIT_CARD_VALIDATOR                    = ClassName.get("convalida.validators", "CreditCardValidator");
    static final ClassName NUMERIC_LIMIT_VALIDATOR                  = ClassName.get("convalida.validators", "NumericLimitValidator");
    static final ClassName IPV4_VALIDATOR                           = ClassName.get("convalida.validators", "Ipv4Validator");
    static final ClassName IPV6_VALIDATOR                           = ClassName.get("convalida.validators", "Ipv6Validator");

}