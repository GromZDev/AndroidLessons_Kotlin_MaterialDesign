package q3_kotlin.material_design.myPictureOfTheDay.view.notesFragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData


class NotesFragmentAdapter(
    private var onItemNoteClickListener:
    NotesFragment.OnItemNoteRecyclerViewClickListener?,
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    private var data: MutableList<NotesData> = mutableListOf()

    fun setNotesData(data: MutableList<NotesData>) {
        this.data = data
        notifyDataSetChanged() // Сетим первоначально список
    }

    // Сетим измененные данные онлайн с прокинутой позицией айтема
    fun setNewData(title: String, desc: String, position: Int) {
        data[position].title = title
        data[position].description = desc
        notifyItemChanged(position) // Сетим новые данные в конкретный айтем!
    }
    // ===========================================================

    fun appendItem() {
        data.add(newItem())
        notifyItemInserted(itemCount - 1) // С анимацией добавления
    }

    private fun newItem() = NotesData(R.drawable.fallout_6, "Добавлена заметка", "Заметка новая")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ONE -> SimpleViewHolder(
                inflater.inflate(R.layout.item_note_recycler_view, parent, false) as View
            )
            TYPE_TWO -> OtherViewHolder(
                inflater.inflate(R.layout.item_note_recycler_view2, parent, false) as View
            )
            else -> HeaderViewHolder(
                inflater.inflate(
                    R.layout.item_note_recycler_view3, parent,
                    false
                ) as View
            )

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].title.isBlank() -> TYPE_ONE
            else -> TYPE_TWO
        }
    }

    inner class SimpleViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem: NotesData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {

                itemView.findViewById<TextView>(R.id.item_note_name).text = dataItem.title
                itemView.findViewById<TextView>(R.id.item_note_description).text =
                    dataItem.description
                itemView.findViewById<ImageView>(R.id.item_note_image)
                    .setImageResource(dataItem.img)

                itemView.findViewById<ImageView>(R.id.delete_button_item).setOnClickListener {
                    removeItem()
                }

                itemView.findViewById<ImageView>(R.id.button_item_up).setOnClickListener {
                    moveItemUp()
                }

                itemView.findViewById<ImageView>(R.id.button_item_down).setOnClickListener {
                    moveItemDown()
                }

                itemView.findViewById<ImageView>(R.id.button_redact_note).setOnClickListener {
                    onItemNoteClickListener?.onItemViewClick(dataItem, layoutPosition)
                }
            }
        }

// ==== Имплементируем интерфейсы базового Вью Холдера для возможности свайпа и перетаскивания ====
        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundResource(R.drawable.item_note_recycler_background)
        }
// ================================================================================================

        private fun moveItemDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun moveItemUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition) // С анимацией удаления
        }
    }

    inner class OtherViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem: NotesData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.item_note_name).text = dataItem.title
                itemView.findViewById<TextView>(R.id.item_note_description).text =
                    dataItem.description
                itemView.findViewById<ImageView>(R.id.item_note_image)
                    .setImageResource(dataItem.img)

                itemView.findViewById<ImageView>(R.id.delete_button_item).setOnClickListener {
                    removeItem()
                }

                itemView.findViewById<ImageView>(R.id.button_item_up).setOnClickListener {
                    moveItemUp()
                }

                itemView.findViewById<ImageView>(R.id.button_item_down).setOnClickListener {
                    moveItemDown()
                }

                itemView.findViewById<ImageView>(R.id.button_redact_note).setOnClickListener {

                    onItemNoteClickListener?.onItemViewClick(dataItem, layoutPosition)
                }

            }
        }

// ==== Имплементируем интерфейсы базового Вью Холдера для возможности свайпа и перетаскивания ====
        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        @SuppressLint("ResourceAsColor")
        override fun onItemClear() {
            itemView.setBackgroundResource(R.color.teal_700)
        }
// ================================================================================================

        private fun moveItemDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun moveItemUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition) // С анимацией удаления
        }

    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(dataItem: NotesData) {
            itemView.findViewById<TextView>(R.id.item_note_name).text = dataItem.title

        }

// ==== Имплементируем интерфейсы базового Вью Холдера для возможности свайпа и перетаскивания ====
        override fun onItemSelected() {
        }
        override fun onItemClear() {
        }
// ================================================================================================
    }

    companion object {
        private const val TYPE_ONE = 0
        private const val TYPE_TWO = 1
        private const val TYPE_HEADER = 2
    }

    fun removeListener() {
        onItemNoteClickListener = null
    }

// ========== Имплементируем интерфейсы Адаптера для возможности свайпа и перетаскивания ==========
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(
                if (toPosition > fromPosition) toPosition - 1 else
                    toPosition, this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
// ================================================================================================

}
