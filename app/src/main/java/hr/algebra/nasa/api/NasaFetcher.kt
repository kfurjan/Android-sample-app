package hr.algebra.nasa.api

import android.content.Context
import android.util.Log
import hr.algebra.nasa.NasaReceiver
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.nasa.handler.downloadImageAndStore
import hr.algebra.nasa.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaFetcher(private val context: Context) {

    private var nasaApi: NasaApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retrofit.create(NasaApi::class.java)
    }

    fun fetchItems() {
        val request = nasaApi.fetchItems()
        request.enqueue(object : Callback<List<NasaItem>> {
            override fun onResponse(
                call: Call<List<NasaItem>>,
                response: Response<List<NasaItem>>
            ) {
                if (response.body() != null) {
                    populateItems(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<NasaItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(nasaItems: List<NasaItem>) {
        // problem -> u foregroundu sam
        // moram opet u background, da procesiram
        GlobalScope.launch {
            val items: MutableList<Item> = mutableListOf()
            nasaItems.forEach {
                // sto sa slikama?
                // moram downloadat slike
                val picturePath = downloadImageAndStore(context, it.url, it.title.replace(" ", "_"))
                items.add(
                    Item(
                        null, it.title, it.explanation,
                        picturePath ?: "", // ako nema picturePath, stavi ""
                        it.date, false
                    )
                )
            }
            context.sendBroadcast<NasaReceiver>()
        }
    }
}