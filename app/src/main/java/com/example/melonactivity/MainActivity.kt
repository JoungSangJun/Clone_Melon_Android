package com.example.melonactivity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //retrofit라이브러리로 서버와 통신
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMelonItemList().enqueue(object : Callback<ArrayList<MelonItem>> {
            //성공적으로 데이터 받아옴
            override fun onResponse(
                call: Call<ArrayList<MelonItem>>,
                response: Response<ArrayList<MelonItem>>
            ) {
                val melonItemList = response.body()
                //recyclerview 에 adapter 장착
                findViewById<RecyclerView>(R.id.melon_list_view).apply {
                    this.adapter = MelonItemRecyclerAdapter(
                        melonItemList!!,
                        LayoutInflater.from(this@MainActivity),
                        Glide.with(this@MainActivity),
                        this@MainActivity
                    )
                }
            }

            //데이터 가져오기 실패
            override fun onFailure(call: Call<ArrayList<MelonItem>>, t: Throwable) {
                Log.d("melonn", "fail")
            }

        })
    }
}

//RecyclerView 어뎁터
class MelonItemRecyclerAdapter(
    val melonItemList: ArrayList<MelonItem>,
    val inflater: LayoutInflater,
    val glide: RequestManager,
    val context: Context
) : RecyclerView.Adapter<MelonItemRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val play: ImageView
        val thumbnail: ImageView

        init {
            title = itemView.findViewById(R.id.title)
            thumbnail = itemView.findViewById(R.id.thumbnail)
            play = itemView.findViewById(R.id.play)

            //play버튼 누르면 melonItemList와 누른 위치 데이터를 넘겨줌
            play.setOnClickListener {
                val intent = Intent(context, MelonDetailActivity::class.java)
                intent.putExtra("melonItemList", melonItemList)
                intent.putExtra("position", adapterPosition)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.melon_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = melonItemList[position].title
        glide.load(melonItemList[position].thumbnail).centerCrop().into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return melonItemList.size
    }
}













