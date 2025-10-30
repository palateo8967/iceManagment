package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to add a new order
 */
class AddOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(order: Order): Resource<String> {
        return repository.addOrder(order)
    }
}