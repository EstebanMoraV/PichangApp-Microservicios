package cl.duoc.pichangapp.core.network

import cl.duoc.pichangapp.core.datastore.TokenDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // We use runBlocking safely here since OkHttp interceptors run on background threads
        val token = runBlocking { tokenDataStore.tokenFlow.firstOrNull() }

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
