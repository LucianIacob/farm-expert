/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/8/19 1:42 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.TextViewWithHeaderAndExpandAndEdit
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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
        animalRef = FirebaseFirestore.getInstance()
            .collection(FirestorePath.Collections.FARMS)
            .document(farmId)
            .collection(FirestorePath.Collections.ANIMALS)
            .document(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animalId.text = args.id
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
        animalRef.get()
            .addOnFailureListener { toast(R.string.unknown_error) }
            .addOnSuccessListener { populateUi(it.toObject(Animal::class.java)) }
            .addOnCompleteListener { loadingHide() }
    }

    private fun populateUi(animal: Animal?) {
        currentAnimal = animal
        animal?.let {
            raceView.setValue(it.race)
            dateOfBirthView.setValue(it.dateOfBirth.toDate().getShort())
            genderView.setValue(it.gender)
            fatherIdView.setValue(it.fatherId)
            fatherFatherIdView.setValue(it.fatherFatherId)
            fatherMotherIdView.setValue(it.fatherMotherId)
            motherIdView.setValue(it.motherId)
            motherFatherIdView.setValue(it.motherFatherId)
            motherMotherIdView.setValue(it.motherMotherId)
        }
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