package ru.startandroid.develop.cashtrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageView imageView2;
    private EditText nameEditText;
    private EditText emailEditText;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        imageView2 = findViewById(R.id.imageView2);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email_for_profile);

        userId = mAuth.getCurrentUser().getUid();

        loadProfileData();

        // Обработка выбора изображения из галереи
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        // Обработка сохранения имени
        ImageView penImageView = findViewById(R.id.pen);
        penImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileData();
            }
        });

        // Обработка выхода из аккаунта
        ImageView exitImageView = findViewById(R.id.exit);
        exitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Profile.this, LoginActivity.class));
                finish();
            }
        });

        // Обработка перехода на MainActivity при нажатии на arrowImageView
        ImageView arrowImageView = findViewById(R.id.arrow);
        arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                finish(); // Закрываем текущую активность Profile
            }
        });
    }

    private void loadProfileData() {
        DocumentReference docRef = db.collection("profiles").document(userId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String profileImage = documentSnapshot.getString("profileImage");

                            nameEditText.setText(name);
                            emailEditText.setText(email);

                            // Загрузка изображения профиля, если есть
                            if (profileImage != null && !profileImage.isEmpty()) {
                                Uri imageUri = Uri.parse(profileImage);
                                imageView2.setImageURI(imageUri);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Ошибка загрузки данных профиля", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveProfileData() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // Создаем или обновляем документ в коллекции "profiles" с идентификатором userId
        DocumentReference docRef = db.collection("profiles").document(userId);
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("email", email);

        docRef.set(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Profile.this, "Данные профиля сохранены", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Ошибка сохранения данных профиля", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imageView2.setImageURI(uri); // Установка выбранного изображения в imageView2

            // Сохранение ссылки на изображение в базе данных Firestore
            DocumentReference docRef = db.collection("profiles").document(userId);
            docRef.update("profileImage", uri.toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Profile.this, "Изображение профиля сохранено", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Ошибка сохранения изображения профиля", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
