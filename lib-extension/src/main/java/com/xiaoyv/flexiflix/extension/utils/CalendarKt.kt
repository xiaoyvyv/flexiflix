package com.xiaoyv.flexiflix.extension.utils

import java.util.Calendar

/**
 * [calendar]
 *
 * @author why
 * @since 5/18/24
 */
val calendar: Calendar
    get() = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }

val currentYear: Int
    get() = calendar.get(Calendar.YEAR)

val currentMonth: Int
    get() = calendar.get(Calendar.MONTH) + 1

val currentWeekOfYear: Int
    get() = calendar.get(Calendar.WEEK_OF_YEAR) + 1

val currentWeekOfMonth: Int
    get() = calendar.get(Calendar.WEEK_OF_MONTH) + 1