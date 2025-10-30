package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to update an item in an order
 */
class UpdateOrderItemUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, item: OrderItem): Resource<Unit> {
        return repository.updateOrderItem(orderId, item)
    }
}