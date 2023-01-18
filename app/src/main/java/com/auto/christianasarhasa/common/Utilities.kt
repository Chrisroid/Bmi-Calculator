package com.auto.christianasarhasa.common

import java.math.RoundingMode
import java.text.DecimalFormat

enum class Genders{
    MALE, FEMALE
}

enum class BmiCategories{
    UNDER_WEIGHT, HEALTHY_WEIGHT, OVER_WEIGHT, OBESITY
}

object Utilities {

    val genders: ArrayList<Genders> = arrayListOf(
        Genders.MALE,
        Genders.FEMALE
    )

    // get the gender string from the gender number picker array
    fun getGenderString(): Array<String> {
        val genderStringList: MutableList<String> = arrayListOf()

        for (i in 0 until (genders.size - 1))
            genders.forEach { gender ->
                genderStringList.add(i, gender.toString())
            }

        return genderStringList.toTypedArray()
    }

    // calculate the bmi by taking in weight and height double
    // run bmi formula, (weight / height / height) * 10000
    fun calculateBmi(weight: Double, height: Double) = (weight / height / height) * 10000

    // Calculate the ponderal index
    // by taking in the weight and height variables which are doubles and
    // dividing (weight /  height / height / height) * 1000000
    fun calculatePonderalIndex(weight: Double, height: Double) =
        (weight / height / height / height) * 1000000

    // Round off whole number to the nearest lowest integer and return the whole number
    fun roundOffToTwoDp(number: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(number)
    }


    // Split the given bmi double into two, integer one side and after decimal points another side, then return the figures after the decimal points
    fun getFormatBmiResult(bmi: Double): List<String> {
        val bmiTwoDp = roundOffToTwoDp(bmi)
        return bmiTwoDp.split(".")
    }
}