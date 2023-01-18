package com.auto.christianasarhasa.domain.usecase.get_bmi_message_range

import com.auto.christianasarhasa.common.BmiCategories
import com.auto.christianasarhasa.domain.repository.ApplicationRepository
import javax.inject.Inject

class GetBmiMessageRangeUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke(bmiCategory : BmiCategories) : String {
        return repository.getBmiMessageRange(bmiCategory)
    }
}