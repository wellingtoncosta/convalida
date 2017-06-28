package convalida.sample;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.library.Convalida;
import convalida.library.ConvalidaValidator;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class SampleActivity extends AppCompatActivity {

    private static final String PHONE_PATTERN = "^\\([1-9]{2}\\)?([0-9]{9})$";

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.name_layout)
    @NotEmptyValidation(R.string.field_required)
    TextInputLayout nameLayout;

    @BindView(R.id.email_layout)
    @EmailValidation(R.string.invalid_email)
    TextInputLayout emailLayout;

    @BindView(R.id.phone_layout)
    @PatternValidation(errorMessage = R.string.invalid_phone, pattern = PHONE_PATTERN)
    TextInputLayout phoneLayout;

    @BindView(R.id.password_layout)
    @PasswordValidation(R.string.password_required)
    TextInputLayout passwordLayout;

    @BindView(R.id.confirm_password_layout)
    @ConfirmPasswordValidation(R.string.confirm_password_not_match)
    TextInputLayout confirmPasswordLayout;

    private ConvalidaValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        validator = Convalida.initialize(this);
    }

    @OnClick(R.id.validate_button)
    public void validateFields() {
        boolean isValid = validator.validateFields();
        String message = isValid ? "Yay!" : "Something is wrong :(";
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.clear_button)
    public void clearFields() {
        validator.clearValidations();
    }
}
