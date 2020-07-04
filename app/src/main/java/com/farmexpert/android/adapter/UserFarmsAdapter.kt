package com.farmexpert.android.adapter

import android.view.ViewGroup
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.SubscribedFarmHolder
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.inflate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UserFarmsAdapter(
    options: FirestoreRecyclerOptions<Farm>,
    private val unsubscribeListener: (Farm) -> Unit,
    private val deleteListener: (Farm) -> Unit
) : FirestoreRecyclerAdapter<Farm, SubscribedFarmHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubscribedFarmHolder(parent.inflate(R.layout.item_user_farm))

    override fun onBindViewHolder(farmHolder: SubscribedFarmHolder, p1: Int, farm: Farm) {
        farmHolder.bind(farm, unsubscribeListener, deleteListener)
    }
}
