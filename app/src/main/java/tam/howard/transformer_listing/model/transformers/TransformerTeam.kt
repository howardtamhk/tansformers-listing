package tam.howard.transformer_listing.model.transformers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransformerTeam {
    @SerialName("A")
    Autobot,
    @SerialName("D")
    Decepticon,

}