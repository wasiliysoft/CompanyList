package ru.wasiliysoft.companylist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import retrofit2.Response
import ru.wasiliysoft.companylist.model.CompanyItem
import java.lang.Exception

class DetailActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        launch(Dispatchers.IO) {
            val q = ServerAPI.getInstance().getCompanyFromId(getArgument())
            Log.d("dfrewf", q.request().toString())
            var resp: Response<List<CompanyItem>>? = null
            try {
                resp = q.execute()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "Сетевая ошибка", Toast.LENGTH_SHORT).show()
                }
            }

            if (resp == null) {
                finish()
                return@launch
            }
            if (resp.isSuccessful) {
                resp.body()?.first()?.let { company ->
                    val imageView = findViewById<ImageView>(R.id.imageView)
                    val nameTv = findViewById<TextView>(R.id.nameTv)
                    val descriptionTv = findViewById<TextView>(R.id.descriptionTv)
                    val wwwTv = findViewById<TextView>(R.id.wwwTv)
                    val latTv = findViewById<TextView>(R.id.latTv)
                    val lonTv = findViewById<TextView>(R.id.lonTv)
                    val phoneTv = findViewById<TextView>(R.id.phoneTv)
                    val idTv = findViewById<TextView>(R.id.idTv)
                    withContext(Dispatchers.Main) {
                        //TODO Стрпоки можно перенести в ресурсы, для дальнейшей локализации
                        nameTv.text = company.name
                        descriptionTv.text = company.description
                        wwwTv.text = "Сайт: ${company.www}"
                        phoneTv.text = "Телефон: ${company.phone}"
                        idTv.text = "Ид компании: ${company.id}"
                        latTv.text = "LAT: ${company.lat}"
                        lonTv.text = "LON: ${company.lon}"
                        Glide.with(imageView.context)
                            .load("https://lifehack.studio/test_task/${company.img}")
                            .override(800, 600) // resizes the image to these dimensions (in pixel)
                            .centerInside()
                            .into(imageView)
                    }
                }
            }
        }
    }

    private fun getArgument() = intent.extras?.getString(EXTRA_COMPANY_ID) ?: "-1"


    companion object {
        private const val EXTRA_COMPANY_ID = "EXTRA_COMPANY_ID"


        fun newInstance(ctx: Context, companyId: String) {
            val i = Intent(ctx, DetailActivity::class.java).apply {
                putExtra(EXTRA_COMPANY_ID, companyId)
            }
            ctx.startActivity(i)
        }
    }
}