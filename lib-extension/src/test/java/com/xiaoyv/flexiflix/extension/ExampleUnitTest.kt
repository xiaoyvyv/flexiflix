package com.xiaoyv.flexiflix.extension.java

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val data = mutableListOf(
            1,
            2,
            3,
            4,
            5,
            6,
            1000,
            8,
            9,
            10,
            2000,
            -1000,
            -1000,
            -1000,
            -1000,
            11,
            12,
            13,
            3000,
            14,
            15,
            16,
            17,
            18,
            19,
            4000,
            440444441,
            440444442,
            440444443,
            440444444,
            20
        )
        val filteredData = removeOutliers(data)
        println(filteredData)
    }

    fun removeOutliers(data: List<Int>): List<Int> {
        if (data.isEmpty()) return data

        fun percentile(data: List<Int>, percentile: Int): Double {
            val index = percentile / 100.0 * (data.size - 1)
            val lower = data[index.toInt()]
            val upper = data[ceil(index).toInt()]
            return lower + (upper - lower) * (index - index.toInt())
        }

        val sortedData = data.sorted()
        val q1 = percentile(sortedData, 25)
        val q3 = percentile(sortedData, 75)
        val iqr = q3 - q1
        val lowerBound = q1 - 1.5 * iqr
        val upperBound = q3 + 1.5 * iqr

        return sortedData.filter { it >= lowerBound && it <= upperBound }
    }
}