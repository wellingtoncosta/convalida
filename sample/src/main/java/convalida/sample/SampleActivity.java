package convalida.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.ConfirmEmailValidation;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.PasswordValidation;
import convalida.annotations.ValidateOnClick;

import static convalida.library.util.Patterns.MIXED_CASE_NUMERIC;


/**
 * @author Wellington Costa on 05/06/17.
 */
public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.name_field)
    @NotEmptyValidation(errorMessage = R.string.field_required)
    EditText nameField;

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

    @ValidateOnClick
    @BindView(R.id.validate_button)
    Button validateButton;

    @ClearValidationsOnClick
    @BindView(R.id.clear_button)
    Button clearValidationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        SampleActivityFieldsValidation.init(this);
    }

    @OnValidationSuccess
    public void onValidationSuccess() {
        Snackbar.make(linearLayout, "Yay!", Snackbar.LENGTH_LONG).show();
    }

    @OnValidationError
    public void onValidationError() {
        Snackbar.make(linearLayout, "Something is wrong :(", Snackbar.LENGTH_LONG).show();
    }

}