package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinInfoMapper
import com.example.cryptoapp.data.worker.RefreshDataWorker
import com.example.cryptoapp.domain.CoinRepository
import com.example.cryptoapp.domain.model.CoinInfoEntity

class CoinRepositoryImpl(private val application: Application) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> =
        Transformations.map(coinInfoDao.getPriceList()) {
            CoinInfoMapper.mapListDbToEntity(it)
        }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfoEntity> =
        Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            CoinInfoMapper.mapDbToEntity(it)
        }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND,
            RefreshDataWorker.makeRequest()
        )
    }
}