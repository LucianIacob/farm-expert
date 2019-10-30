/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.model.Animal
import com.farmexpert.android.transactions.DeleteAnimalTransaction
import com.farmexpert.android.utils.*
import com.farmexpert.android.utils.SpinnerUtils.getGenderByKey
import com.farmexpert.android.views.TextViewWithHeaderAndExpandAndEdit
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_animal_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast

class AnimalDetailFragment : BaseFragment() {

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_animal_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> displayDeleteDialog()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayDeleteDialog(): Boolean {
        context?.let { context ->
            AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setTitle(R.string.delete_animal)
                .setMessage(getString(R.string.delete_animal_message, args.animalId))
                .setPositiveButton(R.string.delete) { _, i ->
                    deleteAnimalConfirmed()
                }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .run {
                    setOnShowListener {
                        getButton(DialogInterface.BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                        getButton(DialogInterface.BUTTON_POSITIVE).applyFarmexpertStyle(
                            context,
                            redButton = true
                        )
                    }
                    show()
                }
        }

        return true
    }

    private fun deleteAnimalConfirmed() {
        loadingShow()
        DeleteAnimalTransaction(farmReference,
            { rootLayout.snackbar(R.string.item_deleted) },
            { exception -> error { exception } },
            {
                loadingHide()
                NavHostFragment.findNavController(this@AnimalDetailFragment)
                    .navigateUp()
            }).execute(args.animalId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animalId.text = args.animalId
        fatherIdView.setExpandListener(viewToExpand = fatherParentsContainer)
        motherIdView.setExpandListener(viewToExpand = motherParentsContainer)
        raceView.setEditListener(View.OnClickListener { editRace() })
        dateOfBirthView.setEditListener(View.OnClickListener { editDateOfBirth() })
        genderView.setEditListener(View.OnClickListener { editGender() })
        fatherIdView.setEditListener(View.OnClickListener { editFather() })
        fatherFatherIdView.setEditListener(View.OnClickListener { editFatherFather() })
        fatherMotherIdView.setEditListener(View.OnClickListener { editFatherMother() })
        motherIdView.setEditListener(View.OnClickListener { editMother() })
        motherFatherIdView.setEditListener(View.OnClickListener { editMotherFather() })
        motherMotherIdView.setEditListener(View.OnClickListener { editMotherMother() })
    }

    override fun onViewReady() {
        loadingShow()
        animalRef.get(if (args.shouldGetFromCache) Source.CACHE else Source.DEFAULT)
            .addOnFailureListener { toast(R.string.unknown_error) }
            .addOnSuccessListener { populateUi(it.toObject<Animal>()) }
            .addOnCompleteListener {
                loadingHide()
                if (it.isSuccessful &&
                    it.isComplete &&
                    it.result?.exists() != true
                ) {
                    failureAlert(
                        message = R.string.err_animal_not_exists,
                        isCancellable = false,
                        okListener = { activity?.onBackPressed() }
                    )
                }
            }
    }

    private fun populateUi(animal: Animal?) {
        currentAnimal = animal
        animal?.let {
            raceView?.setValue(it.race)
            dateOfBirthView?.setValue(it.dateOfBirth.toDate().getShort())
            genderView?.setValue(getGenderByKey(it.gender, resources))
            fatherIdView?.setValue(it.fatherId)
            fatherFatherIdView?.setValue(it.fatherFatherId)
            fatherMotherIdView?.setValue(it.fatherMotherId)
            motherIdView?.setValue(it.motherId)
            motherFatherIdView?.setValue(it.motherFatherId)
            motherMotherIdView?.setValue(it.motherMotherId)
        }
    }

    override fun onResume() {
        super.onResume()
        goToBirths.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalBirthsDetailFragment(args.animalId))
        }
        goToBreedings.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalBreedingsDetailFragment(args.animalId))
        }
        goToDisinfections.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalDisinfectionsDetailFragment(args.animalId))
        }
        goToPedicures.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalPedicuresDetailFragment(args.animalId))
        }
        goToTreatments.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalTreatmentsDetailFragment(args.animalId))
        }
        goToVaccinations.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalVaccinationsDetailFragment(args.animalId))
        }
        goToVitaminizations.setOnClickListener {
            navigateWithDirections(NavGraphDirections.actionGlobalVitaminizationsDetailFragment(args.animalId))
        }
    }

    private fun navigateWithDirections(navDirections: NavDirections) {
        NavHostFragment.findNavController(this).navigate(navDirections)
    }

    override fun onPause() {
        super.onPause()
        goToBirths.setOnClickListener(null)
        goToBreedings.setOnClickListener(null)
        goToDisinfections.setOnClickListener(null)
        goToPedicures.setOnClickListener(null)
        goToTreatments.setOnClickListener(null)
        goToVaccinations.setOnClickListener(null)
        goToVitaminizations.setOnClickListener(null)
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
        val genderTypes = resources.getStringArray(R.array.gender_types_values)
        selector(items = genderTypes.asList()) { _, position ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.GENDER,
                newValue = position,
                viewToUpdate = genderView
            )
        }
    }

    private fun editDateOfBirth() {
        currentAnimal?.run {
            context?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        val newTimestamp = AppUtils.timestampFor(year, month, day)
                        updateField(
                            FirestorePath.Animal.DATE_OF_BIRTH,
                            newTimestamp,
                            dateOfBirthView
                        )
                    },
                    yearOfBirth(),
                    monthOfBirth(),
                    dayOfBirth()
                ).show()
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
        update: (String) -> Unit
    ) {
        context?.let { context ->
            val view = layoutInflater.inflate(R.layout.dialog_simple_edit, null)
            view.findViewById<TextInputLayout>(R.id.edittext_header).hint = getString(hintId)
            view.findViewById<TextInputEditText>(R.id.edittext).setText(currentValue)

            AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setView(view)
                .setPositiveButton(R.string.common_google_play_services_update_button) { _, i ->
                    val edittext = view.findViewById<TextInputEditText>(R.id.edittext)
                    update(edittext.text.toString())
                }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .apply {
                    setOnShowListener {
                        getButton(DialogInterface.BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                        getButton(DialogInterface.BUTTON_POSITIVE).applyFarmexpertStyle(context)
                    }
                    show()
                }
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
                    is Int -> viewToUpdate.setValue(getGenderByKey(newValue, resources))
                    is String -> viewToUpdate.setValue(newValue)
                    is Timestamp -> viewToUpdate.setValue(newValue.asDisplayable())
                }
            }
            .addOnFailureListener { rootLayout.snackbar(R.string.err_updating_animal) }
            .addOnCompleteListener { loadingHide() }
    }
}