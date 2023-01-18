package com.auto.christianasarhasa.di

import com.auto.christianasarhasa.data.repository.ApplicationRepositoryImpl
import com.auto.christianasarhasa.domain.repository.ApplicationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getApplicationRepository() : ApplicationRepository {
        return ApplicationRepositoryImpl()
    }

}