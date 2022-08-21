package com.example.cryptoapp.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.viewmodel.CoinViewModel
import com.example.cryptoapp.databinding.ActivityCoinPriceListBinding
import com.example.cryptoapp.presentation.adapter.CoinInfoAdapter
import com.example.cryptoapp.domain.model.CoinInfoEntity
import com.example.cryptoapp.presentation.fragment.CoinDetailFragment

class CoinPriceListActivity : AppCompatActivity() {

    private val viewModel by viewModels<CoinViewModel>()

    private val binding by lazy { ActivityCoinPriceListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val adapter = CoinInfoAdapter(context = this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinInfoEntity) {
                if (isOnePane()) {
                    launchDetailActivity(coinPriceInfo.fromSymbol)
                } else {
                    if(savedInstanceState == null) {
                        launchDetailFragment(coinPriceInfo.fromSymbol)
                    }
                }
            }
        }
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }

    fun isOnePane() = binding.fragmentContainer == null

    fun launchDetailActivity(fromSymbol: String) {
        val intent = CoinDetailActivity.newIntent(
            context = this,
            fromSymbol = fromSymbol
        )
        startActivity(intent)
    }

    fun launchDetailFragment(fromSymbol: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CoinDetailFragment.newInstance(fromSymbol))
            .addToBackStack(null)
            .commit()
    }
}
