package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.model.CoinInfoDb
import com.example.cryptoapp.data.mapper.CoinInfoMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.domain.CoinRepository
import com.example.cryptoapp.domain.model.CoinInfoEntity
import kotlinx.coroutines.delay

class CoinRepositoryImpl(
    private val application: Application
) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> =
        Transformations.map(coinInfoDao.getPriceList()) {
            CoinInfoMapper.mapListDbToEntity(it)
        }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfoEntity> =
        Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            CoinInfoMapper.mapDbToEntity(it)
        }

    override suspend fun loadData() {
        while (true) {
            val topCoins = apiService.getTopCoinsInfo(limit = 50)
            val fromSymbols: String = CoinInfoMapper.mapNamesListToString(topCoins)
            val jsonContainer = apiService.getFullPriceList(fSyms = fromSymbols)
            val coinInfoDtoList: List<CoinInfoDto> = CoinInfoMapper.mapJsonToCoinInfo(jsonContainer)
            val coinInfoDbList: List<CoinInfoDb> = CoinInfoMapper.mapListDtoToDb(coinInfoDtoList)
            coinInfoDao.insertPriceList(coinInfoDbList)
            delay(1000)
        }
    }
}