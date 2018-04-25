package convalida.sample;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.ConfirmEmailValidation;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.CpfValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.annotations.RequiredValidation;
import convalida.annotations.ValidateOnClick;

import static convalida.library.util.Patterns.MIXED_CASE_NUMERIC;
import static convalida.sample.Constants.PHONE_PATTERN;


/**
 * @author Wellington Costa on 05/06/17.
 */
public class AnnotataionSampleActivity extends AppCompatActivity {

    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.name_field)
    @RequiredValidation(errorMessage = R.string.field_required)
    EditText nameField;

    @BindView(R.id.nickname_field)
    @LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
    EditText nickNameField;

    @BindView(R.id.age_field)
    @OnlyNumberValidation(errorMessage = R.string.only_numbers)
    EditText ageField;

    @BindView(R.id.phone_field)
    @PatternValidation(pattern = PHONE_PATTERN, errorMessage = R.string.invalid_phone)
    EditText phoneField;

    @BindView(R.id.email_field)
    @EmailValidation(errorMessage = R.string.invalid_email)
    EditText emailField;

    @BindView(R.id.confirm_email_field)
    @ConfirmEmailValidation(errorMessage = R.string.emails_not_match)
    EditText confirmEmailField;

    @BindView(R.id.password_field)
    @PasswordValidation(min = 3, pattern = MIXED_CASE_NUMERIC, errorMessage = R.string.invalid_password)
    EditText passwordField;

    @BindView(R.id.confirm_password_field)
    @ConfirmPasswordValidation(errorMessage = R.string.passwords_not_match)
    EditText confirmPasswordField;

    @BindView(R.id.cpf_field)
    @CpfValidation(errorMessage = R.string.cpf_not_valid)
    EditText cpfField;

    @ValidateOnClick
    @BindView(R.id.validate_button)
    Button validateButton;

    @ClearValidationsOnClick
    @BindView(R.id.clear_button)
    Button clearValidationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_sample);
        ButterKnife.bind(this);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.with_annotations);
        AnnotataionSampleActivityFieldsValidation.init(this);
    }

    @OnValidationSuccess
    public void onValidationSuccess() {
        Snackbar.make(constraintLayout, "Yay!", Snackbar.LENGTH_LONG).show();
    }

    @OnValidationError
    public void onValidationError() {
        Snackbar.make(constraintLayout, "Something is wrong :(", Snackbar.LENGTH_LONG).show();
    }

}