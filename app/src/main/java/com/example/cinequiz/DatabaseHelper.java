package com.example.cinequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CineQuizDB";
    private static final int DATABASE_VERSION = 1;

    // Tabla de preguntas
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COL_ID = "id";
    private static final String COL_QUESTION_TEXT = "question_text";
    private static final String COL_QUESTION_IMAGE = "question_image";
    private static final String COL_OPTIONS = "options";
    private static final String COL_OPTION_IMAGES = "option_images";
    private static final String COL_CORRECT_ANSWER = "correct_answer";
    private static final String COL_CONTROL_TYPE = "control_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_QUESTION_TEXT + " TEXT, " +
                COL_QUESTION_IMAGE + " INTEGER, " +
                COL_OPTIONS + " TEXT, " +
                COL_OPTION_IMAGES + " TEXT, " +
                COL_CORRECT_ANSWER + " INTEGER, " +
                COL_CONTROL_TYPE + " TEXT)";
        db.execSQL(createTable);

        // Poblar con preguntas iniciales
        populateInitialQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    private void populateInitialQuestions(SQLiteDatabase db) {
        // Pregunta 1: RadioButton - Películas clásicas
        insertQuestion(db,
                "¿Qué actor interpretó al Joker en 'The Dark Knight' (2008)?",
                null,
                "Joaquin Phoenix|Heath Ledger|Jack Nicholson|Jared Leto",
                null,
                1,
                "RADIO_BUTTON");

        // Pregunta 2: Spinner - Series famosas
        insertQuestion(db,
                "¿Cuál es el nombre del actor del personaje Walter White en 'Breaking Bad'?",
                null,
                "Bryan Cranston|Aaron Paul|Heisenberg García|Tom Cruise",
                null,
                0,
                "SPINNER");

        // Pregunta 3: ListView - Directores
        insertQuestion(db,
                "¿Quién dirigió la trilogía original de 'Star Wars'?",
                null,
                "Steven Spielberg|George Lucas|J.J. Abrams|Rian Johnson",
                null,
                1,
                "LIST_VIEW");

        // Pregunta 4: RadioButton con imagen PNG - Death Star
        insertQuestion(db,
                "¿A qué saga pertenece esta imagen?",
                R.drawable.death_star,
                "Star Trek|Star Wars|Guardianes de la Galaxia|Dune",
                null,
                1,
                "RADIO_BUTTON");

        // Pregunta 5: Spinner - Premios Oscar
        insertQuestion(db,
                "¿Qué película ganó el Oscar a Mejor Película en 2020?",
                null,
                "1917|Joker|Parasite|Once Upon a Time in Hollywood",
                null,
                2,
                "SPINNER");

        // Pregunta 6: IMAGE_GRID - Adivina el póster
        insertQuestion(db,
                "¿Cual de estas imagenes pertenece a la saga 'Star Trek'?",
                null,
                null,
                String.valueOf(R.drawable.death_star) + "|" +
                        String.valueOf(R.drawable.spock) + "|" +
                        String.valueOf(R.drawable.jhon) + "|" +
                        String.valueOf(R.drawable.casadepapelgood),
                1,
                "IMAGE_GRID");

        // Pregunta 7: IMAGE_GRID - Personajes de Pixar
        insertQuestion(db,
                "¿Cuál de estos personajes es 'Woody' de Toy Story?",
                null,
                null,
                String.valueOf(R.drawable.buzzgood) + "|" +
                        String.valueOf(R.drawable.woody) + "|" +
                        String.valueOf(R.drawable.jessietoystory) + "|" +
                        String.valueOf(R.drawable.perro),
                1,
                "IMAGE_GRID");

        // Pregunta 8: Spinner - Naves espaciales
        insertQuestion(db,
                "El Halcón Milenario es una nave de la saga...",
                null,
                "Star Trek|Battlestar Galactica|Star Wars|Stargate",
                null,
                2,
                "SPINNER");

        // Pregunta 9: ListView - Objetos icónicos
        insertQuestion(db,
                "¿A qué universo mágico pertenece la 'Varita de Saúco'?",
                null,
                "El Señor de los Anillos|Harry Potter|Las Crónicas de Narnia|Juego de Tronos",
                null,
                1,
                "LIST_VIEW");

        // Pregunta 10: RadioButton - Actrices famosas
        insertQuestion(db,
                "¿Qué actriz interpreta a la Viuda Negra en el Universo Cinematográfico de Marvel?",
                null,
                "Scarlett Johansson|Gal Gadot|Zoe Saldaña|Jennifer Lawrence",
                null,
                0,
                "RADIO_BUTTON");
        // Pregunta 11: RadioButton - Actrices famosas
        insertQuestion(db,
                "¿Qué actriz interpreta a la Viuda Negra en el Universo Cinematográfico de Marvel?",
                null,
                "Scarlett Johansson|Gal Gadot|Zoe Saldaña|Jennifer Lawrence",
                null,
                0,
                "RADIO_BUTTON");
    }

    private void insertQuestion(SQLiteDatabase db, String questionText, Integer questionImage,
                                String options, String optionImages, int correctAnswer, String controlType) {
        ContentValues values = new ContentValues();
        values.put(COL_QUESTION_TEXT, questionText);
        if (questionImage != null) {
            values.put(COL_QUESTION_IMAGE, questionImage);
        }
        values.put(COL_OPTIONS, options);
        values.put(COL_OPTION_IMAGES, optionImages);
        values.put(COL_CORRECT_ANSWER, correctAnswer);
        values.put(COL_CONTROL_TYPE, controlType);
        db.insert(TABLE_QUESTIONS, null, values);
    }

    // Método público para agregar preguntas desde fuera
    public void addQuestion(String questionText, Integer questionImage,
                           String options, String optionImages, int correctAnswer, String controlType) {
        SQLiteDatabase db = this.getWritableDatabase();
        insertQuestion(db, questionText, questionImage, options, optionImages, correctAnswer, controlType);
        db.close();
    }

    // Obtener todas las preguntas
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow(COL_QUESTION_TEXT));

                Integer questionImage = null;
                int imageColIndex = cursor.getColumnIndexOrThrow(COL_QUESTION_IMAGE);
                if (!cursor.isNull(imageColIndex)) {
                    questionImage = cursor.getInt(imageColIndex);
                }

                String optionsStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTIONS));
                List<String> options = null;
                if (optionsStr != null && !optionsStr.isEmpty()) {
                    options = Arrays.asList(optionsStr.split("\\|"));
                }

                String optionImagesStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTION_IMAGES));
                List<Integer> optionImages = null;
                if (optionImagesStr != null && !optionImagesStr.isEmpty()) {
                    String[] imageIds = optionImagesStr.split("\\|");
                    optionImages = new ArrayList<>();
                    for (String id : imageIds) {
                        optionImages.add(Integer.parseInt(id));
                    }
                }

                int correctAnswer = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CORRECT_ANSWER));
                String controlTypeStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTROL_TYPE));
                Question.ControlType controlType = Question.ControlType.valueOf(controlTypeStr);

                Question question = new Question(questionText, questionImage, options, optionImages,
                        correctAnswer, controlType);
                questions.add(question);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questions;
    }

    // Eliminar todas las preguntas
    public void deleteAllQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, null, null);
        db.close();
    }

    // Contar preguntas
    public int getQuestionCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}

