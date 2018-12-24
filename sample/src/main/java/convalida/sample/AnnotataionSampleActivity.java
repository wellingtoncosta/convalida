package convalida.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import convalida.annotations.*;

import static convalida.library.util.Patterns.MIXED_CASE_NUMERIC;
import static convalida.sample.Constants.PHONE_PATTERN;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class AnnotataionSampleActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    @RequiredValidation(errorMessage = R.string.field_required)
    EditText nameField;

    @LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
    EditText nickNameField;

    @OnlyNumberValidation(errorMessage = R.string.only_numbers)
    EditText ageField;

    @PatternValidation(pattern = PHONE_PATTERN, errorMessage = R.string.invalid_phone)
    EditText phoneField;

    @CpfValidation(errorMessage = R.string.cpf_not_valid)
    EditText cpfField;

    @BetweenValidation.Start(key = 1, errorMessage = R.string.initial_period_not_valid)
    EditText initialPeriodField;

    @BetweenValidation.End(key = 1, errorMessage = R.string.final_period_not_valid)
    EditText finalPeriodField;

    @EmailValidation(errorMessage = R.string.invalid_email)
    EditText emailField;

    @ConfirmEmailValidation(errorMessage = R.string.emails_not_match)
    EditText confirmEmailField;

    @PasswordValidation(min = 3, pattern = MIXED_CASE_NUMERIC, errorMessage = R.string.invalid_password)
    EditText passwordField;

    @ConfirmPasswordValidation(errorMessage = R.string.passwords_not_match)
    EditText confirmPasswordField;

    @CreditCardValidation(errorMessage = R.string.invalid_credit_card)
    EditText creditCardField;

    @NumberLimitValidation(
            min = "0",
            max = "100",
            errorMessage = R.string.invalid_number_limit
    )
    EditText numberLimitField;

    @ValidateOnClick Button validateButton;

    @ClearValidationsOnClick Button clearValidationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_sample);
        bindViews();
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.with_annotations);
        AnnotataionSampleActivityFieldsValidation.init(this);
    }

    private void bindViews() {
        constraintLayout = findViewById(R.id.constraint_layout);
        nameField = findViewById(R.id.name_field);
        nickNameField = findViewById(R.id.nickname_field);
        ageField = findViewById(R.id.age_field);
        phoneField = findViewById(R.id.phone_field);
        cpfField = findViewById(R.id.cpf_field);
        initialPeriodField = findViewById(R.id.initial_period_field);
        finalPeriodField = findViewById(R.id.final_period_field);
        emailField = findViewById(R.id.email_field);
        confirmEmailField = findViewById(R.id.confirm_email_field);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordField = findViewById(R.id.confirm_password_field);
        creditCardField = findViewById(R.id.credit_card_field);
        numberLimitField = findViewById(R.id.number_limit_field);
        validateButton = findViewById(R.id.validate_button);
        clearValidationsButton = findViewById(R.id.clear_button);
    }

    @OnValidationSuccess public void onValidationSuccess() {
        Snackbar.make(constraintLayout, "Yay!", Snackbar.LENGTH_LONG).show();
    }

    @OnValidationError public void onValidationError() {
        Snackbar.make(constraintLayout, "Something is wrong :(", Snackbar.LENGTH_LONG).show();
    }

}