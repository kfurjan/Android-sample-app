package hr.algebra.nasa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.nasa.framework.fetchItems
import hr.algebra.nasa.model.Item
import kotlinx.android.synthetic.main.activity_item_pager.*

const val ITEM_POSITION = "hr.algebra.nasa.item_position"

class ItemPagerActivity : AppCompatActivity() {

    private lateinit var items: MutableList<Item>
    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_pager)

        init()
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // show back button
    }

    private fun init() {
        items = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        viewPager.adapter = ItemPagerAdapter(items)
        viewPager.currentItem = itemPosition
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // simulate back button
        return super.onSupportNavigateUp()
    }
}
