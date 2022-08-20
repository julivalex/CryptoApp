package com.example.cryptoapp.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.presentation.viewmodel.CoinViewModel
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.adapter.CoinInfoAdapter
import com.example.cryptoapp.domain.model.CoinInfoEntity
import kotlinx.android.synthetic.main.activity_coin_prce_list.rvCoinPriceList

class CoinPriceListActivity : AppCompatActivity() {

    private val viewModel by viewModels<CoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_prce_list)
        val adapter = CoinInfoAdapter(context = this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinInfoEntity) {
                val intent = CoinDetailActivity.newIntent(
                    context = this@CoinPriceListActivity,
                    fromSymbol = coinPriceInfo.fromSymbol.orEmpty()
                )
                startActivity(intent)
            }
        }
        rvCoinPriceList.adapter = adapter
        viewModel.coinInfoList.observe(this) {
            adapter.coinInfoList = it
        }
    }
}
