package ru.wasiliysoft.companylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import ru.wasiliysoft.companylist.model.CompanyItem
import ru.wasiliysoft.companylist.model.ListAdapter
import ru.wasiliysoft.companylist.model.OnClickItem

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), OnClickItem {
    companion object {
        const val LOG_TAG = "MainActivity_TAG"
    }

    private val adapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter.callback = this

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        launch(Dispatchers.IO) {
            val resp = ServerAPI.getInstance().getCompanyList().execute()
            if (resp.isSuccessful) {
                Log.d(LOG_TAG, "Company list size ${resp.body()?.size}")
                withContext(Dispatchers.Main) {
                    adapter.list = resp.body() ?: emptyList()
                }
            } else {
                Log.e(LOG_TAG, "resp FAILED, code ${resp.code()}")
            }
        }

    }

    override fun onClickItem(item: CompanyItem) {
        DetailActivity.newInstance(this, item.id)
    }
}