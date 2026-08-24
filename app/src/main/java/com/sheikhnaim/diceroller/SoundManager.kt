package com.sheikhnaim.diceroller

import android.content.Context
import android.media.MediaPlayer

class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playDiceSound() {
        try {
            // Release previous player if exists
            mediaPlayer?.release()

            // Create new player
            mediaPlayer = MediaPlayer.create(context, R.raw.dice_sound)

            // Start playing
            mediaPlayer?.start()

            // Release resources when sound completes
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}