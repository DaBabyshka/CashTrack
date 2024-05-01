package ru.startandroid.develop.cashtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private ImageView taskImageView;
    private ImageView plusImageView;
    private ImageView rubleImageView;
    private ProgressBar progressBar;
    private TextView balanceTextView;
    private int currentBalance = 0;

    private static final int GOAL_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImageView = findViewById(R.id.profile);
        taskImageView = findViewById(R.id.task);
        plusImageView = findViewById(R.id.plus);
        rubleImageView = findViewById(R.id.ruble);
        progressBar = findViewById(R.id.progress);
        balanceTextView = findViewById(R.id.balance);

        if (balanceTextView != null && balanceTextView.getText() != null && !balanceTextView.getText().toString().isEmpty()) {
            try {
                currentBalance = Integer.parseInt(balanceTextView.getText().toString());
            } catch (NumberFormatException e) {
                currentBalance = 0;
            }
        } else {
            currentBalance = 0;
        }
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });

        taskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, History.class));
            }
        });

        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddSumm.class), GOAL_REQUEST_CODE);
            }
        });

        rubleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TwoButtons.class));
            }
        });

        progressBar.setMax(10000000);
        progressBar.setProgress(0);

        int amountFromIntent = getIntent().getIntExtra("amount", 0);
        if (amountFromIntent != 0) {
            currentBalance += amountFromIntent;
            balanceTextView.setText(String.valueOf(currentBalance));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOAL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int summ = data.getIntExtra("summ", 0);
            String categoria = data.getStringExtra("categoria");

            if (categoria != null) {
                if (categoria.equals("minus")) {
                    currentBalance -= summ;
                } else if (categoria.equals("plus")) {
                    currentBalance += summ;
                }

                balanceTextView.setText(String.valueOf(currentBalance));
            }
        }
    }
}
