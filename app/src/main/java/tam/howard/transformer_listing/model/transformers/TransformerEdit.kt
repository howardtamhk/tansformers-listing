package tam.howard.transformer_listing.model.transformers

import kotlinx.serialization.Serializable

@Serializable
data class TransformerEdit(
    val id: String? = null,
    val name: String = "",
    val strength: Int,
    val intelligence: Int,
    val speed: Int,
    val endurance: Int,
    val rank: Int,
    val courage: Int,
    val firepower: Int,
    val skill: Int,
    val team: TransformerTeam? = null,
) {
    val isDataValid: Boolean
        get() = name.isNotBlank() &&
                strength in 1..10 &&
                intelligence in 1..10 &&
                speed in 1..10 &&
                endurance in 1..10 &&
                rank in 1..10 &&
                courage in 1..10 &&
                firepower in 1..10 &&
                skill in 1..10 &&
                team != null
}
