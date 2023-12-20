package com.jpb.music.compose.backend.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jpb.music.compose.backend.realm.Queries
import com.jpb.music.compose.backend.realm.collections.Album
import com.jpb.music.compose.backend.realm.collections.Artist
import com.jpb.music.compose.backend.realm.collections.Song
import com.jpb.music.compose.backend.realm.getRealm
import com.jpb.music.compose.backend.utils.hasNotificationsPermission
import com.jpb.music.compose.backend.utils.hasStoragePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppVM(application: Application) : AndroidViewModel(application) {

}