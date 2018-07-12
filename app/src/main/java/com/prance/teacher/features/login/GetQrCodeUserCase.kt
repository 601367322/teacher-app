package com.prance.teacher.features.login

import com.prance.lib.base.exception.Failure
import com.prance.lib.base.functional.Either
import com.prance.lib.base.interactor.UseCase

class GetQrCodeUserCase : UseCase<QrCode, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, QrCode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}