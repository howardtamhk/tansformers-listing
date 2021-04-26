package tam.howard.transformer_listing.ui.listing.model

import tam.howard.transformer_listing.model.transformers.Transformer

data class TransformerFightResult(
    val noOfFight: Int,
    val winningTeam: WinningTeam? = null,
    val winningTeamLastTransformer: Transformer? = null,
    val survivorFromLosingTeam: List<Transformer> = listOf()
) {
    enum class WinningTeam {
        Tie, Autobot, Deception, AutoWinCompletion
    }
}
