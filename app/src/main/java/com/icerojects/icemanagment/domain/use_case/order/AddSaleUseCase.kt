package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to add a new sale
 */
class AddSaleUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(sale: Sale): Resource<String> {
        return repository.addSale(sale)
    }
}