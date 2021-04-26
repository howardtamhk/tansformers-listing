package tam.howard.transformer_listing.provider.api

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure

class ResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) = delegate.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when (response.isSuccessful) {
                true -> {
                    response.body()?.let {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.Success(response.code(), it))
                        )
                    } ?: callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.Failure(response.code(), ResultFailure.EmptyBody))
                    )
                }
                else -> {
                    val errorBodyString = response.errorBody()?.string()
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(
                            Result.Failure(
                                response.code(),
                                ResultFailure.ApiError(errorBodyString)
                            )
                        )
                    )
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(
                this@ResultCall,
                Response.success(Result.Failure(failure = ResultFailure.NetworkFailure(t)))
            )
        }
    })

    override fun clone(): Call<Result<T>> = ResultCall(delegate)

    override fun execute(): Response<Result<T>> {
        throw UnsupportedOperationException("ResultCall doesn't support execute")
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}