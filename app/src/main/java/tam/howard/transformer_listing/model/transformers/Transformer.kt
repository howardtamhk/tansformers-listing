package tam.howard.transformer_listing.model.transformers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transformer(
    val id: String,
    val name: String,
    val strength: Int,
    val intelligence: Int,
    val speed: Int,
    val endurance: Int,
    val rank: Int,
    val courage: Int,
    val firepower: Int,
    val skill: Int,
    val team: TransformerTeam,
    @SerialName("team_icon")
    val teamIcon: String
) {
    val rating: Int = strength + intelligence + speed + endurance + firepower
    val ratingStarValue: Float = rating / 10f

    fun toTransformerEdit(): TransformerEdit = TransformerEdit(
        id = id,
        name = name,
        strength = strength,
        intelligence = intelligence,
        speed = speed,
        endurance = endurance,
        rank = rank,
        courage = courage,
        firepower = firepower,
        skill = skill,
        team = team,
    )
}
