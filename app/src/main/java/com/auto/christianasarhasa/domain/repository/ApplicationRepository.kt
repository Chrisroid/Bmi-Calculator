package com.auto.christianasarhasa.domain.repository

import com.auto.christianasarhasa.common.BmiCategories

interface ApplicationRepository {

    // function for bmi index calculation
    fun getBmiIndexCalculation (weight: Double, height : Double) : Double

    // function for ponderal index calculation
    fun getPonderalIndexCalculation (weight: Double, height : Double) : Double

    // function for bmi category
    fun getBmiCategory (bmi : Double) : BmiCategories

    // function for bmi message
    fun getBmiMessage (name: String, bmiCategories: BmiCategories) : String

    // function for bmi message range
    fun getBmiMessageRange (bmiCategories: BmiCategories) : String
}