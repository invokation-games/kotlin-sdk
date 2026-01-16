package ivk.skill.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class SkillSdkTest : ShouldSpec({

    context("Builder") {
        should("throw IllegalStateException when apiKey is not set") {
            val exception = shouldThrow<IllegalStateException> {
                SkillSdk.builder().build()
            }
            exception.message shouldContain "API key is required"
        }

        should("build successfully with apiKey") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .build()
            sdk.close()
        }

        should("use default baseUrl") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .build()
            // SDK is created successfully with default baseUrl
            sdk.close()
        }

        should("allow custom baseUrl") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .baseUrl("https://custom.example.com")
                .build()
            sdk.close()
        }

        should("throw IllegalArgumentException for blank baseUrl") {
            shouldThrow<IllegalArgumentException> {
                SkillSdk.builder()
                    .apiKey("test-api-key")
                    .baseUrl("")
                    .build()
            }
        }

        should("throw IllegalArgumentException for blank environment") {
            shouldThrow<IllegalArgumentException> {
                SkillSdk.builder()
                    .apiKey("test-api-key")
                    .environment("")
                    .build()
            }
        }

        should("allow custom environment") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .environment("staging")
                .build()
            sdk.close()
        }

        should("allow custom retryConfig") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .retryConfig(SkillSdk.RetryConfig(maxRetries = 5, initialDelayMs = 1000, maxDelayMs = 30000))
                .build()
            sdk.close()
        }
    }

    context("RetryConfig") {
        should("use default values") {
            val config = SkillSdk.RetryConfig()
            config.maxRetries shouldBe 3
            config.initialDelayMs shouldBe 500
            config.maxDelayMs shouldBe 10000
        }

        should("throw IllegalArgumentException for non-positive maxRetries") {
            shouldThrow<IllegalArgumentException> {
                SkillSdk.RetryConfig(maxRetries = 0)
            }
            shouldThrow<IllegalArgumentException> {
                SkillSdk.RetryConfig(maxRetries = -1)
            }
        }

        should("throw IllegalArgumentException for non-positive initialDelayMs") {
            shouldThrow<IllegalArgumentException> {
                SkillSdk.RetryConfig(initialDelayMs = 0)
            }
            shouldThrow<IllegalArgumentException> {
                SkillSdk.RetryConfig(initialDelayMs = -1)
            }
        }

        should("throw IllegalArgumentException when maxDelayMs is less than initialDelayMs") {
            shouldThrow<IllegalArgumentException> {
                SkillSdk.RetryConfig(initialDelayMs = 1000, maxDelayMs = 500)
            }
        }

        should("allow maxDelayMs equal to initialDelayMs") {
            val config = SkillSdk.RetryConfig(initialDelayMs = 1000, maxDelayMs = 1000)
            config.initialDelayMs shouldBe 1000
            config.maxDelayMs shouldBe 1000
        }
    }

    context("API Key Header") {
        should("add x-ivk-apikey header to requests") {
            val server = MockWebServer()
            server.enqueue(MockResponse().setResponseCode(200).setBody("""{"message": "ok"}"""))
            server.start()

            val sdk = SkillSdk.builder()
                .apiKey("my-secret-api-key")
                .baseUrl(server.url("/").toString().trimEnd('/'))
                .build()

            try {
                // Make a request that will fail with 404 but we can still check the header
                runCatching {
                    sdk.postPreMatchBlocking(
                        "model-id", ivk.skill.api.models.PreMatchRequest(
                            matchId = "test-match",
                            playerSessions = emptyList(),
                            teams = emptyList()
                        )
                    )
                }

                val recordedRequest = server.takeRequest()
                recordedRequest.getHeader("x-ivk-apikey") shouldBe "my-secret-api-key"
            } finally {
                sdk.close()
                server.shutdown()
            }
        }
    }

    context("Resource Cleanup") {
        should("close without throwing") {
            val sdk = SkillSdk.builder()
                .apiKey("test-api-key")
                .build()

            // Should not throw
            sdk.close()
        }

        should("be usable with use block") {
            SkillSdk.builder()
                .apiKey("test-api-key")
                .build()
                .use { sdk ->
                    // SDK is available in this block
                }
            // SDK is automatically closed after the block
        }
    }
})
