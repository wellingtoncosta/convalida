package convalida.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        findViewById(R.id.annotations_sample).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(context, AnnotataionSampleActivity.class));
            }
        });

        findViewById(R.id.databinding_sample).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(context, DatabindingSampleActivity.class));
            }
        });

        findViewById(R.id.kotlin_dsl_sample).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(context, KotlinDslSampleActivity.class));
            }
        });
    }


}
