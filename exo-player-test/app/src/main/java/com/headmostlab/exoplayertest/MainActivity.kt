package com.headmostlab.exoplayertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes

class MainActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player = SimpleExoPlayer.Builder(this).build()

        val uri = "https://firebasestorage.googleapis.com" +
                "/v0/b/word-memorizing.appspot.com/o/wb1%2Fsnd%2F60.mp3?alt=media&" +
                "token=b0fe00ac-2e39-4145-9e5d-7c9793b5da0c"

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMimeType(MimeTypes.AUDIO_MPEG_L1)
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()

        val playerView: StyledPlayerView = findViewById(R.id.playerView)
        playerView.player = player

    }
}