package convalida.sample;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.NotEmptyValidation;
import convalida.library.Convalida;

public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.activity_sample)
    LinearLayout linearLayout;

    @NotEmptyValidation
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.phone_layout)
    TextInputLayout phoneLayout;

    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        Convalida.init(this);
    }

    @OnClick(R.id.validate_button)
    public void validateFields() {
        boolean isValid = Convalida.validate();
        Snackbar.make(linearLayout, "Is valid: " + isValid, Snackbar.LENGTH_LONG).show();
    }
}
