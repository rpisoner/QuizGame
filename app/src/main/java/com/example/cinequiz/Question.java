package com.example.cinequiz;

import java.util.List;

public class Question {
    private String questionText;
    private Integer questionImageId; // null si no hay imagen
    private List<String> options;
    private List<Integer> optionImages; // null si no hay imágenes
    private int correctAnswerIndex;
    private ControlType controlType;

    // Constructor completo
    public Question(String questionText, Integer questionImageId,
                    List<String> options, List<Integer> optionImages,
                    int correctAnswerIndex, ControlType controlType) {
        this.questionText = questionText;
        this.questionImageId = questionImageId;
        this.options = options;
        this.optionImages = optionImages;
        this.correctAnswerIndex = correctAnswerIndex;
        this.controlType = controlType;
    }

    // Constructor simplificado para preguntas solo de texto
    public Question(String questionText, List<String> options,
                    int correctAnswerIndex, ControlType controlType) {
        this(questionText, null, options, null, correctAnswerIndex, controlType);
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public Integer getQuestionImageId() { return questionImageId; }
    public List<String> getOptions() { return options; }
    public List<Integer> getOptionImages() { return optionImages; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public ControlType getControlType() { return controlType; }

    public boolean hasQuestionImage() { return questionImageId != null; }
    public boolean hasOptionImages() { return optionImages != null; }

    // Enum para tipos de control
    public enum ControlType {
        RADIO_BUTTON,
        SPINNER,
        LIST_VIEW,
        IMAGE_GRID
    }
}