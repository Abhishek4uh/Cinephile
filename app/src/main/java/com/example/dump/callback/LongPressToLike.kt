package com.example.dump.callback

import com.example.dump.offline.MovieOfflineModel

interface LongPressToLike {
    fun longPressToLikeItem(content:MovieOfflineModel)
}