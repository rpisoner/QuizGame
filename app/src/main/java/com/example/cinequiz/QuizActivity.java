package com.example.cinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.VideoView;
import android.widget.MediaController;

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

    private DatabaseHelper databaseHelper;

    // MediaPlayer para reproducir audio
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        databaseHelper = new DatabaseHelper(this);

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
        btnRestart.setOnClickListener(v -> {
            SoundManager.playButtonSound(this);
            restartGame();
        });
    }

    private void initializeQuestions() {
        // Cargar preguntas desde la base de datos
        questions = databaseHelper.getAllQuestions();

        // Si no hay preguntas en la BD (por algún error), mostrar mensaje
        if (questions.isEmpty()) {
            Toast.makeText(this, "Error al cargar preguntas de la base de datos", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishQuiz();
            return;
        }

        // Detener cualquier reproducción anterior
        stopMediaPlayback();

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
            case AUDIO_QUESTION:
                createAudioQuestion(question);
                break;
            case VIDEO_QUESTION:
                createVideoQuestion(question);
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

    private void createAudioQuestion(Question question) {
        // Crear botón de reproducción de audio
        Button btnPlayAudio = new Button(this);
        btnPlayAudio.setText("▶ Reproducir Audio");
        btnPlayAudio.setTextSize(18);
        btnPlayAudio.setTextColor(0xFFFFFFFF);
        btnPlayAudio.setBackgroundColor(0xFFDC143C); // Rojo cinema
        btnPlayAudio.setPadding(20, 40, 20, 40);

        LinearLayout.LayoutParams audioParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        audioParams.setMargins(16, 16, 16, 32);
        btnPlayAudio.setLayoutParams(audioParams);

        // Inicializar MediaPlayer
        if (question.hasQuestionAudio()) {
            mediaPlayer = MediaPlayer.create(this, question.getQuestionAudioId());

            btnPlayAudio.setOnClickListener(v -> {
                SoundManager.playButtonSound(this);
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnPlayAudio.setText("▶ Reproducir Audio");
                    } else {
                        mediaPlayer.start();
                        btnPlayAudio.setText("⏸ Pausar Audio");

                        // Cuando termine el audio, resetear el botón
                        mediaPlayer.setOnCompletionListener(mp -> {
                            btnPlayAudio.setText("▶ Reproducir Audio");
                        });
                    }
                }
            });
        }

        controlContainer.addView(btnPlayAudio);

        // Agregar las opciones de respuesta con RadioButtons
        createRadioButtons(question);
    }

    private void createVideoQuestion(Question question) {
        if (!question.hasQuestionVideo()) return;

        // Crear VideoView
        VideoView videoView = new VideoView(this);

        LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600 // Altura fija para el video
        );
        videoParams.setMargins(16, 16, 16, 16);
        videoView.setLayoutParams(videoParams);
        videoView.setBackgroundColor(0xFF000000); // Fondo negro

        // Configurar el video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + question.getQuestionVideoId());
        videoView.setVideoURI(videoUri);

        // Agregar controles de media
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Reproducir automáticamente
        videoView.start();

        // Hacer el video en loop
        videoView.setOnCompletionListener(mp -> videoView.start());

        controlContainer.addView(videoView);

        // Agregar las opciones de respuesta con RadioButtons
        createRadioButtons(question);
    }

    private void stopMediaPlayback() {
        // Detener y liberar MediaPlayer si existe
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Detener cualquier VideoView en el contenedor
        for (int i = 0; i < controlContainer.getChildCount(); i++) {
            View child = controlContainer.getChildAt(i);
            if (child instanceof VideoView) {
                VideoView videoView = (VideoView) child;
                videoView.stopPlayback();
            }
        }
    }

    private void checkAnswer() {
        if (selectedAnswer == -1) {
            Toast.makeText(this, "Por favor, selecciona una respuesta", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedAnswer == question.getCorrectAnswerIndex();

        if (isCorrect) {
            // Reproducir sonido de respuesta correcta
            SoundManager.playCorrectSound(this);
            score += POINTS_CORRECT;
            showFeedbackDialog("¡Correcto!", "¡Excelente! Has ganado " + POINTS_CORRECT + " puntos.", true);
        } else {
            // Reproducir sonido de respuesta incorrecta
            SoundManager.playIncorrectSound(this);
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
        stopMediaPlayback();
        currentQuestionIndex = 0;
        score = 0;
        displayQuestion();
        Toast.makeText(this, "Juego reiniciado", Toast.LENGTH_SHORT).show();
    }

    private void finishQuiz() {
        stopMediaPlayback();
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questions.size());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMediaPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMediaPlayback();
    }
}
