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

    @Between.Start(key = 1, errorMessageResId = R.string.initial_period_not_valid)
    EditText initialPeriodField;

    @Between.End(key = 1, errorMessageResId = R.string.final_period_not_valid)
    EditText finalPeriodField;

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

    @NumberLimit(
            min = "0",
            max = "100",
            errorMessageResId = R.string.invalid_number_limit
    )
    EditText numberLimitField;

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