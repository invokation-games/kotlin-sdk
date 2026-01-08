package ivk.skill.sdk

import ivk.skill.api.apis.SkillApi
import ivk.skill.api.infrastructure.ClientException
import ivk.skill.api.infrastructure.ServerException
import ivk.skill.api.models.MatchResultRequest
import ivk.skill.api.models.MatchResultResponse
import ivk.skill.api.models.PreMatchRequest
import ivk.skill.api.models.PreMatchResponse
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory

/**
 * SDK wrapper for the IVK Skill API with built-in retry mechanism and API key authentication.
 *
 * This SDK provides both synchronous and asynchronous APIs for Kotlin and Java consumers.
 *
 * ## Example usage in Kotlin:
 * ```kotlin
 * val sdk = SkillSdk.builder()
 *     .apiKey("your-api-key")
 *     .modelId("your-model-id")
 *     .build()
 *
 * // Coroutine-based API
 * val result = sdk.postMatchResult(matchResultRequest)
 *
 * // Blocking API
 * val result = sdk.postMatchResultBlocking(matchResultRequest)
 * ```
 *
 * ## Example usage in Java:
 * ```java
 * SkillSdk sdk = SkillSdk.builder()
 *     .apiKey("your-api-key")
 *     .modelId("your-model-id")
 *     .build();
 *
 * // Blocking API
 * MatchResultResponse result = sdk.postMatchResultBlocking(matchResultRequest);
 *
 * // Async API with CompletableFuture
 * CompletableFuture<MatchResultResponse> future = sdk.postMatchResultAsync(matchResultRequest);
 * ```
 *
 * @property apiKey The API key for authentication
 * @property baseUrl The base URL for the API
 * @property environment The environment to target
 * @property retryConfig Configuration for retry behavior
 */
