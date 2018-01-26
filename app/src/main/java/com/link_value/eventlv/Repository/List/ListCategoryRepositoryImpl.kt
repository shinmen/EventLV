package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Infrastructure.Network.HttpEventLvInterface
import com.link_value.eventlv.View.ListEvent.ListCategoryView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await

/**
 * Created by julienb on 26/01/18.
 */
class ListCategoryRepositoryImpl(private val httpClient: HttpClient): ListCategoryRepository {
    override fun queryCategories(listener: ListCategoryView) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoCategories = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                val list = repoCategories.getCategories().await()

                listener.onCategoriesFetched(list)
            } catch (ex: Exception) {
                listener.onErrorCategoryFetch(ex.message)
            }
        }
    }
}