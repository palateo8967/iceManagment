package com.icerojects.icemanagment.domain.use_case.order

import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get a sale by its ID
 */
class GetSaleByIdUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(saleId: String): Flow<Resource<Sale?>> {
        return repository.getSaleById(saleId)
    }
}