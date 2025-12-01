package com.example.cinequiz;

import java.util.List;

public class Question {
    private String questionText;
    private Integer questionImageId; // null si no hay imagen
    private Integer questionAudioId; // null si no hay audio
    private Integer questionVideoId; // null si no hay video
    private List<String> options;
    private List<Integer> optionImages; // null si no hay imágenes
    private int correctAnswerIndex;
    private ControlType controlType;

    // Constructor completo con audio y video
    public Question(String questionText, Integer questionImageId,
                    Integer questionAudioId, Integer questionVideoId,
                    List<String> options, List<Integer> optionImages,
                    int correctAnswerIndex, ControlType controlType) {
        this.questionText = questionText;
        this.questionImageId = questionImageId;
        this.questionAudioId = questionAudioId;
        this.questionVideoId = questionVideoId;
        this.options = options;
        this.optionImages = optionImages;
        this.correctAnswerIndex = correctAnswerIndex;
        this.controlType = controlType;
    }

    // Constructor completo (legacy - para compatibilidad)
    public Question(String questionText, Integer questionImageId,
                    List<String> options, List<Integer> optionImages,
                    int correctAnswerIndex, ControlType controlType) {
        this(questionText, questionImageId, null, null, options, optionImages, correctAnswerIndex, controlType);
    }

    // Constructor simplificado para preguntas solo de texto
    public Question(String questionText, List<String> options,
                    int correctAnswerIndex, ControlType controlType) {
        this(questionText, null, null, null, options, null, correctAnswerIndex, controlType);
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public Integer getQuestionImageId() { return questionImageId; }
    public Integer getQuestionAudioId() { return questionAudioId; }
    public Integer getQuestionVideoId() { return questionVideoId; }
    public List<String> getOptions() { return options; }
    public List<Integer> getOptionImages() { return optionImages; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public ControlType getControlType() { return controlType; }

    public boolean hasQuestionImage() { return questionImageId != null; }
    public boolean hasQuestionAudio() { return questionAudioId != null; }
    public boolean hasQuestionVideo() { return questionVideoId != null; }
    public boolean hasOptionImages() { return optionImages != null; }

    // Enum para tipos de control
    public enum ControlType {
        RADIO_BUTTON,
        SPINNER,
        LIST_VIEW,
        IMAGE_GRID,
        AUDIO_QUESTION,
        VIDEO_QUESTION
    }
}