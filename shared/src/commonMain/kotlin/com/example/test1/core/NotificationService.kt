package com.example.test1.core

interface NotificationService {
    fun showTimerNotification(title: String, content: String, isOngoing: Boolean = false)
    fun dismissNotification()
}
