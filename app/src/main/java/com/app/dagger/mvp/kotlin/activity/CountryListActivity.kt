package com.app.dagger.mvp.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.app.dagger.mvp.kotlin.R
import com.app.dagger.mvp.kotlin.adapter.CountryListAdapter
import com.app.dagger.mvp.kotlin.component.DaggerCountryListActivityComponent
import com.app.dagger.mvp.kotlin.interfaces.CountryListView
import com.app.dagger.mvp.kotlin.model.CountryModel
import com.app.dagger.mvp.kotlin.module.CountryListActivityModule
import com.app.dagger.mvp.kotlin.presenter.CountryListPresenter
import kotlinx.android.synthetic.main.activity_country_list.*
import retrofit2.Response

class CountryListActivity : AppCompatActivity(), CountryListView {

    var countryListAdapter: CountryListAdapter? = null
    lateinit var countryListPresenter: CountryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        var countryListComponent = DaggerCountryListActivityComponent.builder()
                .countryListActivityModule(CountryListActivityModule(this, this))
                .build()
        countryListAdapter = countryListComponent.getCountryListAdapter()
        countryListPresenter = countryListComponent.getCountryPresenter()

        initViews()

    }

    /**
     * Method to init views
     */
    private fun initViews() {
        rvCountryData.layoutManager = LinearLayoutManager(this)
        rvCountryData.adapter = countryListAdapter
        countryListPresenter.getCountryData()

    }

    override fun showProgressDialog() {
        if (!progressbar.isShown) {
            progressbar.visibility = View.VISIBLE

        }
    }

    override fun dismissProgressDialog() {
        progressbar.visibility = View.GONE
    }


    override fun onSuccess(response: Response<List<CountryModel>>) {
        dismissProgressDialog()
        if (rvCountryData != null && countryListAdapter != null && response.body() != null) {
            countryListAdapter?.setItems(response.body()!!)
        }

    }

    override fun onFailure() {
        Toast.makeText(this, getString(R.string.network_msg), Toast.LENGTH_SHORT).show()
        dismissProgressDialog()
    }

}
