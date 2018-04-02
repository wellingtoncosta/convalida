package convalida.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.databinding.ValidationUtils;
import convalida.sample.databinding.ActivityDatabindingSampleBinding;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class DatabindingSampleActivity extends AppCompatActivity {

    private ActivityDatabindingSampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_sample);
        binding.validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = ValidationUtils.validateFields(binding.getRoot());
                if(isValid) {
                    Snackbar.make(
                            binding.getRoot(),
                            "Yay!",
                            Snackbar.LENGTH_LONG
                    ).show();
                } else {
                    Snackbar.make(
                            binding.getRoot(),
                            "Something is wrong :(",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }
        });
        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationUtils.clearValidations(binding.getRoot());
            }
        });
    }

    @OnValidationSuccess
    public void onValidationSuccess() {
        Snackbar.make(binding.getRoot(), "Yay!", Snackbar.LENGTH_LONG).show();
    }

    @OnValidationError
    public void onValidationError() {
        Snackbar.make(binding.getRoot(), "Something is wrong :(", Snackbar.LENGTH_LONG).show();
    }

}