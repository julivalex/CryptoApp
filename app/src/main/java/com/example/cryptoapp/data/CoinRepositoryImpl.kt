package com.example.cryptoapp.data

import androidx.lifecycle.LiveData
import com.example.cryptoapp.domain.CoinRepository
import com.example.cryptoapp.domain.model.CoinInfoEntity

class CoinRepositoryImpl: CoinRepository {

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> {
        TODO("Not yet implemented")
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfoEntity> {
        TODO("Not yet implemented")
    }
}