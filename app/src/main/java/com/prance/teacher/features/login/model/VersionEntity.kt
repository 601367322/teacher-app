package com.prance.teacher.features.login.model

import java.io.Serializable

data class VersionEntity(val path: String, val appVersion: String, val codeVersion: Int) : Serializable