package com.example.cryptoapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.cryptoapp.domain.model.CoinInfoEntity

class CoinInfoDiffCallback: DiffUtil.ItemCallback<CoinInfoEntity>() {

    override fun areItemsTheSame(oldItem: CoinInfoEntity, newItem: CoinInfoEntity): Boolean {
        return oldItem.fromSymbol == newItem.fromSymbol
    }

    override fun areContentsTheSame(oldItem: CoinInfoEntity, newItem: CoinInfoEntity): Boolean {
        return oldItem == newItem
    }
}