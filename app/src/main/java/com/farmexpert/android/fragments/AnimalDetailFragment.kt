/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.model.Animal
import com.farmexpert.android.transactions.DeleteAnimalTransaction
import com.farmexpert.android.utils.*
import com.farmexpert.android.utils.DropdownUtils.getGenderByKey
import com.farmexpert.android.views.TextViewWithHeaderAndExpandAndEdit
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_animal_detail.*
import java.util.*

class AnimalDetailFragment : BaseFragment(R.layout.fragment_animal_detail) {

    private val args: AnimalDetailFragmentArgs by navArgs()

    private lateinit var animalRef: DocumentReference

    private var currentAnimal: Animal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        animalRef = farmReference
            .collection(FirestorePath.Collections.ANIMALS)
            .document(args.animalId)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_animal_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> displayDeleteDialog()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayDeleteDialog(): Boolean {
        context?.let { context ->
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.delete_animal)
                .setMessage(getString(R.string.delete_animal_message, args.animalId))
                .setPositiveButton(R.string.delete) { _, _ -> deleteAnimalConfirmed() }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .create()
                .run {
                    setOnShowListener {
                        getButton(DialogInterface.BUTTON_POSITIVE).applyFarmexpertStyle(
                            context,
                            redButton = true,
                        )
                    }
                    show()
                }
        }

