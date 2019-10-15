package com.example.planner.calendar.demolist

import androidx.annotation.StringRes
import com.example.planner.R

/**
 * Created by Alexander Surinov on 2019-05-13.
 */
enum class DemoItem(@StringRes val titleRes: Int) {
    SELECTION(titleRes = R.string.demo_selection),
    DATE_BOUNDARIES(titleRes = R.string.demo_date_boundaries),
    STYLING(titleRes = R.string.demo_styling),
    EVENTS(titleRes = R.string.demo_events),
    DIALOG(titleRes = R.string.demo_dialog)
}