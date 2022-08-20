package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.model.CoinInfoDb
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.domain.model.CoinInfoEntity
import com.google.gson.Gson

object CoinInfoMapper {

    fun mapDtoToDb(dto: CoinInfoDto): CoinInfoDb = with(dto) {
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
            lastUpdate = lastUpdate,
            imageUrl = imageUrl
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
        }?.joinToString().orEmpty()
    }
}