        return true
    }

    private fun deleteAnimalConfirmed() {
        loadingShow()
        DeleteAnimalTransaction(
            farmReference = farmReference,
            successListener = { rootLayout?.snackbar(R.string.item_deleted) },
            failureListener = { error { it } },
            complete = {
                loadingHide()
                NavHostFragment.findNavController(this@AnimalDetailFragment)
                    .navigateUp()
            }
        ).execute(args.animalId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animalId.text = args.animalId
        fatherIdView.setExpandListener(viewToExpand = fatherParentsContainer)
        motherIdView.setExpandListener(viewToExpand = motherParentsContainer)
        raceView.setEditListener { editRace() }
        dateOfBirthView.setEditListener { editDateOfBirth() }
        genderView.setEditListener { editGender() }
        fatherIdView.setEditListener { editFather() }
        fatherFatherIdView.setEditListener { editFatherFather() }
        fatherMotherIdView.setEditListener { editFatherMother() }
        motherIdView.setEditListener { editMother() }
        motherFatherIdView.setEditListener { editMotherFather() }
        motherMotherIdView.setEditListener { editMotherMother() }
    }

    override fun onStart() {
        super.onStart()
        loadingShow()
        animalRef.get(fromSource())
            .addLoggableFailureListener { toast(R.string.unknown_error) }
            .addOnSuccessListener { populateUi(it.toObject<Animal>()) }
            .addOnCompleteListener {
                loadingHide()
                if (it.isSuccessful &&
                    it.isComplete &&
                    it.result?.exists() != true
                ) {
                    alert(
                        message = R.string.err_animal_not_exists,
                        isCancellable = false,
                        okListener = { activity?.onBackPressed() }
                    )
                }
            }
    }

    private fun fromSource() = if (args.shouldGetFromCache) Source.CACHE else Source.DEFAULT

    private fun populateUi(animal: Animal?) {
        currentAnimal = animal
        animal?.let {
            raceView?.value = it.race
            dateOfBirthView?.value = it.dateOfBirth.toDate().getShort()
            genderView?.value = getGenderByKey(it.gender, resources)
            fatherIdView?.value = it.fatherId
            fatherFatherIdView?.value = it.fatherFatherId
            fatherMotherIdView?.value = it.fatherMotherId
            motherIdView?.value = it.motherId
            motherFatherIdView?.value = it.motherFatherId
            motherMotherIdView?.value = it.motherMotherId
        }
    }

    override fun onResume() {
        super.onResume()
        goToBirths?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalBirthsDetailFragment(args.animalId))
        }
        goToBreedings?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalBreedingsDetailFragment(args.animalId))
        }
        goToDisinfections?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalDisinfectionsDetailFragment(args.animalId))
        }
        goToPedicures?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalPedicuresDetailFragment(args.animalId))
        }
        goToTreatments?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalTreatmentsDetailFragment(args.animalId))
        }
        goToVaccinations?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalVaccinationsDetailFragment(args.animalId))
        }
        goToVitaminizations?.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalVitaminizationsDetailFragment(args.animalId))
        }
    }

    private fun navigateWithDirections(navDirections: NavDirections) {
        NavHostFragment.findNavController(this).navigate(navDirections)
    }

    private fun editMotherMother() {
        readTextInput(
            hintId = R.string.mother_mother_id,
            currentValue = motherMotherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_MOTHER_ID,
                newValue = textRead,
                viewToUpdate = motherMotherIdView
            )
        }
    }

    private fun editMotherFather() {
        readTextInput(
            hintId = R.string.mother_father_id,
            currentValue = motherFatherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_FATHER_ID,
                newValue = textRead,
                viewToUpdate = motherFatherIdView
            )
        }
    }

    private fun editMother() {
        readTextInput(
            hintId = R.string.mother_id,
            currentValue = motherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_ID,
                newValue = textRead,
                viewToUpdate = motherIdView
            )
        }
    }

    private fun editFatherMother() {
        readTextInput(
            hintId = R.string.father_mother_id,
            currentValue = fatherMotherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_MOTHER_ID,
                newValue = textRead,
                viewToUpdate = fatherMotherIdView
            )
        }
    }

    private fun editFatherFather() {
        readTextInput(
            hintId = R.string.father_father_id,
            currentValue = fatherFatherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_FATHER_ID,
                newValue = textRead,
                viewToUpdate = fatherFatherIdView
            )
        }
    }

    private fun editFather() {
        readTextInput(
            hintId = R.string.father_id,
            currentValue = fatherIdView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_ID,
                newValue = textRead,
                viewToUpdate = fatherIdView
            )
        }
    }

    private fun editGender() {
        context?.let {
            val genderTypes = resources.getStringArray(R.array.gender_types_values)
            MaterialAlertDialogBuilder(it)
                .setItems(genderTypes) { _, position ->
                    updateField(
                        fieldToUpdate = FirestorePath.Animal.GENDER,
                        newValue = position,
                        viewToUpdate = genderView
                    )
                }
                .show()
        }
    }

    private fun editDateOfBirth() {
        currentAnimal?.run {
            MaterialDatePicker.Builder
                .datePicker()
                .setSelection(dateOfBirth.toDate().time)
                .build()
                .apply {
                    isCancelable = false
                    addOnPositiveButtonClickListener {
                        updateField(
                            FirestorePath.Animal.DATE_OF_BIRTH,
                            Timestamp(Date(it)),
                            this@AnimalDetailFragment.dateOfBirthView
                        )
                    }
                    showNow(this@AnimalDetailFragment.parentFragmentManager, "DatePicker")
                }
        }
    }

    private fun editRace() {
        readTextInput(
            hintId = R.string.race,
            currentValue = raceView.value
        ) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.RACE,
                newValue = textRead,
                viewToUpdate = raceView
            )
        }
    }

    private fun readTextInput(
        @StringRes hintId: Int,
        currentValue: String = "",
        update: (String) -> Unit,
    ) {
        context?.let { context ->
            val view = layoutInflater.inflate(R.layout.dialog_simple_edit, null)
            view.findViewById<TextInputLayout>(R.id.edittext_header).hint = getString(hintId)
            view.findViewById<TextInputEditText>(R.id.edittext).setText(currentValue)

            MaterialAlertDialogBuilder(context)
                .setView(view)
                .setPositiveButton(R.string.update) { _, _ ->
                    val edittext = view.findViewById<TextInputEditText>(R.id.edittext)
                    update(edittext.text.toString())
                }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .setCancelable(false)
                .show()
        }
    }

    private fun updateField(
        fieldToUpdate: String,
        newValue: Any,
        viewToUpdate: TextViewWithHeaderAndExpandAndEdit
    ) {
        loadingShow()
        animalRef.update(fieldToUpdate, newValue)
            .addOnSuccessListener {
                when (newValue) {
                    is Int -> viewToUpdate.value = getGenderByKey(newValue, resources)
                    is String -> viewToUpdate.value = newValue
                    is Timestamp -> viewToUpdate.value = newValue.asDisplayable()
                }
            }
            .addLoggableFailureListener {
                rootLayout?.snackbar(R.string.err_updating_animal)
            }
            .addOnCompleteListener { loadingHide() }
    }
}