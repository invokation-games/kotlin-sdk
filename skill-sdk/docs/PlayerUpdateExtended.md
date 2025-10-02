
# PlayerUpdateExtended

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **alpha** | **kotlin.Double** | Calculated scale factor for uncertainty and step sizes typically &#x3D; 1.0 outside of placement |  |
| **botLevel** | **kotlin.Double** |  |  |
| **isBot** | **kotlin.Boolean** | Passthrough from request |  |
| **isFinalPlacement** | **kotlin.Boolean** | Was this the last match of placement |  |
| **maxTs** | **kotlin.Double** |  |  |
| **minTs** | **kotlin.Double** |  |  |
| **mmrDelta** | **kotlin.Double** | Change in MMR after this match |  |
| **placementFrac** | **kotlin.Double** | All the teams the player was a part of during this match Fraction of placement completed (max of 1.0, but can be negative) |  |
| **playerExpected** | **kotlin.Double** | Expected individual outcome for the player (0-1) potentially clamped to a maximum value (typically 0.8) to guarantee minimum payoffs |  |
| **playerOutcome** | **kotlin.Double** | Actual individual outcome for the player (0-1) |  |
| **playerScoreRate** | **kotlin.Double** | Player score per time unit |  |
| **playerWeight** | **kotlin.Double** | How much weight individual outcome has to influence MMR |  |
| **residual** | **kotlin.Double** | Difference between actual and expected outcomes (prediction error) |  |
| **sessionCount** | **kotlin.Int** | Total number of sessions for this player during this match |  |
| **teamCount** | **kotlin.Int** | The amount of teams the player was a part of during this match |  |
| **teamExpected** | **kotlin.Double** | Weighted average of expected outcomes of all teams this player was a member of |  |
| **teamOutcome** | **kotlin.Double** | Weighted average of actual outcomes of all teams this player was a memeber of |  |
| **teamWeight** | **kotlin.Double** | How much weight team outcome has to influences MMR |  |
| **unifiedExpected** | **kotlin.Double** | Combined team and individual expected outcomes |  |
| **unifiedExpectedDist** | [**BetaDistribution**](BetaDistribution.md) | The Beta distribution of expected outcomes Encodes how much certainty we have about the unified expected outcome |  |
| **unifiedOutcome** | **kotlin.Double** | Combined team and individual actual outcomes |  |



