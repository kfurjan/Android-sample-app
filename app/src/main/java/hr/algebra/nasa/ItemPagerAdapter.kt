package hr.algebra.nasa

import android.content.ContentValues
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasa.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(private var items: MutableList<Item>) :
    RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem: ImageView = itemView.findViewById(R.id.ivItem)
        private val ivRead: ImageView = itemView.findViewById(R.id.ivRead)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvExplanation: TextView = itemView.findViewById(R.id.tvExplanation)

        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.flat_about)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)

            ivRead.setImageResource(if (item.read) R.drawable.green_flag else R.drawable.red_flag)
            ivRead.setOnClickListener {
                item.read = !item.read
                itemView.context.contentResolver.update(
                    Uri.withAppendedPath(NASA_PROVIDER_CONTENT_URI, "/${item._id}"),
                    ContentValues().apply { put(Item::read.name, item.read) },
                    null,
                    arrayOf(item._id.toString())
                )
                notifyDataSetChanged()
            }
            tvDate.text = item.date
            tvTitle.text = item.title
            tvExplanation.text = item.explanation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pager, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}
