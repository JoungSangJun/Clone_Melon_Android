package com.example.melonactivity

import retrofit2.Call
import retrofit2.http.GET
import java.io.Serializable

//서버에서 받아올 데이터 형태, intent로 데이터 전달해야함으로 Serializable 구현
class MelonItem(
    val id: Int, val title: String, val song: String, val thumbnail: String
) : Serializable

//사용할 HTTP 메소드
interface RetrofitService {
    @GET("melon/list/")
    fun getMelonItemList(): Call<ArrayList<MelonItem>>
}