package com.example.chiptest.di

import com.example.chiptest.data.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val CHIP_OKHTTP = "chipOkhttp"
const val CHIP_RETROFIT = "chipRetrofit"

val networkModule = module {
    single(named(CHIP_OKHTTP)) { provideOkHttpClient() }
    single(named(CHIP_RETROFIT)) { provideRetrofit(get(named(CHIP_OKHTTP))) }
    single { provideApiService(get(named(CHIP_RETROFIT))) }
}

fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .header("Accept", "application/json")
        val response = chain.proceed(newRequest.build())
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)

    return retrofitBuilder.build()
}


fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)