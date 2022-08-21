package com.example.cryptoapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCoinInfoBinding
import com.example.cryptoapp.domain.model.CoinInfoEntity
import com.example.cryptoapp.presentation.CoinInfoDiffCallback
import com.example.cryptoapp.presentation.holder.CoinInfoViewHolder
import com.squareup.picasso.Picasso

class CoinInfoAdapter(
    private val context: Context
) : ListAdapter<CoinInfoEntity, CoinInfoViewHolder>(
    AsyncDifferConfig.Builder(CoinInfoDiffCallback()).build()
) {

    private lateinit var binding: ItemCoinInfoBinding

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        binding = ItemCoinInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        getItem(position).apply {
            val symbolsTemplate = context.resources.getString(R.string.symbols_template)
            val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
            holder.binding.tvSymbols.text =
                String.format(symbolsTemplate, fromSymbol, toSymbol)
            holder.binding.tvPrice.text = price
            holder.binding.tvLastUpdate.text = String.format(lastUpdateTemplate, lastUpdate)
            Picasso.get().load(imageUrl).into(holder.binding.ivLogoCoin)
            holder.binding.root.setOnClickListener {
                onCoinClickListener?.onCoinClick(this)
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinInfoEntity)
    }
}