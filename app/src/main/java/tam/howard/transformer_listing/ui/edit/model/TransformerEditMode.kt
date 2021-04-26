package tam.howard.transformer_listing.ui.edit.model

enum class TransformerEditMode(val value: String) {
    Create("crate"), Edit("edit");

    companion object {
        fun customValueOf(value: String): TransformerEditMode =
            values().firstOrNull { it.value == value } ?: Create
    }
}