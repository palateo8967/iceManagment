package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to delete an order
 */
class DeleteOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): Resource<Unit> {
        return repository.deleteOrder(orderId)
    }
}