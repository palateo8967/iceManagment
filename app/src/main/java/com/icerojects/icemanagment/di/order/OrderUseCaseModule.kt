package com.icerojects.icemanagment.di.order

import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.domain.use_case.order.AddOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.order.AddOrderUseCase
import com.icerojects.icemanagment.domain.use_case.order.AddSaleUseCase
import com.icerojects.icemanagment.domain.use_case.order.DeleteOrderUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetNextSaleNumberUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetOrderByIdUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetOrdersUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetSaleByIdUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetSalesUseCase
import com.icerojects.icemanagment.domain.use_case.order.RemoveOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.order.UpdateOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.order.UpdateOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderUseCaseModule {
    
    @Provides
    @Singleton
    fun provideGetOrdersUseCase(repository: OrderRepository): GetOrdersUseCase {
        return GetOrdersUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetOrderByIdUseCase(repository: OrderRepository): GetOrderByIdUseCase {
        return GetOrderByIdUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideAddOrderUseCase(repository: OrderRepository): AddOrderUseCase {
        return AddOrderUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideUpdateOrderUseCase(repository: OrderRepository): UpdateOrderUseCase {
        return UpdateOrderUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideDeleteOrderUseCase(repository: OrderRepository): DeleteOrderUseCase {
        return DeleteOrderUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideAddOrderItemUseCase(repository: OrderRepository): AddOrderItemUseCase {
        return AddOrderItemUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideRemoveOrderItemUseCase(repository: OrderRepository): RemoveOrderItemUseCase {
        return RemoveOrderItemUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideUpdateOrderItemUseCase(repository: OrderRepository): UpdateOrderItemUseCase {
        return UpdateOrderItemUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetSalesUseCase(repository: OrderRepository): GetSalesUseCase {
        return GetSalesUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetSaleByIdUseCase(repository: OrderRepository): GetSaleByIdUseCase {
        return GetSaleByIdUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideAddSaleUseCase(repository: OrderRepository): AddSaleUseCase {
        return AddSaleUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetNextSaleNumberUseCase(repository: OrderRepository): GetNextSaleNumberUseCase {
        return GetNextSaleNumberUseCase(repository)
    }
}