package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to get the next sale number
 */
class GetNextSaleNumberUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): Resource<Int> {
        return repository.getNextSaleNumber()
    }
}