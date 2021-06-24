package q3_kotlin.material_design.myPictureOfTheDay.view.notesFragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentNotesBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.NotesAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.NotesViewModel

class NotesFragment : Fragment(R.layout.fragment_notes) {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val notesViewModel: NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    interface OnItemNoteRecyclerViewClickListener {
        fun onItemViewClick(note: NotesData, position: Int)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val adapter = NotesFragmentAdapter(object : OnItemNoteRecyclerViewClickListener {
        override fun onItemViewClick(note: NotesData, position: Int) {
            val manager = requireActivity().supportFragmentManager
            // Если manager не null...(let)
            manager.let {

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.includedBottomSheetLayoutNotes.bottomSheetDescriptionHeaderNote.setText(note.title)
                binding.includedBottomSheetLayoutNotes.bottomSheetDescriptionNote.setText(note.description)

                binding.includedBottomSheetLayoutNotes.bottomSheetNoteButtonSave.setOnClickListener {
                    val newTitle = binding.includedBottomSheetLayoutNotes.bottomSheetDescriptionHeaderNote.text.toString()
                    val newDescription = binding.includedBottomSheetLayoutNotes.bottomSheetDescriptionNote.text.toString()

                    notesViewModel.getLiveData().observe(viewLifecycleOwner, {
                        setNewData(newTitle, newDescription, position).also {
                            closeBottomSheet()
                        } }
                    )
                    toast("Заметка на позиции $position обновлена")
                }

                Toast.makeText(context, note.title, Toast.LENGTH_SHORT).show()
            }
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        setBottomSheetBehavior(binding.includedBottomSheetLayoutNotes.bottomSheetContainer)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesFragmentRecyclerView.adapter = adapter

        val notesRecyclerView: RecyclerView = binding.notesFragmentRecyclerView

        notesRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.notesFragmentFAB.setOnClickListener { adapter.appendItem() }

// =========== Сетим ItemTouchHelper в наш ресайклер для смахивания и таскания ============
        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.notesFragmentRecyclerView)
// ========================================================================================

        notesViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        notesViewModel.getDataFromLocalSource()
    }

// ================   Сетим измененные данные онлайн с прокинутой позицией айтема  ========
    private fun setNewData(title: String, desc: String, position: Int) {
                adapter.setNewData(title, desc, position)
    }

    private fun closeBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
// ========================================================================================

    private fun renderData(appState: NotesAppState) {
        when (appState) {
            is NotesAppState.Success -> {

                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.notesFragmentRecyclerView.visibility = View.VISIBLE

                adapter.setNotesData(appState.serverResponseData as MutableList<NotesData>)

            }
            is NotesAppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                binding.notesFragmentRecyclerView.visibility = View.GONE
            }
            is NotesAppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                toast("Error in loading...")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeListener()
    }

    companion object {
        fun newInstance() = NotesFragment()
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // Изначально положение скрыто!

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

    }

}


