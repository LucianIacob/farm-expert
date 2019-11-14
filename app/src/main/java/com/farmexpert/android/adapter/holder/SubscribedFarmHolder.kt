package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.asDisplayable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_user_farm.view.*

class SubscribedFarmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        farm: Farm,
        unsubscribeListener: (Farm) -> Unit,
        deleteListener: (Farm) -> Unit
    ) = with(itemView) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_CURRENT_FARM_ID, null)
            ?.equals(farm.id)
            ?.takeIfTrue()
            ?.let { currentFarm.visibility = View.VISIBLE }
            ?: run { currentFarm.visibility = View.GONE }

        FirebaseAuth.getInstance()
            .currentUser
            ?.uid
            .equals(farm.owner)
            .takeIfTrue()
            ?.let {
                ownerStatus.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE
            }
            ?: run {
                ownerStatus.visibility = View.GONE
                deleteBtn.visibility = View.GONE
            }

        farmName.text = farm.name
        createdAt.text = itemView.resources.getString(
            R.string.farm_creation_date,
            farm.createdOn.asDisplayable()
        )
        subscribersCount.text = itemView.resources.getString(
            R.string.farm_subscribers_count,
            farm.users.size
        )

        unsubscribeBtn.setOnClickListener { unsubscribeListener.invoke(farm) }
        deleteBtn.setOnClickListener { deleteListener.invoke(farm) }
    }
}

private fun Boolean.takeIfTrue() = this.takeIf { it }
