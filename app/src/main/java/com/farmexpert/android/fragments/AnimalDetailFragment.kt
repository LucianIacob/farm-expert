/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 10:04 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.DatePickerDialog
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
import com.farmexpert.android.views.TextViewWithHeaderAndExpandAndEdit
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.fragment_animal_detail.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.alert
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
        alert(
            title = getString(R.string.delete_animal),
            message = getString(R.string.delete_animal_message, args.animalId)
        ) {
            noButton { }
            yesButton { deleteAnimalConfirmed() }
        }.show()

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
        animalRef.get(Source.CACHE) // todo not whether Source.CACHE should go in production
            .addOnFailureListener { toast(R.string.unknown_error) }
            .addOnSuccessListener { populateUi(it.toObject(Animal::class.java)) }
            .addOnCompleteListener { loadingHide() }
    }

    private fun populateUi(animal: Animal?) {
        currentAnimal = animal
        animal?.let {
            raceView?.setValue(it.race)
            dateOfBirthView?.setValue(it.dateOfBirth.toDate().getShort())
            genderView?.setValue(it.gender)
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
        readTextInput(hintId = R.string.mother_mother_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_MOTHER_ID,
                newValue = textRead,
                viewToUpdate = motherMotherIdView
            )
        }
    }

    private fun editMotherFather() {
        readTextInput(hintId = R.string.mother_father_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_FATHER_ID,
                newValue = textRead,
                viewToUpdate = motherFatherIdView
            )
        }
    }

    private fun editMother() {
        readTextInput(hintId = R.string.mother_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.MOTHER_ID,
                newValue = textRead,
                viewToUpdate = motherIdView
            )
        }
    }

    private fun editFatherMother() {
        readTextInput(hintId = R.string.father_mother_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_MOTHER_ID,
                newValue = textRead,
                viewToUpdate = fatherMotherIdView
            )
        }
    }

    private fun editFatherFather() {
        readTextInput(hintId = R.string.father_father_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_FATHER_ID,
                newValue = textRead,
                viewToUpdate = fatherFatherIdView
            )
        }
    }

    private fun editFather() {
        readTextInput(hintId = R.string.father_id) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.FATHER_ID,
                newValue = textRead,
                viewToUpdate = fatherIdView
            )
        }
    }

    private fun editGender() {
        val genderTypes = resources.getStringArray(R.array.gender_types)
        selector(items = genderTypes.asList()) { _, position ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.GENDER,
                newValue = genderTypes[position],
                viewToUpdate = genderView
            )
        }
    }

    private fun editDateOfBirth() {
        currentAnimal?.run {
            DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val newTimestamp = AppUtils.timestampFor(year, month, day)
                    updateField(FirestorePath.Animal.DATE_OF_BIRTH, newTimestamp, dateOfBirthView)
                },
                yearOfBirth(),
                monthOfBirth(),
                dayOfBirth()
            ).show()
        }
    }

    private fun editRace() {
        readTextInput(hintId = R.string.race) { textRead ->
            updateField(
                fieldToUpdate = FirestorePath.Animal.RACE,
                newValue = textRead,
                viewToUpdate = raceView
            )
        }
    }

    private fun readTextInput(@StringRes hintId: Int, update: (String) -> Unit) {
        alert {
            customView = ctx.UI {
                frameLayout {
                    padding = dip(10)
                    val newRace = editText().apply { this.hint = getString(hintId) }
                    positiveButton(R.string.common_google_play_services_update_button) {
                        update(newRace.text.toString())
                    }
                    negativeButton(R.string.fui_cancel) {}
                }
            }.view
        }.show()
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
                    is String -> viewToUpdate.setValue(newValue)
                    is Timestamp -> viewToUpdate.setValue(newValue.asDisplayable())
                }
            }
            .addOnFailureListener { rootLayout.snackbar(R.string.err_updating_animal) }
            .addOnCompleteListener { loadingHide() }
    }
}