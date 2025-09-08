
# PreMatchTeamResult

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **beta** | **kotlin.Double** | Total uncertainty in team outcome |  |
| **density** | **kotlin.Double** | The weighted team size |  |
| **expected** | **kotlin.Double** | The expected team outcome based on party and team model (0-1) Potentially clamped to a maximum value (typically 0.8) to guarantee minimum payoffs |  |
| **id** | **kotlin.String** | The local identifier of the team (passthrough) |  |
| **idx** | **kotlin.Int** | Zero-indexed team index |  |
| **mmr** | **kotlin.Double** | Total MMR of the team as determined by the party and team models (not necessarily the sum of MMRs) |  |
| **partyCount** | **kotlin.Int** | Total parties in the team |  |
| **propertySize** | **kotlin.Int** | Total player count the team has seen throughout the match |  |



