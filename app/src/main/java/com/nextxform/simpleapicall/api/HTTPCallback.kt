package com.nextxform.simpleapicall.api

interface HTTPCallback {
    fun processFinished(output: String)
    fun processFailed(responseCode: Int, output: String)
}