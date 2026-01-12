# SkillApi

All URIs are relative to *https://skill.ivk.dev*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getConfiguration**](SkillApi.md#getConfiguration) | **GET** /api/v2/{model_id}/configuration |  |
| [**postMatchResult**](SkillApi.md#postMatchResult) | **POST** /api/v2/{model_id}/match_result/{environment} |  |
| [**postPreMatch**](SkillApi.md#postPreMatch) | **POST** /api/v2/{model_id}/pre_match/{environment} |  |


<a id="getConfiguration"></a>
# **getConfiguration**
> ConfigurationResponse getConfiguration(modelId)



 Get the current active model configuration

### Example
```kotlin
// Import classes:
//import ivk.skill.api.infrastructure.*
//import ivk.skill.api.models.*

val apiInstance = SkillApi()
val modelId : kotlin.String = abc123 // kotlin.String | ID of the skill rating model
try {
    val result : ConfigurationResponse = apiInstance.getConfiguration(modelId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SkillApi#getConfiguration")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SkillApi#getConfiguration")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **modelId** | **kotlin.String**| ID of the skill rating model | |

### Return type

[**ConfigurationResponse**](ConfigurationResponse.md)

### Authorization


Configure api_key:
    ApiClient.apiKey["x-ivk-apikey"] = ""
    ApiClient.apiKeyPrefix["x-ivk-apikey"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="postMatchResult"></a>
# **postMatchResult**
> MatchResultResponse postMatchResult(modelId, environment, matchResultRequest)



During or after the match, you can retrieve skill rating updates based on the provided player sessions.

### Example
```kotlin
// Import classes:
//import ivk.skill.api.infrastructure.*
//import ivk.skill.api.models.*

val apiInstance = SkillApi()
val modelId : kotlin.String = abc123 // kotlin.String | ID of the skill rating model
val environment : kotlin.String = prod // kotlin.String | Environment
val matchResultRequest : MatchResultRequest = {"match_id":"unique_id_1234","player_sessions":[{"player_id":"player_1","player_score":200,"prior_games_played":80,"prior_mmr":0.5},{"player_id":"player_2","player_score":250,"prior_games_played":70,"prior_mmr":0.4}],"teams":[]} // MatchResultRequest | 
try {
    val result : MatchResultResponse = apiInstance.postMatchResult(modelId, environment, matchResultRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SkillApi#postMatchResult")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SkillApi#postMatchResult")
    e.printStackTrace()
}
```

### Parameters
| **modelId** | **kotlin.String**| ID of the skill rating model | |
| **environment** | **kotlin.String**| Environment | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **matchResultRequest** | [**MatchResultRequest**](MatchResultRequest.md)|  | |

### Return type

[**MatchResultResponse**](MatchResultResponse.md)

### Authorization


Configure api_key:
    ApiClient.apiKey["x-ivk-apikey"] = ""
    ApiClient.apiKeyPrefix["x-ivk-apikey"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="postPreMatch"></a>
# **postPreMatch**
> PreMatchResponse postPreMatch(modelId, environment, preMatchRequest)



Useful to calculate the expected outcome.

### Example
```kotlin
// Import classes:
//import ivk.skill.api.infrastructure.*
//import ivk.skill.api.models.*

val apiInstance = SkillApi()
val modelId : kotlin.String = abc123 // kotlin.String | ID of the skill rating model
val environment : kotlin.String = prod // kotlin.String | Environment
val preMatchRequest : PreMatchRequest = {"match_id":"unique_id_1234","player_sessions":[{"player_id":"player_1","prior_games_played":80,"prior_mmr":0.5},{"player_id":"player_2","prior_games_played":70,"prior_mmr":0.4}],"teams":[]} // PreMatchRequest | 
try {
    val result : PreMatchResponse = apiInstance.postPreMatch(modelId, environment, preMatchRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SkillApi#postPreMatch")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SkillApi#postPreMatch")
    e.printStackTrace()
}
```

### Parameters
| **modelId** | **kotlin.String**| ID of the skill rating model | |
| **environment** | **kotlin.String**| Environment | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **preMatchRequest** | [**PreMatchRequest**](PreMatchRequest.md)|  | |

### Return type

[**PreMatchResponse**](PreMatchResponse.md)

### Authorization


Configure api_key:
    ApiClient.apiKey["x-ivk-apikey"] = ""
    ApiClient.apiKeyPrefix["x-ivk-apikey"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

