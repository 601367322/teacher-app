package com.prance.teacher.features.common

import android.content.Context
import java.io.Serializable

class Retry(var onRetry: ((context: Context) -> Unit)? = null) : Serializable