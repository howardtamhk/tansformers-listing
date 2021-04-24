package tam.howard.transformer_listing.provider.api

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import tam.howard.transformer_listing.model.Result
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapter<T>(private val successType: Type) : CallAdapter<T, Call<Result<T>>> {
    override fun responseType(): Type = successType
    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call)
}

class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType) || returnType !is ParameterizedType) {
            return null
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Result::class.java || responseType !is ParameterizedType) {
            return null
        }

        val successBodyType = getParameterUpperBound(0, responseType)
        return ResultCallAdapter<Any>(successBodyType)
    }
}