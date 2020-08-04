package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
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
import com.tfg.workoutagent.databinding.FragmentCreateExerciseBinding
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.CreateExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.CreateExerciseViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_exercise.*


/**
 * A simple [Fragment] subclass.
 */
class CreateExerciseFragment : BaseFragment() {

    companion object {
        private val PICK_MULTI_IMAGE_CODE = 10000
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateExerciseViewModelFactory(
                ManageExerciseUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(CreateExerciseViewModel::class.java)
    }

    private lateinit var binding: FragmentCreateExerciseBinding
    private var selectedPhotosUri: MutableList<Uri?> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_exercise,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
        setupButtonTags()
    }

    private fun setupUI() {
        upload_multiple_images_create_exercise.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_MULTI_IMAGE_CODE
            )
        }
        if(getDarkMode()){
            ll_arms.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_legs.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_back.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_chest.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_shoulder.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_gluteus.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_abs.setBackgroundResource(R.drawable.goal_item_border_dark)
            ll_cardio.setBackgroundResource(R.drawable.goal_item_border_dark)
        }else{
            ll_arms.setBackgroundResource(R.drawable.goal_item_border)
            ll_legs.setBackgroundResource(R.drawable.goal_item_border)
            ll_back.setBackgroundResource(R.drawable.goal_item_border)
            ll_chest.setBackgroundResource(R.drawable.goal_item_border)
            ll_shoulder.setBackgroundResource(R.drawable.goal_item_border)
            ll_gluteus.setBackgroundResource(R.drawable.goal_item_border)
            ll_abs.setBackgroundResource(R.drawable.goal_item_border)
            ll_cardio.setBackgroundResource(R.drawable.goal_item_border)
        }
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
                        val builder = AlertDialog.Builder(this.context)
                        builder.setTitle("Delete this image")
                        builder.setMessage(getString(R.string.alert_message_delete))

                        builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                            selectedPhotosUri.remove(photoUri)
                            ll_photos.removeView(it)
                            dialog.dismiss()
                        }

                        builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.create()
                        builder.show()
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
                    val builder = AlertDialog.Builder(this.context)
                    builder.setTitle("Delete this image")
                    builder.setMessage(getString(R.string.alert_message_delete))

                    builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                        selectedPhotosUri.remove(data.data)
                        ll_photos.removeView(it)
                        dialog.dismiss()
                    }

                    builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.create()
                    builder.show()
                }
            }
        }
    }

    private fun setupButtonTags() {
        ll_arms.setOnClickListener {
            val index = viewModel.tags.indexOf("Arms")
            if (index == -1) {
                viewModel.addTag("Arms")
                ll_arms.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_arms.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_arms.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_legs.setOnClickListener {
            val index = viewModel.tags.indexOf("Legs")
            if (index == -1) {
                viewModel.addTag("Legs")
                ll_legs.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_legs.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_legs.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_back.setOnClickListener {
            val index = viewModel.tags.indexOf("Back")
            if (index == -1) {
                viewModel.addTag("Back")
                ll_back.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_back.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_back.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_chest.setOnClickListener {
            val index = viewModel.tags.indexOf("Chest")
            if (index == -1) {
                viewModel.addTag("Chest")
                ll_chest.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_chest.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_chest.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_shoulder.setOnClickListener {
            val index = viewModel.tags.indexOf("Shoulder")
            if (index == -1) {
                viewModel.addTag("Shoulder")
                ll_shoulder.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_shoulder.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_shoulder.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_gluteus.setOnClickListener {
            val index = viewModel.tags.indexOf("Gluteus")
            if (index == -1) {
                viewModel.addTag("Gluteus")
                ll_gluteus.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_gluteus.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_gluteus.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_abs.setOnClickListener {
            val index = viewModel.tags.indexOf("Abs")
            if (index == -1) {
                viewModel.addTag("Abs")
                ll_abs.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_abs.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_abs.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
        ll_cardio.setOnClickListener {
            val index = viewModel.tags.indexOf("Cardio")
            if (index == -1) {
                viewModel.addTag("Cardio")
                ll_cardio.setBackgroundResource(R.drawable.item_border_green)
            } else {
                viewModel.removeTag(index)
                if(getDarkMode()){
                    ll_cardio.setBackgroundResource(R.drawable.goal_item_border_dark)
                }else{
                    ll_cardio.setBackgroundResource(R.drawable.goal_item_border)
                }
            }
        }
    }

    private fun observeData() {
        viewModel.titleError.observe(viewLifecycleOwner, Observer {
            binding.exerciseTitleInputEdit.error =
                if (it != "") it else null
        })

        viewModel.descriptionError.observe(viewLifecycleOwner, Observer {
            binding.exerciseDescriptionInputEdit.error =
                if (it != "") it else null
        })

        viewModel.tagsError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.addExerciseTagsErrorMessage.text = it
                    binding.addExerciseTagsErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.addExerciseTagsErrorMessage.text = ""
                    binding.addExerciseTagsErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.addExerciseTagsErrorMessage.text = ""
                binding.addExerciseTagsErrorMessage.visibility = View.GONE
            }
        })

        viewModel.photosError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.addExercisePhotoErrorMessage.text = it
                    binding.addExercisePhotoErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.addExercisePhotoErrorMessage.text = ""
                    binding.addExercisePhotoErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.addExercisePhotoErrorMessage.text = ""
                binding.addExercisePhotoErrorMessage.visibility = View.GONE
            }
        })

        viewModel.exerciseCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    true -> {
                        Toast.makeText(context, "Exercise created", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(CreateExerciseFragmentDirections.actionCreateExerciseFragmentToNavigationExercisesTrainer())
                    }
                    false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}
