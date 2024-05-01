package ru.startandroid.develop.cashtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Goal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal);
        ImageView Back = findViewById(R.id.arrow);
        Intent l = new Intent(this, MainActivity.class);
        ImageView Plus = findViewById(R.id.plus);
        Intent j = new Intent(this, AddSumm.class);
        ImageView Task = findViewById(R.id.task);
        Intent k = new Intent(this, History.class);
        ImageView Ruble = findViewById(R.id.ruble);
        Intent h = new Intent(this, TwoButtons.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(l);
                }
            });
            Ruble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(h);
                }
            });
            Task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(k);
                }
            });
            Plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(j);
                }
            });
            return insets;
        });
    }
}