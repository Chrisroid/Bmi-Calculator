package com.auto.christianasarhasa.domain.repository

import com.auto.christianasarhasa.common.BmiCategories

interface ApplicationRepository {

    fun getBmiIndexCalculation (weight: Double, height : Double) : Double

    fun getPonderalIndexCalculation (weight: Double, height : Double) : Double

    fun getBmiCategory (bmi : Double) : BmiCategories

    fun getBmiMessage (name: String, bmiCategories: BmiCategories) : String

    fun getBmiMessageRange (bmiCategories: BmiCategories) : String
}