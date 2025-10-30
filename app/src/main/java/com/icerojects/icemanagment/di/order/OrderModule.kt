package com.icerojects.icemanagment.di.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager
import com.icerojects.icemanagment.data.remote.firestore.order.OrderFirestoreService
import com.icerojects.icemanagment.data.repository.order.OrderRepositoryImpl
import com.icerojects.icemanagment.domain.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {
    
    @Provides
    @Singleton
    fun provideOrderFirestoreService(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        firestoreManager: FirestoreManager
    ): OrderFirestoreService {
        return OrderFirestoreService(firestore, auth, firestoreManager)
    }
    
    @Provides
    @Singleton
    fun provideOrderRepository(
        orderFirestoreService: OrderFirestoreService
    ): OrderRepository {
        return OrderRepositoryImpl(orderFirestoreService)
    }
}