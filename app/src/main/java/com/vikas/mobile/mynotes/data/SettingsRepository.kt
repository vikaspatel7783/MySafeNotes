package com.vikas.mobile.mynotes.data

import com.vikas.mobile.mynotes.data.entity.Setting

interface SettingsRepository {

    suspend fun getSetting(name: String): Setting

    suspend fun updateSettings(setting: Setting)
}