class SkillSdk
private constructor(
        private val apiKey: String,
        private val baseUrl: String,
        private val environment: String,
        private val retryConfig: RetryConfig,
        httpClient: OkHttpClient?
) : AutoCloseable {

    private val logger = LoggerFactory.getLogger(SkillSdk::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val skillApi: SkillApi
    private val httpClientInternal: OkHttpClient

    init {
        httpClientInternal =
                (httpClient
                                ?: OkHttpClient.Builder()
                                        .connectTimeout(30, TimeUnit.SECONDS)
                                        .readTimeout(30, TimeUnit.SECONDS)
                                        .writeTimeout(30, TimeUnit.SECONDS)
                                        .build())
                        .newBuilder()
                        .addInterceptor(ApiKeyInterceptor(apiKey))
                        .build()

        skillApi = SkillApi(basePath = baseUrl, client = httpClientInternal)
    }

    // ===== Coroutine-based API (Kotlin-friendly) =====

    /**
     * Submit match results to update player skill ratings.
     *
     * @param modelId The model ID to use
     * @param matchResultRequest The match result data
     * @return MatchResultResponse containing updated skill ratings
     * @throws IOException if network error occurs after all retries
     * @throws ClientException if client error occurs (4xx status codes)
     * @throws ServerException if server error occurs after all retries (5xx status codes)
     */
    suspend fun postMatchResult(
            modelId: String,
            matchResultRequest: MatchResultRequest
    ): MatchResultResponse {
        return executeWithRetry {
            skillApi.postMatchResult(
                    modelId = modelId,
                    environment = environment,
                    matchResultRequest = matchResultRequest
            )
        }
    }

    /**
     * Calculate expected match outcomes before the match starts.
     *
     * @param modelId The model ID to use
     * @param preMatchRequest The pre-match data with player information
     * @return PreMatchResponse containing expected outcomes
     * @throws IOException if network error occurs after all retries
     * @throws ClientException if client error occurs (4xx status codes)
     * @throws ServerException if server error occurs after all retries (5xx status codes)
     */
    suspend fun postPreMatch(modelId: String, preMatchRequest: PreMatchRequest): PreMatchResponse {
        return executeWithRetry {
            skillApi.postPreMatch(
                    modelId = modelId,
                    environment = environment,
                    preMatchRequest = preMatchRequest
            )
        }
    }

    // ===== Blocking API (Java-friendly) =====

    /**
     * Submit match results to update player skill ratings (blocking).
     *
     * This method blocks the current thread until the operation completes. Suitable for Java
     * consumers or when synchronous behavior is desired.
     */
    @JvmName("postMatchResultBlocking")
    fun postMatchResultBlocking(
            modelId: String,
            matchResultRequest: MatchResultRequest
    ): MatchResultResponse {
        return runBlocking { postMatchResult(modelId, matchResultRequest) }
    }

    /**
     * Calculate expected match outcomes before the match starts (blocking).
     *
     * This method blocks the current thread until the operation completes. Suitable for Java
     * consumers or when synchronous behavior is desired.
     */
    @JvmName("postPreMatchBlocking")
    fun postPreMatchBlocking(modelId: String, preMatchRequest: PreMatchRequest): PreMatchResponse {
        return runBlocking { postPreMatch(modelId, preMatchRequest) }
    }

    // ===== CompletableFuture API (Java-friendly async) =====

    /**
     * Submit match results to update player skill ratings (async).
     *
     * @return CompletableFuture that will complete with the result
     */
    @JvmName("postMatchResultAsync")
    fun postMatchResultAsync(
            modelId: String,
            matchResultRequest: MatchResultRequest
    ): CompletableFuture<MatchResultResponse> {
        val future = CompletableFuture<MatchResultResponse>()
        coroutineScope.launch {
            try {
                val result = postMatchResult(modelId, matchResultRequest)
                future.complete(result)
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }
        return future
    }

    /**
     * Calculate expected match outcomes before the match starts (async).
     *
     * @return CompletableFuture that will complete with the result
     */
    @JvmName("postPreMatchAsync")
    fun postPreMatchAsync(
            modelId: String,
            preMatchRequest: PreMatchRequest
    ): CompletableFuture<PreMatchResponse> {
        val future = CompletableFuture<PreMatchResponse>()
        coroutineScope.launch {
            try {
                val result = postPreMatch(modelId, preMatchRequest)
                future.complete(result)
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }
        return future
    }

    /** Execute an API call with retry logic */
    private suspend fun <T> executeWithRetry(block: suspend () -> T): T {
        var lastException: Exception? = null

        for (attempt in 0 until retryConfig.maxRetries) {
            try {
                logger.debug(
                        "Attempting API call (attempt ${attempt + 1}/${retryConfig.maxRetries}) "
                )
                return block()
            } catch (e: ClientException) {
                // Don't retry client errors (4xx)
                logger.warn("Client error occurred: ${e.message}")
                throw e
            } catch (e: Exception) {
                lastException = e
                logger.warn(
                        "API call failed (attempt ${attempt + 1}/${retryConfig.maxRetries}): ${e.message}"
                )

                // Check if we should retry
                val shouldRetry =
                        when (e) {
                            is IOException -> true
                            is ServerException -> e.statusCode >= 500
                            else -> false
                        }

                if (!shouldRetry || attempt == retryConfig.maxRetries - 1) {
                    throw e
                }

                // Calculate delay with exponential backoff
                val delayMs = calculateBackoffDelay(attempt)
                logger.debug("Retrying after ${delayMs}ms")
                delay(delayMs)
            }
        }

        throw lastException ?: IOException("Failed after ${retryConfig.maxRetries} retries")
    }

    /** Calculate exponential backoff delay */
    private fun calculateBackoffDelay(attempt: Int): Long {
        val exponentialDelay = retryConfig.initialDelayMs * (2.0.pow(attempt.toDouble())).toLong()
        return min(exponentialDelay, retryConfig.maxDelayMs)
    }

    /**
     * Closes the SDK and releases resources. This should be called when the SDK is no longer
     * needed.
     */
    override fun close() {
        coroutineScope.cancel()
        httpClientInternal.dispatcher.executorService.shutdown()
        httpClientInternal.connectionPool.evictAll()
    }

    /** Configuration for retry behavior */
    data class RetryConfig
    @JvmOverloads
    constructor(
            val maxRetries: Int = 3,
            val initialDelayMs: Long = 500,
            val maxDelayMs: Long = 10000,
    ) {
        init {
            require(maxRetries > 0) { "maxRetries must be positive" }
            require(initialDelayMs > 0) { "initialDelayMs must be positive" }
            require(maxDelayMs >= initialDelayMs) { "maxDelayMs must be >= initialDelayMs" }
        }
    }

    /** Builder class for SkillSdk */
    class Builder {
        private var apiKey: String? = null
        private var baseUrl: String = "https://skill.ivk.dev"
        private var environment: String = "production"
        private var retryConfig: RetryConfig = RetryConfig()
        private var httpClient: OkHttpClient? = null

        /** Set the API key for authentication (required) */
        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

        /** Set the base URL for the API (optional, defaults to https://skill.ivk.dev) */
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        /** Set the environment (optional, defaults to "production") */
        fun environment(environment: String) = apply { this.environment = environment }

        /** Set custom retry configuration (optional) */
        fun retryConfig(retryConfig: RetryConfig) = apply { this.retryConfig = retryConfig }

        /**
         * Set custom OkHttpClient (optional). Note: API key interceptor will be added to this
         * client
         */
        fun httpClient(httpClient: OkHttpClient) = apply { this.httpClient = httpClient }

        /**
         * Build the SkillSdk instance.
         *
         * @throws IllegalStateException if required parameters are missing
         */
        fun build(): SkillSdk {
            val apiKey = this.apiKey ?: throw IllegalStateException("API key is required")

            require(baseUrl.isNotBlank()) { "Base URL cannot be blank" }
            require(environment.isNotBlank()) { "Environment cannot be blank" }

            return SkillSdk(
                    apiKey = apiKey,
                    baseUrl = baseUrl,
                    environment = environment,
                    retryConfig = retryConfig,
                    httpClient = httpClient
            )
        }
    }

    /** Interceptor to add API key to requests */
    private class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request =
                    chain.request().newBuilder().addHeader("x-ivk-apikey", apiKey).build()
            return chain.proceed(request)
        }
    }

    companion object {
        /**
         * Creates a new Builder instance.
         *
         * @return A new Builder for constructing SkillSdk instances
         */
        @JvmStatic fun builder(): Builder = Builder()
    }
}
