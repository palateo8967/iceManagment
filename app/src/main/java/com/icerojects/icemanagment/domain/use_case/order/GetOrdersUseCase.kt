package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all orders
 */
class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(): Flow<Resource<List<Order>>> {
        return repository.getOrders()
    }
}