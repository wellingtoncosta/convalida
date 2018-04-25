package convalida.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
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
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.with_databinding);
        DatabindingSampleActivityFieldsValidation.init(this, binding);
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