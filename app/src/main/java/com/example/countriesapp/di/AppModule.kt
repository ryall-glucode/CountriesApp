package com.example.countriesapp.di

import android.app.Application
import com.example.countriesapp.api.ApiService
import com.example.countriesapp.db.AppDao
import com.example.countriesapp.db.AppDatabase
import com.example.countriesapp.domain.providers.CountriesProvider
import com.example.countriesapp.domain.providers.CountriesProviderImpl
import com.example.countriesapp.repository.CountryRepository
import com.example.countriesapp.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getAppDatabase(context: Application): AppDatabase {
        return AppDatabase.getAppDbInstance(context)
    }

    @Singleton
    @Provides
    fun getAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.getAppDao()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, appDao: AppDao) = CountryRepository(apiService, appDao)

    @Singleton
    @Provides
    fun countriesProvider(repo: CountryRepository): CountriesProvider = CountriesProviderImpl(repo)
}