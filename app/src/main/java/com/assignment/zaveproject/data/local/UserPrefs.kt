package com.assignment.zaveproject.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.assignment.zaveproject.domain.model.User

class UserPrefs(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    companion object {
        val name = stringPreferencesKey("name")
        val email = stringPreferencesKey("email")
        val profile_pic = stringPreferencesKey("profile_pic")
    }

    suspend fun saveUserData(user: User){
        context.dataStore.edit{
            it[name] = user.name
            it[email] = user.email
            it[profile_pic] = user.profileImage
        }
    }

}