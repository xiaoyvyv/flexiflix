package com.xiaoyv.comic.flexiflix.data

import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepositoryImpl
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.comic.flexiflix.data.media.MediaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [ModuleContainer]
 *
 * @author why
 * @since 5/9/24
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ModuleContainer {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(repositoryImpl: MediaRepositoryImpl): MediaRepository


    @Binds
    @Singleton
    abstract fun bindExtensionRepository(repositoryImpl: ExtensionRepositoryImpl): ExtensionRepository
}