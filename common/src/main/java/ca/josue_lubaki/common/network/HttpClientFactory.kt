package ca.josue_lubaki.common.network

import okhttp3.OkHttpClient

class HttpClientFactory {

    private val httpClient by lazy {
        OkHttpClient()
    }

    fun create(): OkHttpClient.Builder {
        return httpClient.newBuilder()
    }
}