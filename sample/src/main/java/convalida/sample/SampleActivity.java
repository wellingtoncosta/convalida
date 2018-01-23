package convalida.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.PasswordValidation;
import convalida.annotations.ValidateOnClick;
import convalida.library.Convalida;

import static convalida.library.util.Patterns.LOWER_UPPER_CASE_NUMERIC;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.name_layout)
    @NotEmptyValidation(R.string.field_required)
    TextInputLayout nameLayout;

    @BindView(R.id.email_layout)
    @EmailValidation(R.string.invalid_email)
    TextInputLayout emailLayout;

    @BindView(R.id.password_layout)
    @PasswordValidation(min = 3, pattern = LOWER_UPPER_CASE_NUMERIC, errorMessage = R.string.invalid_password)
    TextInputLayout passwordLayout;

    @BindView(R.id.confirm_password_layout)
    @ConfirmPasswordValidation(R.string.passwords_not_match)
    TextInputLayout confirmPasswordLayout;

    @ValidateOnClick
    @BindView(R.id.validate_button)
    Button validateButton;

    @ClearValidationsOnClick
    @BindView(R.id.clear_button)
    Button clearValidatinonsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        Convalida.init(this);
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