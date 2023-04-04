/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.R

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 02 January, 2018.
 */

enum class DashboardItem(
    val iconId: Int,
    val titleId: Int,
    val actionDestination: Int
) {

    HEADCOUNT(
        R.drawable.dashboard_headcount,
        R.string.dashboard_headcount,
        R.id.action_dashboardFragment_to_animalMasterFragment
    ),
    BIRTHS_GRAPH(
        R.drawable.dashboard_births_graph,
        R.string.dashboard_graph_births,
        R.id.action_dashboardFragment_to_birthsGraphFragment
    ),
    BREEDING_GRAPH(
        R.drawable.dashboard_breedings_graph,
        R.string.dashboard_graph_breedings,
        R.id.action_dashboardFragment_to_breedingsGraphFragment
    ),
    DISINFECTION_GRAPH(
        R.drawable.dashboard_disinfections_graph,
        R.string.dashboard_graph_disinfections,
        R.id.action_dashboardFragment_to_disinfectionsGraphFragment
    ),
    VITAMINISATION_GRAPH(
        R.drawable.dashboard_vitaminisation_graph,
        R.string.dashboard_graph_vitaminisations,
        R.id.action_dashboardFragment_to_vitaminisationsGraphFragment
    ),
    VACCINATION_GRAPH(
        R.drawable.dashboard_vaccination_graph,
        R.string.dashboard_graph_vaccinations,
        R.id.action_dashboardFragment_to_vaccinationsGraphFragment
    ),
    PEDICURES_GRAPH(
        R.drawable.dashboard_pedicures_graph,
        R.string.dashboard_graph_pedicures,
        R.id.action_dashboardFragment_to_pedicuresGraphFragment
    ),
    TREATMENTS_GRAPH(
        R.drawable.dashboard_treatments_graph,
        R.string.dashboard_graph_treatments,
        R.id.action_dashboardFragment_to_treatmentsGraphFragment
    )
}
