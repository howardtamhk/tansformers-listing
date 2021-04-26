package tam.howard.transformer_listing.ui.listing

import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformerTeam
import tam.howard.transformer_listing.ui.listing.model.TransformerFightResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

@Singleton
class TransformerFightHelper @Inject constructor() {

    private val autoWinTransformers: List<String> =
        listOf("Optimus Prime", "Predaking").map { it.toLowerCase() }

    fun fight(transformers: List<Transformer>): TransformerFightResult {
        val comparator =
            compareByDescending<Transformer> { it.rank }
                .thenByDescending { it.rating }
                .thenBy { it.name }

        val autobots = transformers.filter { it.team == TransformerTeam.Autobot }
            .sortedWith(comparator)
        val decepticons =
            transformers.filter { it.team == TransformerTeam.Decepticon }.sortedWith(comparator)

        val numberOfRound = min(autobots.size, decepticons.size)
        if (numberOfRound == 0) {
            return TransformerFightResult(0)
        }

        var autobotWinCount = 0
        var deceptionWinCount = 0
        (0 until numberOfRound).forEach { i ->
            when (fight(autobot = autobots[i], deception = decepticons[i])) {
                TransformerFightResult.WinningTeam.Autobot -> autobotWinCount++
                TransformerFightResult.WinningTeam.Deception -> deceptionWinCount++
                TransformerFightResult.WinningTeam.Tie -> {
                }
                TransformerFightResult.WinningTeam.AutoWinCompletion -> {
                    return TransformerFightResult(
                        noOfFight = i + 1,
                        winningTeam = TransformerFightResult.WinningTeam.AutoWinCompletion
                    )
                }
            }
        }

        val winningTeam = when {
            autobotWinCount == deceptionWinCount -> TransformerFightResult.WinningTeam.Tie
            autobotWinCount > deceptionWinCount -> TransformerFightResult.WinningTeam.Autobot
            else -> TransformerFightResult.WinningTeam.Deception
        }

        return TransformerFightResult(
            noOfFight = numberOfRound,
            winningTeam = winningTeam,
            winningTeamLastTransformer = when (winningTeam) {
                TransformerFightResult.WinningTeam.Autobot -> autobots[numberOfRound - 1]
                TransformerFightResult.WinningTeam.Deception -> decepticons[numberOfRound - 1]
                else -> null
            },
            survivorFromLosingTeam = when (winningTeam) {
                TransformerFightResult.WinningTeam.Autobot -> decepticons.drop(numberOfRound)
                TransformerFightResult.WinningTeam.Deception -> autobots.drop(numberOfRound)
                else -> listOf()
            }

        )
    }

    /**
     * pass 1 Autobot and 1 Deception to calcuate the winning team between these two Transformers
     * @param [autobot] Autobot Transformer
     * @param [deception] Deception Transformer
     * @return [TransformerTeam] winning transformer team
     * @throws [IllegalStateException] if pass in Deception Transformer to [autobot] or vice versa
     */
    private fun fight(
        autobot: Transformer,
        deception: Transformer
    ): TransformerFightResult.WinningTeam {
        if (autobot.team != TransformerTeam.Autobot || deception.team != TransformerTeam.Decepticon) {
            throw IllegalStateException()
        }

        // auto win rules
        if (autobot.name.toLowerCase() in autoWinTransformers && deception.name.toLowerCase() in autoWinTransformers) {
            return TransformerFightResult.WinningTeam.AutoWinCompletion
        } else if (autobot.name.toLowerCase() in autoWinTransformers) {
            return TransformerFightResult.WinningTeam.Autobot
        } else if (deception.name.toLowerCase() in autoWinTransformers) {
            return TransformerFightResult.WinningTeam.Deception
        }

        //ran away rules
        val courageDiff = autobot.courage - deception.courage
        val strengthDiff = autobot.strength - deception.strength
        if (courageDiff.sign == strengthDiff.sign && abs(courageDiff) >= 4 && strengthDiff >= 3) {
            return if (courageDiff.sign > 0) {
                TransformerFightResult.WinningTeam.Autobot
            } else {
                TransformerFightResult.WinningTeam.Deception
            }
        }

        //skill rules
        val skillDiff = autobot.skill - deception.skill
        if (abs(skillDiff) >= 3) {
            return if (skillDiff.sign > 0) {
                TransformerFightResult.WinningTeam.Autobot
            } else {
                TransformerFightResult.WinningTeam.Deception
            }
        }

        //overall rating
        return when {
            autobot.rating == deception.rating -> TransformerFightResult.WinningTeam.Tie
            autobot.rating > deception.rating -> TransformerFightResult.WinningTeam.Autobot
            else -> TransformerFightResult.WinningTeam.Deception
        }
    }

}