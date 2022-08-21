package com.example.cryptoapp.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinInfoMapper
import com.example.cryptoapp.data.network.ApiFactory
import kotlinx.coroutines.delay

class RefreshDataWorker(
    val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    private val coinInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fromSymbols = CoinInfoMapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fromSymbols)
                val coinInfoDtoList = CoinInfoMapper.mapJsonToCoinInfo(jsonContainer)
                val coinInfoDbList = CoinInfoMapper.mapListDtoToDb(coinInfoDtoList)
                coinInfoDao.insertPriceList(coinInfoDbList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }

    companion object {
        const val WORK_NAME = "work name"
        fun makeRequest(): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<RefreshDataWorker>()
                .build()
    }
}