package com.auto.christianasarhasa.data.repository

import com.auto.christianasarhasa.common.BmiCategories
import com.auto.christianasarhasa.common.Constants
import com.auto.christianasarhasa.domain.repository.ApplicationRepository

class ApplicationRepositoryImpl : ApplicationRepository {
    // calculate the bmi by taking in weight and height double
    // run bmi formula, (weight / height / height) * 10000
    override fun getBmiIndexCalculation(weight: Double, height: Double) = (weight / height / height) * 10000

    // Calculate the ponderal index
    // by taking in the weight and height variables which are doubles and
    // dividing (weight /  height / height / height) * 1000000
    override fun getPonderalIndexCalculation(weight: Double, height: Double)  = (weight / height / height / height) * 1000000


    /*
    If bmi double is less than 18.50 return under weight
    if bmi double is greater than or equal to 18.50 and is less than 25.00 return healthy weight
    if bmi double is greater than or equal to 25.00 and also less than 30.00 return over weight
    else if bmi double is greater than or equal to 30.00 return obesity weight
     */
    override fun getBmiCategory(bmi: Double): BmiCategories {
        return if(bmi < Constants.BMI_UPPER_UNDERWEIGHT) BmiCategories.UNDER_WEIGHT
        else if(bmi >= Constants.BMI_UPPER_UNDERWEIGHT && bmi < Constants.BMI_UPPER_HEALTHYWEIGHT) BmiCategories.HEALTHY_WEIGHT
        else if(bmi >= Constants.BMI_UPPER_HEALTHYWEIGHT && bmi < Constants.BMI_UPPER_OVERWEIGHT) BmiCategories.OVER_WEIGHT
        else BmiCategories.OBESITY
    }

    /*
    bmi string for all weight categories with name variables
     */
    override fun getBmiMessage(name: String, bmiCategories: BmiCategories): String {
        return when(bmiCategories) {
            BmiCategories.UNDER_WEIGHT -> "Hello ${name}, you are underweight"
            BmiCategories.HEALTHY_WEIGHT -> "Hello ${name}, you are normal"
            BmiCategories.OVER_WEIGHT -> "Hello ${name}, you are overweight"
            else -> "Hello ${name}, you are obese"
        }
    }

    /*
    message for all weights categories
     */
    override fun getBmiMessageRange(bmiCategories: BmiCategories): String {
        return when(bmiCategories) {
            BmiCategories.UNDER_WEIGHT -> "Underweight bmi range: Below ${Constants.BMI_UPPER_UNDERWEIGHT}kg/m2"
            BmiCategories.HEALTHY_WEIGHT -> "Normal bmi range: ${Constants.BMI_UPPER_UNDERWEIGHT}kg.m2 - ${Constants.BMI_UPPER_HEALTHYWEIGHT}kg/m2"
            BmiCategories.OVER_WEIGHT -> "Overweight bmi range: ${Constants.BMI_UPPER_HEALTHYWEIGHT}kg.m2 - ${Constants.BMI_UPPER_OVERWEIGHT}kg/m2"
            else -> "Underweight bmi range: Above ${Constants.BMI_UPPER_OVERWEIGHT}kg/m2"
        }
    }
}