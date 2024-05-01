package ru.startandroid.develop.cashtrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddSumm extends AppCompatActivity {

    private EditText summEditText;
    private EditText categoryEditText;
    private Button minusSummButton;
    private Button plusSummButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summ);

        summEditText = findViewById(R.id.summ);
        categoryEditText = findViewById(R.id.categoria);
        minusSummButton = findViewById(R.id.minus_summ);
        plusSummButton = findViewById(R.id.plus_summ);

        minusSummButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractFromBalance();
            }
        });

        plusSummButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBalance();
            }
        });
    }

    private void subtractFromBalance() {
        String inputSumm = summEditText.getText().toString().trim();
        if (!inputSumm.isEmpty()) {
            int amount = Integer.parseInt(inputSumm);

            // Передача данных об уменьшении суммы в MainActivity
            Intent intent = new Intent();
            intent.putExtra("amount", -amount);
            setResult(Activity.RESULT_OK, intent);
            finish(); // Завершаем активити AddSummActivity
        } else {
            Toast.makeText(this, "Введите сумму для вычета", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToBalance() {
        String inputSumm = summEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();

        if (!inputSumm.isEmpty() && !category.isEmpty()) {
            int amount = Integer.parseInt(inputSumm);

            // Передача данных об увеличении суммы в MainActivity
            Intent intent = new Intent(AddSumm.this, MainActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("category", category);
            startActivityForResult(intent, 1);
            finish(); // Завершаем активити AddSummActivity
        } else {
            Toast.makeText(this, "Введите сумму и категорию", Toast.LENGTH_SHORT).show();
        }
    }
}
