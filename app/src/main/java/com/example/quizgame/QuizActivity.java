package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvQuestionNumber;
    private ImageView ivQuestionImage;
    private LinearLayout controlContainer;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedAnswer = -1;

    private static final int POINTS_CORRECT = 3;
    private static final int POINTS_INCORRECT = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initializeViews();
        initializeQuestions();
        displayQuestion();
    }

    private void initializeViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);
        controlContainer = findViewById(R.id.controlContainer);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnRestart = findViewById(R.id.btnRestart);

        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnRestart.setOnClickListener(v -> restartGame());
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        // Pregunta 1: RadioButton - Texto simple
        questions.add(new Question(
                "¿Cuál es la capital de Francia?",
                Arrays.asList("Londres", "Berlín", "París", "Madrid"),
                2,
                Question.ControlType.RADIO_BUTTON
        ));

        // Pregunta 2: Spinner - Cine
        questions.add(new Question(
                "¿Quién dirigió la película 'Inception'?",
                Arrays.asList("Steven Spielberg", "Christopher Nolan", "Martin Scorsese", "Quentin Tarantino"),
                1,
                Question.ControlType.SPINNER
        ));

        // Pregunta 3: ListView - Cultura general
        questions.add(new Question(
                "¿En qué año llegó el hombre a la Luna?",
                Arrays.asList("1965", "1969", "1972", "1975"),
                1,
                Question.ControlType.LIST_VIEW
        ));

        // Pregunta 4: RadioButton con imagen
        questions.add(new Question(
                "¿Qué planeta es este?",
                R.drawable.planet_saturn,
                Arrays.asList("Júpiter", "Saturno", "Urano", "Neptuno"),
                null,
                1,
                Question.ControlType.RADIO_BUTTON
        ));

        // Pregunta 5: Spinner - Arte
        questions.add(new Question(
                "¿Quién pintó 'La Noche Estrellada'?",
                Arrays.asList("Pablo Picasso", "Claude Monet", "Vincent van Gogh", "Leonardo da Vinci"),
                2,
                Question.ControlType.SPINNER
        ));
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishQuiz();
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        selectedAnswer = -1;

        // Actualizar información de la pregunta
        tvQuestion.setText(question.getQuestionText());
        tvQuestionNumber.setText(getString(R.string.question_number,
                currentQuestionIndex + 1, questions.size()));
        updateScore();

        // Mostrar imagen si existe
        if (question.hasQuestionImage()) {
            ivQuestionImage.setVisibility(View.VISIBLE);
            ivQuestionImage.setImageResource(question.getQuestionImageId());
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        // Limpiar contenedor de controles
        controlContainer.removeAllViews();

        // Crear el control apropiado según el tipo
        switch (question.getControlType()) {
            case RADIO_BUTTON:
                createRadioButtons(question);
                break;
            case SPINNER:
                createSpinner(question);
                break;
            case LIST_VIEW:
                createListView(question);
                break;
        }
    }

    private void createRadioButtons(Question question) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(i);
            radioButton.setTextSize(16);
            radioButton.setPadding(16, 16, 16, 16);
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> selectedAnswer = checkedId);

        controlContainer.addView(radioGroup);
    }

    private void createSpinner(Question question) {
        TextView label = new TextView(this);
        label.setText("Selecciona una opción:");
        label.setTextSize(16);
        label.setPadding(16, 16, 16, 16);
        controlContainer.addView(label);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                question.getOptions()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPadding(16, 8, 16, 8);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswer = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAnswer = -1;
            }
        });

        controlContainer.addView(spinner);
    }

    private void createListView(Question question) {
        TextView label = new TextView(this);
        label.setText("Toca una opción:");
        label.setTextSize(16);
        label.setPadding(16, 16, 16, 16);
        controlContainer.addView(label);

        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                question.getOptions()
        );
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        ));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedAnswer = position;
            listView.setItemChecked(position, true);
        });

        controlContainer.addView(listView);
    }

    private void checkAnswer() {
        if (selectedAnswer == -1) {
            Toast.makeText(this, "Por favor, selecciona una respuesta", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedAnswer == question.getCorrectAnswerIndex();

        if (isCorrect) {
            score += POINTS_CORRECT;
            showFeedbackDialog("¡Correcto!", "¡Excelente! Has ganado " + POINTS_CORRECT + " puntos.", true);
        } else {
            score += POINTS_INCORRECT;
            showFeedbackDialog("Incorrecto", "La respuesta correcta era: " +
                    question.getOptions().get(question.getCorrectAnswerIndex()) +
                    "\nSe han restado " + Math.abs(POINTS_INCORRECT) + " puntos.", false);
        }

        updateScore();
    }

    private void showFeedbackDialog(String title, String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        if (isCorrect) {
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                currentQuestionIndex++;
                displayQuestion();
            });
        } else {
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                currentQuestionIndex++;
                displayQuestion();
            });
            builder.setNegativeButton("Reiniciar", (dialog, which) -> restartGame());
        }

        builder.setCancelable(false);
        builder.show();
    }

    private void updateScore() {
        tvScore.setText(getString(R.string.current_score, score));
    }

    private void restartGame() {
        currentQuestionIndex = 0;
        score = 0;
        displayQuestion();
        Toast.makeText(this, "Juego reiniciado", Toast.LENGTH_SHORT).show();
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questions.size());
        startActivity(intent);
        finish();
    }
}
