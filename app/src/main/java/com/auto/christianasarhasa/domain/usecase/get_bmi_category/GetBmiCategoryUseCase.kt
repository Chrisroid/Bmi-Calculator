package com.auto.christianasarhasa.domain.usecase.get_bmi_category

import com.auto.christianasarhasa.common.BmiCategories
import com.auto.christianasarhasa.domain.repository.ApplicationRepository
import javax.inject.Inject

class GetBmiCategoryUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {

    operator fun invoke(bmi : Double) : BmiCategories {
        return repository.getBmiCategory(bmi)
    }
}