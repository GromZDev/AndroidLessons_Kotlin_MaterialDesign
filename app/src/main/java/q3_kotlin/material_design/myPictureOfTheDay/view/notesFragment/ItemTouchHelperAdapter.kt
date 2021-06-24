package q3_kotlin.material_design.myPictureOfTheDay.view.notesFragment

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}
