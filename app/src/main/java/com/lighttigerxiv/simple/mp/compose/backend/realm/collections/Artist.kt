package com.jpb.music.compose.backend.realm.collections

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Artist: RealmObject{
    @PrimaryKey var id: Long = 0L
    var name: String = ""
}