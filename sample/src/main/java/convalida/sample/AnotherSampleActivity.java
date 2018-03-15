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
import convalida.annotations.LengthValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PatternValidation;
import convalida.annotations.ValidateOnClick;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class AnotherSampleActivity extends AppCompatActivity {

    private static final String PHONE_PATTERN = "[\\+]\\d{2}[\\(]\\d{2}[\\)]\\d{4}[\\-]\\d{4}";

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.name_field)
    @NotEmptyValidation(errorMessage = R.string.field_required)
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

    @ValidateOnClick
    @BindView(R.id.validate_button)
    Button validateButton;

    @ClearValidationsOnClick
    @BindView(R.id.clear_button)
    Button clearValidationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_sample);
        ButterKnife.bind(this);
        AnotherSampleActivityFieldsValidation.init(this);
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