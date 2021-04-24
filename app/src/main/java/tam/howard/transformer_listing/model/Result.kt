package tam.howard.transformer_listing.model

sealed class Result<out T>(open val statusCode: Int? = null) {
    data class Success<out T>(override val statusCode: Int? = null, val value: T) :
        Result<T>(statusCode) {

        fun <U> map(transform: (T) -> (U)): Result<U> =
            Success(this.statusCode, transform(value))
    }

    data class Failure(override val statusCode: Int? = null, val failure: ResultFailure? = null) :
        Result<Nothing>(statusCode)
}

sealed class ResultFailure {
    object EmptyBody : ResultFailure()

    data class ApiError(val errorBodyStr: String?) : ResultFailure()
    data class NetworkFailure(val e: Throwable?) : ResultFailure()
}
