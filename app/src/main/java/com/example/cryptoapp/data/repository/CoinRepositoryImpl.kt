package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinInfoMapper
import com.example.cryptoapp.data.worker.RefreshDataWorker
import com.example.cryptoapp.domain.CoinRepository
import com.example.cryptoapp.domain.model.CoinInfoEntity
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val application: Application,
    private val coinInfoDao: CoinInfoDao,
    private val mapper: CoinInfoMapper
) : CoinRepository {

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> =
        Transformations.map(coinInfoDao.getPriceList()) {
            mapper.mapListDbToEntity(it)
        }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfoEntity> =
        Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            mapper.mapDbToEntity(it)
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