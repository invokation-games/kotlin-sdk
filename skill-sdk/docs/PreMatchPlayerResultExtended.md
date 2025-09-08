
# PreMatchPlayerResultExtended

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **alpha** | **kotlin.Double** | Calculated scale factor for uncertainty and step sizes Typically &#x3D; 1.0 outside of placement |  |
| **isBot** | **kotlin.Boolean** | Passthrough from request |  |
| **mmrDelta** | **kotlin.Double** | Expected change in MMR if match ends as predicted |  |
| **playerExpected** | **kotlin.Double** | Expected individual outcome for the player (0-1) Potentially clamped to a maximum value (typically 0.8) to guarantee minimum payoffs |  |
| **playerWeight** | **kotlin.Double** | How much weight individual outcome is expected to influence MMR |  |
| **teamExpected** | **kotlin.Double** | Expected weighted average of outcomes for all teams this player is a member of |  |
| **teamWeight** | **kotlin.Double** | How much weight team outcome is expected to influence MMR |  |
| **unifiedExpected** | **kotlin.Double** | Combined team and individual expected outcomes |  |
| **unifiedExpectedDist** | [**BetaDistribution**](BetaDistribution.md) | The Beta distribution of expected outcomes Encodes how much certainty we have about the unified expected outcome |  |



