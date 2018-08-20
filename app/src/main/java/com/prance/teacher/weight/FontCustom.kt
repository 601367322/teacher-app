package com.prance.teacher.weight

import android.content.Context
import android.graphics.Typeface

object FontCustom {

    private val fontUrl_FZY1JW = "FZY1JW.ttf"
    private val fontUrl_COMICSANSMSGRAS = "COMICSANSMSGRAS.otf"
    private var FZY1JW: Typeface? = null
    private var COMICSANSMSGRAS: Typeface? = null

    fun getFZY1JWFont(context: Context): Typeface {
        if (FZY1JW == null) {
            //给它设置你传入的自定义字体文件，再返回回来
            FZY1JW = Typeface.createFromAsset(context.assets, fontUrl_FZY1JW)
        }
        return FZY1JW!!
    }

    fun getCOMICSANSMSGRASFont(context: Context): Typeface {
        if (COMICSANSMSGRAS == null) {
            //给它设置你传入的自定义字体文件，再返回回来
            COMICSANSMSGRAS = Typeface.createFromAsset(context.assets, fontUrl_COMICSANSMSGRAS)
        }
        return COMICSANSMSGRAS!!
    }
}
