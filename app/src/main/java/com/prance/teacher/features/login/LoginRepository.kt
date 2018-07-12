package com.prance.teacher.features.login

import com.prance.lib.base.exception.Failure
import com.prance.lib.base.functional.Either

interface LoginRepository {

    fun getQrCode(): Either<Failure, QrCode>

    fun checkLoginStatus(): Either<Failure, Any>
}