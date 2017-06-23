package convalida.sample;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.library.Convalida;
import convalida.library.ConvalidaValidator;

public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @NotEmptyValidation(errorMessage = "Campo obrigatório")
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @EmailValidation(errorMessage = "E-mail inválido")
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @PatternValidation(
            errorMessage = "Telefone inválido",
            pattern = "^\\([1-9]{2}\\)?([0-9]{9})$")
    @BindView(R.id.phone_layout)
    TextInputLayout phoneLayout;

    @PasswordValidation(errorMessage = "Senha obrigatória")
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

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
        String message = isValid ? "Campos validados" : "Campos inválidos";
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.clear_button)
    public void clearFields() {
        validator.clearValidations();
    }
}
