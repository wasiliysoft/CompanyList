package ru.wasiliysoft.companylist

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.wasiliysoft.companylist.model.CompanyItem

class ServerAPI {
    interface API {
        @GET("test.php")
        fun getCompanyList(): Call<List<CompanyItem>>

        @GET("test.php")
        fun getCompanyFromId(@Query("id") id: String): Call<List<CompanyItem>>
    }

    companion object {
        private var INSTANCE: API? = null
        fun getInstance(): API {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl("https://lifehack.studio/test_task/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build()
                    .create(API::class.java)
            }
            return INSTANCE!!
        }
    }
}