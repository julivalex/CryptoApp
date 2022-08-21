package com.example.cryptoapp.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.presentation.viewmodel.CoinViewModel
import com.example.cryptoapp.databinding.ActivityCoinPriceListBinding
import com.example.cryptoapp.presentation.adapter.CoinInfoAdapter
import com.example.cryptoapp.domain.model.CoinInfoEntity

class CoinPriceListActivity : AppCompatActivity() {

    private val viewModel by viewModels<CoinViewModel>()

    private val binding by lazy { ActivityCoinPriceListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        binding.rvCoinPriceList.adapter = adapter
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }
}
