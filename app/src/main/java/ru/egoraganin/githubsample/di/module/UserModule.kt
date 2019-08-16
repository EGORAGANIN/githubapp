package ru.egoraganin.githubsample.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.egoraganin.githubsample.di.qualifier.GraphInternal
import ru.egoraganin.githubsample.di.scope.UserScope
import ru.egoraganin.githubsample.entity.login.UserIdentity
import ru.egoraganin.githubsample.model.data.cache.common.GithubDb
import ru.egoraganin.githubsample.model.data.cache.repo.IUserReposDao
import ru.egoraganin.githubsample.model.data.server.IUserRestService
import ru.egoraganin.githubsample.model.data.server.Route
import ru.egoraganin.githubsample.model.repository.repo.IUserReposRepository
import ru.egoraganin.githubsample.model.repository.repo.UserReposRepository
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.model.system.provider.IExecutorProvider
import ru.egoraganin.githubsample.model.system.provider.OkHttpClientFactory

@Module
class UserModule(private val userIdentity: UserIdentity) {

    @Provides
    @UserScope
    fun provideUserRestService(@GraphInternal gson: Gson): IUserRestService {
        return Retrofit.Builder()
            .baseUrl(Route.BASE_API_URL)
            .client(OkHttpClientFactory.createClient(userIdentity))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IUserRestService::class.java)
    }

    @Provides
    @UserScope
    fun provideUserReposDao(db: GithubDb): IUserReposDao {
        return db.getUserReposDb()
    }

    @Provides
    @UserScope
    fun provideUserReposRepository(
        dispatcherProvider: IDispatcherProvider,
        executorProvider: IExecutorProvider,
        userRestService: IUserRestService,
        userReposDao: IUserReposDao
    ): IUserReposRepository {
        return UserReposRepository(dispatcherProvider, executorProvider, userRestService, userReposDao)
    }
}