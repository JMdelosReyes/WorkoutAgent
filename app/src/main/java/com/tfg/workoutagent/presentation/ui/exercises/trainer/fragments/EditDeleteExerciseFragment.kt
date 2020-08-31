package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDeleteExerciseBinding
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.EditDeleteExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.EditDeleteExerciseViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import kotlinx.android.synthetic.main.fragment_edit_delete_exercise.*

/**
 * A simple [Fragment] subclass.
 */
class EditDeleteExerciseFragment : BaseFragment() {
    companion object {
        private val PICK_MULTI_IMAGE_CODE = 10001
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, EditDeleteExerciseViewModelFactory(
                exerciseId,
                ManageExerciseUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(EditDeleteExerciseViewModel::class.java)
    }

    private val exerciseId by lazy { EditDeleteExerciseFragmentArgs.fromBundle(arguments!!).exerciseId }
    // private val exerciseTitle by lazy { EditDeleteExerciseFragmentArgs.fromBundle(arguments!!).exerciseTitle }

    private lateinit var binding: FragmentEditDeleteExerciseBinding
    private var selectedPhotosUri: MutableList<Uri?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_delete_exercise,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_MULTI_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.clipData != null) {
                selectedPhotosUri.clear()
                if ((ll_photos as LinearLayout).childCount > 0) (ll_photos as LinearLayout).removeAllViews()
                for (index in 0 until data.clipData!!.itemCount) run {
                    val photoUri: Uri = data.clipData!!.getItemAt(index).uri
                    selectedPhotosUri.add(photoUri)
                    viewModel.dataPhoto = data
                    val image = ImageView(this.context)
                    image.id = Math.random().toInt()
                    Glide.with(this).asBitmap().load(photoUri).into(image)
                    image.maxWidth = 150
                    ll_photos.addView(image)
                    image.setOnClickListener {
                        createAlertDialog(
                            context = this.context!!,
                            title = "Delete this image",
                            message = getString(R.string.alert_message_delete),
                            positiveAction = {
                                selectedPhotosUri.remove(photoUri)
                                ll_photos.removeView(it)
                            },
                            negativeAction = {},
                            positiveText = getString(R.string.answer_yes),
                            negativeText = getString(R.string.answer_no)
                        )
                    }
                }
            } else if (data.data != null) {
                selectedPhotosUri.clear()
                if ((ll_photos as LinearLayout).childCount > 0) (ll_photos as LinearLayout).removeAllViews()
                selectedPhotosUri.add(data.data)
                viewModel.dataPhoto = data
                val image = ImageView(this.context)
                Glide.with(this).asBitmap().load(selectedPhotosUri[0]).into(image)
                image.maxWidth = 150
                ll_photos.addView(image)
                image.setOnClickListener {
                    createAlertDialog(
                        context = this.context!!,
                        title = "Delete this image",
                        message = getString(R.string.alert_message_delete),
                        positiveAction = {
                            selectedPhotosUri.remove(data.data)
                            ll_photos.removeView(it)
                        },
                        negativeAction = {},
                        positiveText = getString(R.string.answer_yes),
                        negativeText = getString(R.string.answer_no)
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    fun setupUI() {
        upload_multiple_images_edit_exercise.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_MULTI_IMAGE_CODE
            )
        }
        delete_exercise_button.setOnClickListener {
            createAlertDialog(
                context = this.context!!,
                title = getString(R.string.alert_title_delete_exercise),
                message = getString(R.string.alert_message_delete),
                positiveAction = { viewModel.onDelete() },
                negativeAction = {},
                positiveText = getString(R.string.answer_yes),
                negativeText = getString(R.string.answer_no)
            )
        }
    }

    private fun setPhotos(photos: MutableList<String>) {
        photos.forEachIndexed { index, photo ->
            val image = ImageView(this.context)
            image.id = Math.random().toInt()
            Glide.with(this).asBitmap().load(photo).into(image)
            image.maxWidth = 150
            ll_photos.addView(image)
            image.setOnClickListener {
                createAlertDialog(
                    context = this.context!!,
                    title = "Delete this image",
                    message = getString(R.string.alert_message_delete),
                    positiveAction = {
                        ll_photos.removeView(it)
                        viewModel.removePhoto(photo)
                    },
                    negativeAction = {},
                    positiveText = getString(R.string.answer_yes),
                    negativeText = getString(R.string.answer_no)
                )
            }
        }
    }

    private fun setTags(tags: MutableList<String>) {
        if (tags.contains("Arms")) ll_arms.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_arms.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_arms.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Legs")) ll_legs.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_legs.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_legs.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Back")) ll_back.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_back.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_back.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Chest")) ll_chest.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_chest.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_chest.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Shoulder")) ll_shoulder.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_shoulder.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_shoulder.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Gluteus")) ll_gluteus.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_gluteus.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_gluteus.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Abs")) ll_abs.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_abs.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_abs.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        if (tags.contains("Cardio")) ll_cardio.setBackgroundResource(R.drawable.item_border_selected) else {
            if (getDarkMode()) {
                ll_cardio.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                ll_cardio.setBackgroundResource(R.drawable.item_border_unselected)
            }
        }
        ll_arms.setOnClickListener {
            val index = tags.indexOf("Arms")
            if (index == -1) {
                viewModel.addTag("Arms")
                ll_arms.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_arms.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_arms.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_legs.setOnClickListener {
            val index = tags.indexOf("Legs")
            if (index == -1) {
                viewModel.addTag("Legs")
                ll_legs.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_legs.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_legs.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_back.setOnClickListener {
            val index = tags.indexOf("Back")
            if (index == -1) {
                viewModel.addTag("Back")
                ll_back.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_back.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_back.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_chest.setOnClickListener {
            val index = tags.indexOf("Chest")
            if (index == -1) {
                viewModel.addTag("Chest")
                ll_chest.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_chest.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_chest.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_shoulder.setOnClickListener {
            val index = tags.indexOf("Shoulder")
            if (index == -1) {
                viewModel.addTag("Shoulder")
                ll_shoulder.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_shoulder.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_shoulder.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_gluteus.setOnClickListener {
            val index = tags.indexOf("Gluteus")
            if (index == -1) {
                viewModel.addTag("Gluteus")
                ll_gluteus.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_gluteus.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_gluteus.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_abs.setOnClickListener {
            val index = tags.indexOf("Abs")
            if (index == -1) {
                viewModel.addTag("Abs")
                ll_abs.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_abs.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_abs.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        ll_cardio.setOnClickListener {
            val index = tags.indexOf("Cardio")
            if (index == -1) {
                viewModel.addTag("Cardio")
                ll_cardio.setBackgroundResource(R.drawable.item_border_selected)
            } else {
                viewModel.removeTag(index)
                if (getDarkMode()) {
                    ll_cardio.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    ll_cardio.setBackgroundResource(R.drawable.item_border_unselected)
                }
            }
        }
        viewModel.tags.value?.clear()
    }


    private fun observeData() {
        viewModel.tagsError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.editExerciseTagsErrorMessage.text = it
                    binding.editExerciseTagsErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.editExerciseTagsErrorMessage.text = ""
                    binding.editExerciseTagsErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.editExerciseTagsErrorMessage.text = ""
                binding.editExerciseTagsErrorMessage.visibility = View.GONE
            }
        })

        viewModel.photosError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.editExercisePhotoErrorMessage.text = it
                    binding.editExercisePhotoErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.editExercisePhotoErrorMessage.text = ""
                    binding.editExercisePhotoErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.editExercisePhotoErrorMessage.text = ""
                binding.editExercisePhotoErrorMessage.visibility = View.GONE
            }
        })

        viewModel.getExercise.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO

                }
                is Resource.Success -> {
                    // TODO
                    setPhotos(it.data.photos)
                    setTags(it.data.tags)
                }
                is Resource.Failure -> {
                    // TODO
                }
            }
        })

        viewModel.exerciseDeleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    findNavController().navigate(EditDeleteExerciseFragmentDirections.actionEditDeleteExerciseFragmentToNavigationExercisesTrainer())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.exerciseSaved.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    findNavController().navigate(
                        EditDeleteExerciseFragmentDirections.actionEditDeleteExerciseFragmentToDisplayExercise(
                            exerciseId, viewModel.title.value!!
                        )
                    )
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.titleError.observe(viewLifecycleOwner, Observer {
            binding.exerciseTitleInputEdit.error =
                if (it != "") it else null
        })

        viewModel.descriptionError.observe(viewLifecycleOwner, Observer {
            binding.exerciseDescriptionInputEdit.error =
                if (it != "") it else null
        })

    }
}
