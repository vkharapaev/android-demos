package com.headmostlab.exoplayertest

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.util.concurrent.Executor

object ExoPlayerUtil {

    const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"

    private lateinit var downloadCache: Cache
    private lateinit var databaseProvider: ExoDatabaseProvider
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var httDataSourceFactory: HttpDataSource.Factory
    private lateinit var downloadManager: DownloadManager

    fun getDownloadManager(context: Context): DownloadManager {
        if (!ExoPlayerUtil::downloadManager.isInitialized) {
            val executor = Executor(Runnable::run)
            downloadManager = DownloadManager(
                context,
                getDatabaseProvide(context),
                getDownloadCache(context),
                getDataSourceFactory(context),
                executor
            )
            downloadManager.maxParallelDownloads = 3
        }
        return downloadManager
    }

    @Synchronized
    fun getDataSourceFactory(context: Context): DataSource.Factory {
        var context = context
        if (!ExoPlayerUtil::dataSourceFactory.isInitialized) {
            context = context.applicationContext
            dataSourceFactory = CacheDataSource.Factory()
                .setCache(getDownloadCache(context))
                .setUpstreamDataSourceFactory(getHttDataSourceFactory())
                .setCacheWriteDataSinkFactory(null) // Disable writing
        }
        return dataSourceFactory
    }

    @Synchronized
    private fun getHttDataSourceFactory(): HttpDataSource.Factory {
        if (!ExoPlayerUtil::httDataSourceFactory.isInitialized) {
            httDataSourceFactory = DefaultHttpDataSourceFactory()
        }
        return httDataSourceFactory
    }

    @Synchronized
    private fun getDownloadCache(context: Context): Cache {
        if (!ExoPlayerUtil::downloadCache.isInitialized) {
            downloadCache = SimpleCache(
                context.cacheDir, LeastRecentlyUsedCacheEvictor(1024 * 1024 * 5),
                getDatabaseProvide(context)
            )
        }
        return downloadCache
    }

    @Synchronized
    private fun getDatabaseProvide(context: Context): DatabaseProvider {
        if (!ExoPlayerUtil::databaseProvider.isInitialized) {
            databaseProvider = ExoDatabaseProvider(context)
        }
        return databaseProvider
    }
}
