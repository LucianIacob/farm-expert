/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.DropdownUtils.getGenderByPosition
import kotlinx.android.synthetic.main.dialog_add_animal.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 05 January, 2018.
 */

class AddAnimalDialogFragment : FullScreenAddRecordDialogFragment(R.string.add_animal_title) {

    override val dialogContent: Int = R.layout.dialog_add_animal

    override fun onUiElementsReady() {
        super.onUiElementsReady()
        fillDropdownComponent(dialogGender, R.array.gender_types_values)
    }

    override fun addRecordClicked() {
        val bundle = bundleOf(
            ADD_DIALOG_ANIMAL to dialogId.text.toString(),
            DIALOG_DATE to currentDate.time,
            ADD_DIALOG_GENDER to getGenderByPosition(dialogGender, resources),
            ADD_DIALOG_RACE to dialogRace.text.toString(),
            ADD_DIALOG_FATHER to dialogFather.text.toString(),
            ADD_DIALOG_MOTHER to dialogMother.text.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        val TAG: String = AddAnimalDialogFragment::class.java.simpleName
    }
}
