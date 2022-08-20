package com.example.cryptoapp.domain.usecase

import com.example.cryptoapp.domain.CoinRepository

class LoadDataUseCase(private val repository: CoinRepository) {

    suspend operator fun invoke() = repository.loadData()
}