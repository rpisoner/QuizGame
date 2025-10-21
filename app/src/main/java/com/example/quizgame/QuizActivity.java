package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
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

        // Pregunta 1: RadioButton - Películas clásicas
        questions.add(new Question(
                "¿Qué actor interpretó al Joker en 'The Dark Knight' (2008)?",
                Arrays.asList("Joaquin Phoenix", "Heath Ledger", "Jack Nicholson", "Jared Leto"),
                1,
                Question.ControlType.RADIO_BUTTON
        ));

        // Pregunta 2: Spinner - Series famosas
        questions.add(new Question(
                "¿Cuál es el nombre del actor del personaje Walter White en 'Breaking Bad'?",
                Arrays.asList("Bryan Cranston", "Aaron Paul", "Heisenberg García", "Tom Cruise"),
                0,
                Question.ControlType.SPINNER
        ));

        // Pregunta 3: ListView - Directores
        questions.add(new Question(
                "¿Quién dirigió la trilogía original de 'Star Wars'?",
                Arrays.asList("Steven Spielberg", "George Lucas", "J.J. Abrams", "Rian Johnson"),
                1,
                Question.ControlType.LIST_VIEW
        ));

        // Pregunta 4: RadioButton con imagen PNG - Death Star
        questions.add(new Question(
                "¿A qué saga pertenece esta imagen?",
                R.drawable.death_star,
                Arrays.asList("Star Trek", "Star Wars", "Guardianes de la Galaxia", "Dune"),
                null,
                1,
                Question.ControlType.RADIO_BUTTON
        ));

        // Pregunta 5: Spinner - Premios Oscar
        questions.add(new Question(
                "¿Qué película ganó el Oscar a Mejor Película en 2020?",
                Arrays.asList("1917", "Joker", "Parasite", "Once Upon a Time in Hollywood"),
                2,
                Question.ControlType.SPINNER
        ));

        // Pregunta 6: IMAGE_GRID - Adivina el póster
        questions.add(new Question(
                "¿Cual de estas imagenes pertenece a la saga 'Star Trek'?",
                null, // Sin imagen principal para la pregunta
                null, // Sin opciones de texto
                Arrays.asList(R.drawable.death_star, R.drawable.spock, R.drawable.jhon, R.drawable.casadepapelgood),
                1, // Índice de la respuesta correcta (p. ej., el primer póster)
                Question.ControlType.IMAGE_GRID
        ));

        // Pregunta 7: IMAGE_GRID - Personajes de Pixar
        questions.add(new Question(
                "¿Cuál de estos personajes es 'Woody' de Toy Story?",
                null,
                null,
                Arrays.asList(R.drawable.buzzgood, R.drawable.woody, R.drawable.jessietoystory, R.drawable.perro),
                1,
                Question.ControlType.IMAGE_GRID
        ));

        // Pregunta 8: Spinner - Naves espaciales
        questions.add(new Question(
                "El Halcón Milenario es una nave de la saga...",
                Arrays.asList("Star Trek", "Battlestar Galactica", "Star Wars", "Stargate"),
                2,
                Question.ControlType.SPINNER
        ));

        // Pregunta 9: ListView - Objetos icónicos
        questions.add(new Question(
                "¿A qué universo mágico pertenece la 'Varita de Saúco'?",
                Arrays.asList("El Señor de los Anillos", "Harry Potter", "Las Crónicas de Narnia", "Juego de Tronos"),
                1,
                Question.ControlType.LIST_VIEW
        ));

        // Pregunta 10: RadioButton - Actrices famosas
        questions.add(new Question(
                "¿Qué actriz interpreta a la Viuda Negra en el Universo Cinematográfico de Marvel?",
                Arrays.asList("Scarlett Johansson", "Gal Gadot", "Zoe Saldaña", "Jennifer Lawrence"),
                0,
                Question.ControlType.RADIO_BUTTON
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
            case IMAGE_GRID:
                createImageGrid(question);
                break;
        }
    }

    private void createRadioButtons(Question question) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioGroup.setPadding(8, 8, 8, 8);

        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(i);
            radioButton.setTextSize(17);
            radioButton.setTextColor(0xFFFFFFFF); // Blanco
            radioButton.setPadding(20, 20, 20, 20);

            // Agregar margen entre opciones
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            radioButton.setLayoutParams(params);
            radioButton.setBackgroundColor(0xFF2C2C2E); // Fondo gris oscuro

            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedAnswer = checkedId;
            // Resaltar la opción seleccionada
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                if (rb.getId() == checkedId) {
                    rb.setBackgroundColor(0xFFDC143C); // Rojo cinema cuando está seleccionado
                } else {
                    rb.setBackgroundColor(0xFF2C2C2E); // Gris oscuro
                }
            }
        });

        controlContainer.addView(radioGroup);
    }

    private void createSpinner(Question question) {
        TextView label = new TextView(this);
        label.setText("🎥 Selecciona una opción:");
        label.setTextSize(16);
        label.setTextColor(0xFFFFD700); // Dorado
        label.setPadding(16, 16, 16, 16);
        controlContainer.addView(label);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, question.getOptions()) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setBackgroundColor(0xFF3A3A3C); // Fondo gris claro para el dropdown
                textView.setTextColor(0xFFFFFFFF); // Texto blanco
                textView.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPadding(20, 16, 20, 16);
        spinner.setBackgroundColor(0xFF2C2C2E); // Fondo gris oscuro

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 8, 16, 8);
        spinner.setLayoutParams(params);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswer = position;
                if (view != null) {
                    ((TextView) view).setTextColor(0xFFFFD700); // Dorado
                }
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
        label.setText("🎬 Toca una opción:");
        label.setTextSize(16);
        label.setTextColor(0xFFFFD700); // Dorado
        label.setPadding(16, 16, 16, 16);
        controlContainer.addView(label);

        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                question.getOptions()
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(0xFFFFFFFF); // Set text color to white
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setBackgroundColor(0xFF2C2C2E); // Fondo gris oscuro
        listView.setDivider(null);
        listView.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                450
        );
        params.setMargins(16, 8, 16, 8);
        listView.setLayoutParams(params);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedAnswer = position;
            listView.setItemChecked(position, true);
        });

        controlContainer.addView(listView);
    }

    private void createImageGrid(Question question) {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2); // 2 columnas
        gridLayout.setRowCount(2);    // 2 filas para 4 imágenes
        gridLayout.setUseDefaultMargins(true);

        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        gridParams.setMargins(16, 8, 16, 8);
        gridLayout.setLayoutParams(gridParams);

        List<Integer> imageIds = question.getOptionImages();
        if (imageIds == null) return;

        for (int i = 0; i < imageIds.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds.get(i));
            imageView.setId(i);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setBackgroundColor(0xFF2C2C2E); // Gris oscuro

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            imageView.setLayoutParams(params);

            final int index = i;
            imageView.setOnClickListener(v -> {
                selectedAnswer = index;
                for (int j = 0; j < gridLayout.getChildCount(); j++) {
                    View child = gridLayout.getChildAt(j);
                    if (j == selectedAnswer) {
                        child.setBackgroundColor(0xFFDC143C); // Rojo cinema
                    } else {
                        child.setBackgroundColor(0xFF2C2C2E); // Gris oscuro
                    }
                }
            });

            gridLayout.addView(imageView);
        }
        controlContainer.addView(gridLayout);
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
            String correctAnswerText;
            if (question.getControlType() == Question.ControlType.IMAGE_GRID) {
                correctAnswerText = "la opción " + (question.getCorrectAnswerIndex() + 1);
            } else {
                correctAnswerText = question.getOptions().get(question.getCorrectAnswerIndex());
            }
            score += POINTS_INCORRECT;
            showFeedbackDialog("Incorrecto", "La respuesta correcta era: " +
                    correctAnswerText +
                    "\nSe han restado " + Math.abs(POINTS_INCORRECT) + " puntos.", false);
        }

        updateScore();
    }

    private void showFeedbackDialog(String title, String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Continuar", (dialog, which) -> {
            currentQuestionIndex++;
            displayQuestion();
        });

        if (!isCorrect) {
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
