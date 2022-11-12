package com.example.cryptoapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.model.CoinInfoEntity
import com.example.cryptoapp.domain.usecase.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.usecase.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecase.LoadDataUseCase
import javax.inject.Inject

class CoinViewModel @Inject constructor(
    private val getCoinInfoListUseCase: GetCoinInfoListUseCase,
    private val getCoinInfoUseCase: GetCoinInfoUseCase,
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String): LiveData<CoinInfoEntity> {
        return getCoinInfoUseCase(fSym)
    }

    init {
        loadDataUseCase()
    }
}