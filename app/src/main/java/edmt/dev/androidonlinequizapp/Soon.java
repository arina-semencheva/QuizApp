package edmt.dev.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Soon extends AppCompatActivity {
    Button btnTryAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soon);

        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Soon.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
