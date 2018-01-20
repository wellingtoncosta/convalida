package convalida.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.LengthValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PatternValidation;
import convalida.library.Convalida;
import convalida.validators.ConvalidaValidator;

public class AnotherSampleActivity extends AppCompatActivity {

    private static final String PHONE_PATTERN = "[\\+]\\d{2}[\\(]\\d{2}[\\)]\\d{4}[\\-]\\d{4}";

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.name_layout)
    @NotEmptyValidation(R.string.field_required)
    TextInputLayout nameLayout;

    @BindView(R.id.nickname_layout)
    @LengthValidation(min = 3, errorMessage = R.string.min_3_characters)
    TextInputLayout nickNameLayout;

    @BindView(R.id.age_layout)
    @OnlyNumberValidation(R.string.only_numbers)
    TextInputLayout ageLayout;

    @BindView(R.id.phone_layout)
    @PatternValidation(pattern = PHONE_PATTERN, errorMessage = R.string.invalid_phone)
    TextInputLayout phoneLayout;

    private ConvalidaValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_sample);
        ButterKnife.bind(this);
        validator = Convalida.init(this);
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
