package com.jpb.music.compose.backend.realm.collections

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Album: RealmObject {
    @PrimaryKey var id: Long = 0L
    var artistId: Long = 0L
    var name: String = ""
}