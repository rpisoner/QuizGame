package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private TextView tvFinalScore;
    private TextView tvMessage;
    private TextView tvPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initializeViews();
        displayResults();
    }

    private void initializeViews() {
        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvMessage = findViewById(R.id.tvMessage);
        tvPercentage = findViewById(R.id.tvPercentage);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        Button btnMainMenu = findViewById(R.id.btnMainMenu);

        btnPlayAgain.setOnClickListener(v -> playAgain());
        btnMainMenu.setOnClickListener(v -> goToMainMenu());
    }

    private void displayResults() {
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 5);
        int maxPossibleScore = totalQuestions * 3;

        tvFinalScore.setText(getString(R.string.final_score, finalScore));

        double percentage = 0;
        if (finalScore > 0) {
            percentage = ((double) finalScore / maxPossibleScore) * 100;
        }

        tvPercentage.setText(String.format(Locale.getDefault(), "%.1f%% de respuestas correctas", percentage));

        String message;
        if (finalScore >= maxPossibleScore * 0.8) {
            message = "¡Excelente! ¡Eres un experto!";
        } else if (finalScore >= maxPossibleScore * 0.6) {
            message = "¡Muy bien! ¡Buen trabajo!";
        } else if (finalScore >= maxPossibleScore * 0.4) {
            message = "¡No está mal! Puedes mejorar.";
        } else if (finalScore >= 0) {
            message = "Sigue intentándolo, ¡puedes hacerlo mejor!";
        } else {
            message = "¡Ánimo! La práctica hace al maestro.";
        }

        tvMessage.setText(message);
    }

    private void playAgain() {
        Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainMenu() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainMenu();
    }
}
