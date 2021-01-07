package com.headmostlab.exoplayertest

import android.app.Notification
import com.google.android.exoplayer2.ext.workmanager.WorkManagerScheduler
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper

class MyDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    ExoPlayerUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.exo_download_notification_channel_name,
    0
) {

    override fun getDownloadManager(): DownloadManager {
        return ExoPlayerUtil.getDownloadManager(this)
    }

    override fun getScheduler(): Scheduler {
        return WorkManagerScheduler(this, WORK_NAME)
    }

    override fun getForegroundNotification(downloads: List<Download>): Notification {
        return DownloadNotificationHelper(this, ExoPlayerUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .buildProgressNotification(
                this, R.mipmap.ic_launcher, null,
                null, downloads
            )
    }

    companion object {
        private const val WORK_NAME = "DOWNLOAD_SONGS"
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }
}