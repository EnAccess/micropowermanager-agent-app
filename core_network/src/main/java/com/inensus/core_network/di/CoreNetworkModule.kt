package com.inensus.core_network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.inensus.core.utils.DateUtils.DATE_FORMAT_FULL
import com.inensus.core_network.BuildConfig
import com.inensus.core_network.gson.ExclusionGsonStrategy
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object CoreNetworkModule {

    private const val DEFAULT_TIMEOUT = 30L

    fun createCoreNetworkModule() = module {
        single { provideGson() }

        factory { (interceptors: InterceptorsModel) ->
            provideOkHttp(
                interceptors = if (BuildConfig.DEBUG) {
                    interceptors.interceptors + provideHttpLoggingInterceptor()
                } else {
                    interceptors.interceptors
                },
                networkInterceptors = interceptors.networkInterceptors
            )
        }

        factory(qualifier = Qualifiers.BASE_RETROFIT) { (interceptors: InterceptorsModel) ->
            provideRetrofit(
                okHttpClient = get(parameters = { parametersOf(interceptors) }),
                gson = get()
            )
        }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://localhost/api/")
            .build()

    private fun provideOkHttp(interceptors: List<Interceptor>?, networkInterceptors: List<Interceptor>?): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                interceptors?.forEach { addInterceptor(it) }
                networkInterceptors?.forEach { addNetworkInterceptor(it) }
            }
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                chain.proceed(requestBuilder.build())
            }
            .build()

    private fun provideGson(): Gson =
        GsonBuilder()
            .setExclusionStrategies(ExclusionGsonStrategy())
            .setDateFormat(DATE_FORMAT_FULL).create()

    private fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("Network").d(message)
            }
        })
            .apply { level = HttpLoggingInterceptor.Level.BODY }
}