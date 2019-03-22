/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/21/19 4:13 PM.
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
    val titleId: Int/*,
                         val actionDestination: Int*/
) {

    HEADCOUNT(R.drawable.dashboard_headcount, R.string.dashboard_efectiv_animale),
    BIRTHS_GRAPH(R.drawable.dashboard_births_graph, R.string.dashboard_grafic_fatari),
    BREEDING_GRAPH(R.drawable.dashboard_breedings_graph, R.string.dashboard_grafic_monta),
    DISINFECTION_GRAPH(R.drawable.dashboard_disinfections_graph, R.string.dashboard_grafic_deparazitari),
    VITAMINISATION_GRAPH(R.drawable.dashboard_vitaminisation_graph, R.string.dashboard_grafic_vitaminizari),
    VACCINATION_GRAPH(R.drawable.dashboard_vaccination_graph, R.string.dashboard_grafic_vaccinari),
    PEDICURES_GRAPH(R.drawable.dashboard_pedicures_graph, R.string.dashboard_grafic_pedichiuri),
    TREATMENTS_GRAPH(R.drawable.dashboard_treatments_graph, R.string.dashboard_grafic_tratamente)

}
