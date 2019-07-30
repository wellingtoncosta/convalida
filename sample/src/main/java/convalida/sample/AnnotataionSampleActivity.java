package convalida.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import convalida.annotations.Between;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.Cnpj;
import convalida.annotations.ConfirmEmail;
import convalida.annotations.ConfirmPassword;
import convalida.annotations.Cpf;
import convalida.annotations.CreditCard;
import convalida.annotations.Email;
import convalida.annotations.Ipv4;
import convalida.annotations.Ipv6;
import convalida.annotations.Isbn;
import convalida.annotations.Length;
import convalida.annotations.NumericLimit;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumber;
import convalida.annotations.Password;
import convalida.annotations.Pattern;
import convalida.annotations.Required;
import convalida.annotations.Url;
import convalida.annotations.ValidateOnClick;

import static convalida.library.util.Patterns.MIXED_CASE_NUMERIC;
import static convalida.sample.Constants.PHONE_PATTERN;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class AnnotataionSampleActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    @Required(errorMessageResId = R.string.field_required)
    EditText nameField;

    @Length(min = 3, errorMessageResId = R.string.min_3_characters)
    EditText nickNameField;

    @OnlyNumber(errorMessageResId = R.string.only_numbers)
    EditText ageField;

    @Pattern(pattern = PHONE_PATTERN, errorMessageResId = R.string.invalid_phone)
    EditText phoneField;

    @Cpf(errorMessageResId = R.string.invalid_cpf)
    EditText cpfField;

    @Cnpj(errorMessageResId = R.string.invalid_cnpj)
    EditText cnpjField;

    @Isbn(errorMessageResId = R.string.invalid_isbn)
    EditText isbnField;

    @Between.Start(key = 1, errorMessageResId = R.string.start_value_not_valid)
    EditText startValueField;

    @Between.Limit(key = 1, errorMessageResId = R.string.limit_value_not_valid)
    EditText limitValueField;

    @Email(errorMessageResId = R.string.invalid_email)
    EditText emailField;

    @ConfirmEmail(errorMessageResId = R.string.emails_not_match)
    EditText confirmEmailField;

    @Password(min = 3, pattern = MIXED_CASE_NUMERIC, errorMessageResId = R.string.invalid_password)
    EditText passwordField;

    @ConfirmPassword(errorMessageResId = R.string.passwords_not_match)
    EditText confirmPasswordField;

    @CreditCard(errorMessageResId = R.string.invalid_credit_card)
    EditText creditCardField;

    @NumericLimit(
            min = "0",
            max = "100",
            errorMessageResId = R.string.invalid_numeric_limit
    )
    EditText numberLimitField;

    @Ipv4(errorMessageResId = R.string.invalid_ipv4)
    EditText ipv4Field;

    @Ipv6(errorMessageResId = R.string.invalid_ipv6)
    EditText ipv6Field;

    @Url(errorMessageResId = R.string.invalid_url)
    EditText urlField;

    @ValidateOnClick Button validateButton;

    @ClearValidationsOnClick Button clearValidationsButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_sample);
        bindViews();
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.using_annotations);
        AnnotataionSampleActivityFieldsValidation.init(this);
    }

    private void bindViews() {
        constraintLayout = findViewById(R.id.constraint_layout);
        nameField = findViewById(R.id.name_field);
        nickNameField = findViewById(R.id.nickname_field);
        ageField = findViewById(R.id.age_field);
        phoneField = findViewById(R.id.phone_field);
        cpfField = findViewById(R.id.cpf_field);
        cnpjField = findViewById(R.id.cnpj_field);
        isbnField = findViewById(R.id.isbn_field);
        startValueField = findViewById(R.id.initial_period_field);
        limitValueField = findViewById(R.id.final_period_field);
        emailField = findViewById(R.id.email_field);
        confirmEmailField = findViewById(R.id.confirm_email_field);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordField = findViewById(R.id.confirm_password_field);
        creditCardField = findViewById(R.id.credit_card_field);
        numberLimitField = findViewById(R.id.numeric_limit_field);
        ipv4Field = findViewById(R.id.ipv4_field);
        ipv6Field = findViewById(R.id.ipv6_field);
        urlField = findViewById(R.id.url_field);
        validateButton = findViewById(R.id.validate_button);
        clearValidationsButton = findViewById(R.id.clear_button);
    }

    @OnValidationSuccess public void onValidationSuccess() {
        Toast.makeText(this, "Yay!", Toast.LENGTH_SHORT).show();
    }

    @OnValidationError public void onValidationError() {
        Toast.makeText(this, "Something is wrong :(", Toast.LENGTH_SHORT).show();
    }

}