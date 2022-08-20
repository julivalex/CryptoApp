package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.model.CoinInfoDb
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.domain.model.CoinInfoEntity
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object CoinInfoMapper {

    private fun mapDtoToDb(dto: CoinInfoDto): CoinInfoDb = with(dto) {
        CoinInfoDb(
            fromSymbol = fromSymbol,
            toSymbol = toSymbol,
            price = price,
            lowDay = lowDay,
            highDay = highDay,
            lastMarket = lastMarket,
            lastUpdate = lastUpdate,
            imageUrl = imageUrl
        )
    }

    fun mapListDtoToDb(list: List<CoinInfoDto>): List<CoinInfoDb> = list.map { mapDtoToDb(it) }

    fun mapDbToEntity(coinInfo: CoinInfoDb): CoinInfoEntity = with(coinInfo) {
        CoinInfoEntity(
            fromSymbol = fromSymbol,
            toSymbol = toSymbol,
            price = price,
            lowDay = lowDay,
            highDay = highDay,
            lastMarket = lastMarket,
            lastUpdate = convertTimestampToTime(lastUpdate),
            imageUrl = BASE_IMAGE_URL + imageUrl
        )
    }

    fun mapListDbToEntity(list: List<CoinInfoDb>): List<CoinInfoEntity> {
        return list.map { coinInfo -> mapDbToEntity(coinInfo) }
    }

    fun mapJsonToCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(coinNamesList: CoinNamesListDto): String {
        return coinNamesList.coinNames?.map {
            it.coinName?.name
        }?.joinToString(",").orEmpty()
    }


    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private const val BASE_IMAGE_URL = "https://cryptocompare.com"
}