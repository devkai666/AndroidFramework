package devkai.app.base

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class GsonProvider private constructor() {

    private val gson: Gson = GsonBuilder().create()

    fun get(): Gson {
        return gson
    }

    companion object {
        @get:Synchronized
        var instance: GsonProvider? = null
            private set
    }
}
