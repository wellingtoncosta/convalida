package convalida.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.sample.databinding.ActivityDatabindingSampleBinding;

/**
 * @author Wellington Costa on 05/06/17.
 */
public class DatabindingSampleActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDatabindingSampleBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_databinding_sample);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.using_databinding);

        DatabindingSampleActivityFieldsValidation.init(this, binding);
    }

    @OnValidationSuccess public void onValidationSuccess() {
        Toast.makeText(this, "Yay!", Toast.LENGTH_SHORT).show();
    }

    @OnValidationError public void onValidationError() {
        Toast.makeText(this, "Something is wrong :(", Toast.LENGTH_SHORT).show();
    }

}