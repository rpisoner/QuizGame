package com.example.cinequiz;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private static MediaPlayer mediaPlayer;

    /**
     * Reproduce el sonido de clic de botón
     * @param context Contexto de la aplicación
     */
    public static void playButtonSound(Context context) {
        try {
            // Liberar el MediaPlayer anterior si existe
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            // Crear nuevo MediaPlayer con el sonido tick.mp3
            mediaPlayer = MediaPlayer.create(context, R.raw.tick);

            if (mediaPlayer != null) {
                mediaPlayer.setVolume(0.5f, 0.5f); // Volumen al 50%
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay error, no hacemos nada para no interrumpir la app
        }
    }

    /**
     * Reproduce el sonido de respuesta correcta
     * @param context Contexto de la aplicación
     */
    public static void playCorrectSound(Context context) {
        playSound(context, R.raw.correct);
    }

    /**
     * Reproduce el sonido de respuesta incorrecta
     * @param context Contexto de la aplicación
     */
    public static void playIncorrectSound(Context context) {
        playSound(context, R.raw.incorrect);
    }

    /**
     * Reproduce el sonido de pantalla final
     * @param context Contexto de la aplicación
     */
    public static void playFinalScreenSound(Context context) {
        playSound(context, R.raw.final_screen);
    }

    /**
     * Método genérico para reproducir un sonido
     * @param context Contexto de la aplicación
     * @param soundResourceId ID del recurso de sonido
     */
    private static void playSound(Context context, int soundResourceId) {
        try {
            // Liberar el MediaPlayer anterior si existe
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            // Crear nuevo MediaPlayer con el sonido especificado
            mediaPlayer = MediaPlayer.create(context, soundResourceId);

            if (mediaPlayer != null) {
                mediaPlayer.setVolume(0.7f, 0.7f); // Volumen al 70%
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay error, no hacemos nada para no interrumpir la app
        }
    }

    /**
     * Libera los recursos del MediaPlayer
     */
    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
