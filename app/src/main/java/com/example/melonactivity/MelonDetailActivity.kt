package com.example.melonactivity

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

class MelonDetailActivity : AppCompatActivity() {

    lateinit var playPauseButton: ImageView //음악 실행,정지 버튼
    lateinit var mediaPlayer: MediaPlayer //음원재생에 사용할 라이브러리
    lateinit var melonItemList: ArrayList<MelonItem> //서버에서 받아올 데이터

    var position = 0 //사용자가 클릭한 위치값
        set(value) {
            if (value <= 0) { //첫 번째 음악에서 이전버튼 클릭하면 position 변경하지 않음
                field = 0
            } else if (value >= melonItemList.size - 1) { // 마지막 음악에서 다음버튼 클릭하면 position 변경하지 않음
                field = melonItemList.size - 1
            } else {
                field = value
            }
        }

    var is_playing = true //음악 실행여부 판단
        @SuppressLint("UseCompatLoadingForDrawables")
        set(value) {
            if (value) { //음악실행
                playPauseButton.setImageDrawable(
                    this.resources.getDrawable(R.drawable.pause, this.theme)
                )
            } else { //음악 정지
                playPauseButton.setImageDrawable(
                    this.resources.getDrawable(R.drawable.play, this.theme)
                )
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melon_detail)

        melonItemList = intent.getSerializableExtra("melonItemList") as ArrayList<MelonItem> //Serializable로 값 받아옴
        position = intent.getIntExtra("position", 0)
        
        //처음 Activity가 켜지면 음악과 thumbnail 실행
        playMelonItem(melonItemList[position])
        changeThumbnail(melonItemList[position])

        //음악 실행, 정지버튼
        playPauseButton = findViewById(R.id.play)
        playPauseButton.setOnClickListener {
            if (is_playing) {
                is_playing = false
                mediaPlayer.stop()
            } else {
                is_playing = true
                playMelonItem(melonItemList[position])
            }
        }
        
        //이전음악 재생
        findViewById<ImageView>(R.id.back).setOnClickListener {
            mediaPlayer.stop() //다른음악 켜기 위해 실행되던 음악 정지
            position--
            playMelonItem(melonItemList[position])
            changeThumbnail(melonItemList[position])
        }

        findViewById<ImageView>(R.id.next).setOnClickListener {
            mediaPlayer.stop()
            position++
            playMelonItem(melonItemList[position])
            changeThumbnail(melonItemList[position])
        }


    }

    //음악재생 메소드
    fun playMelonItem(melonItem: MelonItem) {
        mediaPlayer = MediaPlayer.create(
            this,
            Uri.parse(melonItem.song)
        )
        mediaPlayer.start()
    }

    //thumbnail 변경 메소드
    fun changeThumbnail(melonItem: MelonItem) {
        findViewById<ImageView>(R.id.thumbnail).apply {
            val glide = Glide.with(this@MelonDetailActivity)
            glide.load(melonItem.thumbnail).into(this)
        }
    }

}