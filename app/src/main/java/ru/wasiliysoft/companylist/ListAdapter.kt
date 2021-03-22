package ru.wasiliysoft.companylist.model

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.wasiliysoft.companylist.R

fun interface OnClickItem {
    fun onClickItem(item: CompanyItem)
}

class ListAdapter : RecyclerView.Adapter<ListAdapter.VH>() {
    var callback: OnClickItem? = null
    var list: List<CompanyItem> = emptyList()
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
        holder.imageView.setOnClickListener {
            callback?.onClickItem(list[position])
        }
    }

    override fun getItemCount() = list.size


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.title)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        fun bind(item: CompanyItem) {
            textView.text = item.name
            Glide.with(itemView.context).load("https://lifehack.studio/test_task/${item.img}")
                .override(600, 400) // resizes the image to these dimensions (in pixel)
                .centerInside()
                .into(imageView)
        }
    }
}