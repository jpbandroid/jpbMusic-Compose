package com.jpb.music.compose.backend.realm

import com.jpb.music.compose.backend.realm.collections.Album
import com.jpb.music.compose.backend.realm.collections.Artist
import com.jpb.music.compose.backend.realm.collections.Playlist
import com.jpb.music.compose.backend.realm.collections.Song
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun getRealm(): Realm{
    val config = RealmConfiguration.Builder(
        schema = setOf(
            Song::class,
            Album::class,
            Artist::class,
            Playlist::class
        )
    )
        .schemaVersion(11)
        .compactOnLaunch()
        .build()

    return Realm.open(config)
}