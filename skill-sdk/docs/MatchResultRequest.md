
# MatchResultRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **playerSessions** | [**kotlin.collections.List&lt;PlayerSession&gt;**](PlayerSession.md) | A list of all player sessions for one single match. There can be multiple sessions for the same player. |  |
| **teams** | [**kotlin.collections.List&lt;TeamInfo&gt;**](TeamInfo.md) | A list of teams and their metadata |  |
| **matchEndTs** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time the match ended (ISO 8601 UTC timestamp). Apart from backfilling historical data or simulations, you probably don&#39;t need to pass this. |  [optional] |
| **matchId** | **kotlin.String** | Unique match identifier |  [optional] |
| **matchStartTs** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time the match started (ISO 8601 UTC timestamp). Apart from backfilling historical data or simulations, you probably don&#39;t need to pass this. |  [optional] |
| **metadata** | [**MatchMetadata**](MatchMetadata.md) | Several properties to provide more context about the match |  [optional] |



