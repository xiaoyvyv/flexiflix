package com.xiaoyv.comic.flexiflix.data

import android.content.Context
import androidx.room.Room
import com.xiaoyv.comic.flexiflix.data.database.DatabaseRepository
import com.xiaoyv.comic.flexiflix.data.database.DatabaseRepositoryImpl
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepositoryImpl
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.comic.flexiflix.data.media.MediaRepositoryImpl
import com.xiaoyv.comic.flexiflix.data.remote.RemoteApi
import com.xiaoyv.flexiflix.common.database.LocalDatabase
import com.xiaoyv.flexiflix.common.database.collect.CollectionDao
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * [ModuleBindsContainer]
 *
 * @author why
 * @since 5/9/24
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ModuleBindsContainer {

    @Binds
    @Singleton
    abstract fun bindDatabaseRepository(repositoryImpl: DatabaseRepositoryImpl): DatabaseRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(repositoryImpl: MediaRepositoryImpl): MediaRepository


    @Binds
    @Singleton
    abstract fun bindExtensionRepository(repositoryImpl: ExtensionRepositoryImpl): ExtensionRepository
}

/**
 * [ModuleProvidesContainer]
 *
 * @author why
 * @since 5/9/24
 */
@Module
@InstallIn(SingletonComponent::class)
object ModuleProvidesContainer {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideLogDao(database: LocalDatabase): CollectionDao {
        return database.collectionDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return MediaSourceFactory.retrofitBuilder
            .baseUrl("http://localhost")
            .build()
    }

    @Provides
    fun provideRemoteApi(retrofit: Retrofit): RemoteApi {
        return retrofit.create(RemoteApi::class.java)
    }
}