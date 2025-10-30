package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to remove an item from an order
 */
class RemoveOrderItemUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, itemId: String): Resource<Unit> {
        return repository.removeOrderItem(orderId, itemId)
    }
}