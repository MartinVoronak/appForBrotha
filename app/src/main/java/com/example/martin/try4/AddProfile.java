package com.example.martin.try4;

        import android.content.Intent;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;


public class AddProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab2);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(AddProfile.this, ColorPicker.class);
                startActivity(i);
            }
        });
    }
}

