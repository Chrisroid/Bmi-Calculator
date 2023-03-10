package com.auto.christianasarhasa.domain.usecase.get_bmi_message

import com.auto.christianasarhasa.common.BmiCategories
import com.auto.christianasarhasa.domain.repository.ApplicationRepository
import javax.inject.Inject

class GetBmiMessageUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke(name : String, bmiCategory : BmiCategories) : String {
        return repository.getBmiMessage(name, bmiCategory)
    }
}