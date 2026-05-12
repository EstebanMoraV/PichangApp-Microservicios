package cl.duoc.pichangapp.di

import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.data.remote.AuthApi
import cl.duoc.pichangapp.data.remote.KarmaApi
import cl.duoc.pichangapp.data.remote.NotificationApi
import cl.duoc.pichangapp.data.remote.UserApi
import cl.duoc.pichangapp.data.repository.AuthRepository
import cl.duoc.pichangapp.data.repository.KarmaRepository
import cl.duoc.pichangapp.data.repository.NotificationRepository
import cl.duoc.pichangapp.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, tokenDataStore: TokenDataStore): AuthRepository {
        return AuthRepository(authApi, tokenDataStore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi): UserRepository {
        return UserRepository(userApi)
    }

    @Provides
    @Singleton
    fun provideKarmaRepository(karmaApi: KarmaApi): KarmaRepository {
        return KarmaRepository(karmaApi)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationApi: NotificationApi): NotificationRepository {
        return NotificationRepository(notificationApi)
    }
}
