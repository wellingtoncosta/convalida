package convalida.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sample_1)
    public void showSample1() {
        startActivity(new Intent(this, AnnotataionSampleActivity.class));
    }

    @OnClick(R.id.sample_2)
    public void showSample2() {
        startActivity(new Intent(this, DatabindingSampleActivity.class));
    }
}
