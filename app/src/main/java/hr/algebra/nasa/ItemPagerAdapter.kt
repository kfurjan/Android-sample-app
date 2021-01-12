package hr.algebra.nasa

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasa.framework.fetchItems
import hr.algebra.nasa.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(private val items: MutableList<Item>, private val context: Context) :
    RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem: ImageView = itemView.findViewById(R.id.ivItem)
        val ivRead: ImageView = itemView.findViewById(R.id.ivRead)
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
            tvDate.text = item.date
            tvTitle.text = item.title
            tvExplanation.text = item.explanation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pager, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!items[position].read) {
            holder.ivRead.setOnClickListener {
                context.contentResolver.update(
                    Uri.withAppendedPath(NASA_PROVIDER_CONTENT_URI, "/${items[position]._id}"),
                    ContentValues().apply { put(Item::read.name, true) },
                    null,
                    arrayOf(items[position]._id.toString())
                )

                holder.bind(context.fetchItems()[position])
            }
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
