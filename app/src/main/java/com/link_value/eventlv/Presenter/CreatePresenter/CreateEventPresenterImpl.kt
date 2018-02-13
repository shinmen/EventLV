package com.link_value.eventlv.Presenter.CreatePresenter

import com.link_value.eventlv.Infrastructure.LocationApi.AddressPredictionFetcher
import com.link_value.eventlv.Model.AddressEventLV
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Repository.Create.NewEventRepository
import com.link_value.eventlv.Repository.List.ListCategoryRepository
import com.link_value.eventlv.View.Create.CreateEventView
import com.link_value.eventlv.View.ListEvent.ListCategoryView

/**
 * Created by julo on 30/12/17.
 */
class CreateEventPresenterImpl(
        private val mNewView: CreateEventView,
        private val mListCategoryView: ListCategoryView,
        private val mNewRepo: NewEventRepository,
        private val mCategoryRepository: ListCategoryRepository
        //private val addressFetcher: AddressPredictionFetcher
    ): CreateEventPresenter {

    override fun start() {
        mCategoryRepository.queryCategories(mListCategoryView)
    }
    override fun onSuccessEventSaved() {
        mNewView.onEventPersisted()
    }

    override fun onErrorEventSaved(error: String?) {
        mNewView.onError(error)
    }

    override fun persistEventLv(event: EventLV) {
        mNewRepo.saveEvent(event, this)
    }

    /*suspend fun fetchAddresses(query: String): List<AddressEventLV> {
        return addressFetcher.fetchAddressPrediction(query)
    }*/


}

