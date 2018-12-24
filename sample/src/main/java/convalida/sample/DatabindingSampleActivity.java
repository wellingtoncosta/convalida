package convalida.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.snackbar.Snackbar;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.sample.databinding.ActivityDatabindingSampleBinding;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class DatabindingSampleActivity extends AppCompatActivity {

    private ActivityDatabindingSampleBinding binding;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_sample);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.with_databinding);
        DatabindingSampleActivityFieldsValidation.init(this, binding);
    }

    @OnValidationSuccess public void onValidationSuccess() {
        Snackbar.make(binding.getRoot(), "Yay!", Snackbar.LENGTH_LONG).show();
    }

    @OnValidationError public void onValidationError() {
        Snackbar.make(binding.getRoot(), "Something is wrong :(", Snackbar.LENGTH_LONG).show();
    }

}