package convalida.sample;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.NotEmptyValidation;
import convalida.library.Convalida;
import convalida.library.Validator;

public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @NotEmptyValidation
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @NotEmptyValidation
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @NotEmptyValidation
    @BindView(R.id.phone_layout)
    TextInputLayout phoneLayout;

    @NotEmptyValidation
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        validator = Convalida.init(this);
    }

    @OnClick(R.id.validate_button)
    public void validateFields() {
        validator.validate();
    }

    @OnClick(R.id.clear_button)
    public void clearFields() {
        validator.clear();
    }
}
