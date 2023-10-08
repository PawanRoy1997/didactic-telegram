package com.nextxform.simpleapicall.api

import arrow.core.Either

interface HTTPCallback {
    fun callResponse(response: Either<ResponseModel, ResponseModel>)
}

data class ResponseModel(val responseCode: Int, val responseBody: String)