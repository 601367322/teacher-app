package com.prance.teacher.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.prance.teacher.R

object SoundUtils {

    val sounds = mutableMapOf<String, Int>()
    var mSoundPoll: SoundPool

    init {
        mSoundPoll = SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(
                        AudioAttributes.Builder()
                                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                .build()
                )
                .build()
        mSoundPoll.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {

            override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
                LogUtils.i("onLoadComplete  $sampleId")
            }
        })
    }

    fun load() {
        if (sounds.isEmpty()) {
            sounds["four_count_time"] = mSoundPoll.load(Utils.getApp(), R.raw.four_count_time, Int.MAX_VALUE)
            sounds["rank_background"] = mSoundPoll.load(Utils.getApp(), R.raw.rank_background, Int.MAX_VALUE)
            sounds["red_package_get"] = mSoundPoll.load(Utils.getApp(), R.raw.red_package_get, Int.MAX_VALUE)
            sounds["five_count_time"] = mSoundPoll.load(Utils.getApp(), R.raw.five_count_time, Int.MAX_VALUE)
            sounds["open_box"] = mSoundPoll.load(Utils.getApp(), R.raw.open_box, Int.MAX_VALUE)
        }
    }

    fun getRatio(): Float {
        val am = Utils.getApp().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val current = am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        return current / max * 0.5f
    }

    fun play(key: String) {
        sounds[key]?.run {
            mSoundPoll.play(this,
                    getRatio(),
                    getRatio(),
                    Int.MAX_VALUE,
                    0,
                    1F)
        }
    }

    fun stop(key: String) {
        sounds[key]?.run {
            mSoundPoll.stop(this)
        }
    }

}