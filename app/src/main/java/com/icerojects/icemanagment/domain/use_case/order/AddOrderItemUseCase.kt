package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to add an item to an order
 */
class AddOrderItemUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, item: OrderItem): Resource<String> {
        return repository.addOrderItem(orderId, item)
    }
}