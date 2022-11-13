package com.example.cryptoapp.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinInfoMapper
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay

class RefreshDataWorker(
    val context: Context,
    private val workerParameters: WorkerParameters,
    private val coinInfoDao: CoinInfoDao,
    private val apiService: ApiService,
    private val mapper: CoinInfoMapper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fromSymbols = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fromSymbols)
                val coinInfoDtoList = mapper.mapJsonToCoinInfo(jsonContainer)
                val coinInfoDbList = mapper.mapListDtoToDb(coinInfoDtoList)
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