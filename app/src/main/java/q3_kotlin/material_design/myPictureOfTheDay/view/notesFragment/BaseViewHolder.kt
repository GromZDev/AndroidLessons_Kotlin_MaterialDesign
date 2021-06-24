package q3_kotlin.material_design.myPictureOfTheDay.view.notesFragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    ItemTouchHelperViewHolder {
    abstract fun bind(dataItem: NotesData)



}