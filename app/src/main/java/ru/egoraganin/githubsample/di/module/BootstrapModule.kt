package ru.egoraganin.githubsample.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.threeten.bp.Instant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.egoraganin.githubsample.di.AppGraphManager
import ru.egoraganin.githubsample.di.IAppGraphManager
import ru.egoraganin.githubsample.di.qualifier.GraphInternal
import ru.egoraganin.githubsample.di.scope.BootScope
import ru.egoraganin.githubsample.model.data.GsonDataMapper
import ru.egoraganin.githubsample.model.data.IDataMapper
import ru.egoraganin.githubsample.model.data.cache.common.GithubDb
import ru.egoraganin.githubsample.model.data.cache.login.AccessTokenStorage
import ru.egoraganin.githubsample.model.data.cache.login.IAccessTokenStorage
import ru.egoraganin.githubsample.model.data.exception.ExceptionHandler
import ru.egoraganin.githubsample.model.data.exception.IExceptionHandler
import ru.egoraganin.githubsample.model.data.server.ILoginRestService
import ru.egoraganin.githubsample.model.data.server.Route
import ru.egoraganin.githubsample.model.data.server.adapter.InstantTypeAdapter
import ru.egoraganin.githubsample.model.interactor.boot.BootstrapInteractor
import ru.egoraganin.githubsample.model.interactor.boot.IBootstrapInteractor
import ru.egoraganin.githubsample.model.interactor.login.ILoginStateInteractor
import ru.egoraganin.githubsample.model.interactor.login.LoginStateInteractor
import ru.egoraganin.githubsample.model.repository.login.ILoginStateRepository
import ru.egoraganin.githubsample.model.repository.login.LoginStateRepository
import ru.egoraganin.githubsample.model.system.customtabs.CustomTabManager
import ru.egoraganin.githubsample.model.system.customtabs.ICustomTabManager
import ru.egoraganin.githubsample.model.system.provider.*
import ru.terrakok.cicerone.Router

@Module
class BootstrapModule(private val context: Context) {

    @Provides
    @BootScope
    fun provideAppGraphManager(): IAppGraphManager {
        return AppGraphManager.instance()
    }

    @Provides
    @BootScope
    fun provideCustomTabManager(): ICustomTabManager {
        return CustomTabManager(context)
    }

    @Provides
    @BootScope
    fun provideConfigProvider(): IConfigProvider {
        return ConfigProvider()
    }

    @Provides
    @BootScope
    @GraphInternal
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
            .create()
    }

    @Provides
    @BootScope
    fun provideDispatcherProvider(): IDispatcherProvider {
        return DispatcherProvider()
    }

    @Provides
    @BootScope
    fun provideExecutorProvider(): IExecutorProvider {
        return ExecutorProvider()
    }

    @Provides
    @BootScope
    fun provideExceptionHandler(
        dispatcherProvider: IDispatcherProvider,
        router: Router,
        loginStateInteractor: ILoginStateInteractor
    ): IExceptionHandler {
        return ExceptionHandler(dispatcherProvider, router, loginStateInteractor)
    }

    @Provides
    @BootScope
    fun provideBootstrapInteractor(
        loginStateRepository: ILoginStateRepository,
        graphManager: IAppGraphManager
    ): IBootstrapInteractor {
        return BootstrapInteractor(loginStateRepository, graphManager)
    }

    @Provides
    @BootScope
    fun provideLoginStateInteractor(
        configProvider: IConfigProvider,
        dispatcherProvider: IDispatcherProvider,
        loginStateRepository: ILoginStateRepository,
        db: GithubDb,
        graphManager: IAppGraphManager
    ): ILoginStateInteractor {
        return LoginStateInteractor(
            configProvider,
            dispatcherProvider,
            loginStateRepository,
            db,
            graphManager
        )
    }

    @Provides
    @BootScope
    fun provideLoginStateRepository(
        dispatcherProvider: IDispatcherProvider,
        restService: ILoginRestService,
        tokenStorage: IAccessTokenStorage
    ): ILoginStateRepository {
        return LoginStateRepository(dispatcherProvider, restService, tokenStorage)
    }

    @Provides
    @BootScope
    fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("app_shared_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @BootScope
    fun provideDataMapper(@GraphInternal gson: Gson): IDataMapper {
        return GsonDataMapper(gson)
    }

    @Provides
    @BootScope
    fun provideAccessTokenStorage(prefs: SharedPreferences, mapper: IDataMapper): IAccessTokenStorage {
        return AccessTokenStorage.newInstance(prefs, mapper)
    }

    @Provides
    @BootScope
    fun provideILoginRestService(): ILoginRestService {
        return Retrofit.Builder()
            .baseUrl(Route.BASE_AUTH_URL)
            .client(OkHttpClientFactory.createClient(null))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ILoginRestService::class.java)
    }

    @Provides
    @BootScope
    fun provideGithubDb(): GithubDb {
        return Room.databaseBuilder(context, GithubDb::class.java, "database.db").build()
    }
}