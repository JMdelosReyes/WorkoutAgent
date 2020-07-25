package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_edit_delete_exercise.*

/**
 * A simple [Fragment] subclass.
 */
class EditDeleteExerciseFragment : BaseFragment() {
    companion object{
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
    private var selectedPhotosUri : MutableList<Uri?> = mutableListOf()

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
        if(requestCode == PICK_MULTI_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            if(data.clipData != null){
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
            } else if(data.data != null){
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    fun setupUI(){
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
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_exercise))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                dialog.dismiss()
                viewModel.onDelete()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }

    private fun setPhotos(photos : MutableList<String>){
        for (photo in photos){
            val image = ImageView(this.context)
            image.id = Math.random().toInt()
            Glide.with(this).asBitmap().load(photo).into(image)
            image.maxWidth = 150
            ll_photos.addView(image)
            image.setOnClickListener {
                val builder = AlertDialog.Builder(this.context)
                builder.setTitle("Delete this image")
                builder.setMessage(getString(R.string.alert_message_delete))
                builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
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

    private fun setTags(tags : MutableList<String>){
        val darkMode = getDarkMode()
        if(tags.contains("Arms")) ll_arms.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_arms.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_arms.setBackgroundResource(R.drawable.item_white_dark_border)
        }
        if(tags.contains("Legs")) ll_legs.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_legs.setBackgroundResource(R.drawable.item_border_dark)
        }else {
            ll_legs.setBackgroundResource(R.drawable.item_white_dark_border)
        }
        if(tags.contains("Back")) ll_back.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_back.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_back.setBackgroundResource(R.drawable.item_white_dark_border)
        }
        if(tags.contains("Chest")) ll_chest.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_chest.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_chest.setBackgroundResource(R.drawable.item_white_dark_border)
        }
        if(tags.contains("Shoulder")) ll_shoulder.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_shoulder.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_shoulder.setBackgroundColor(Color.WHITE)
        }
        if(tags.contains("Gluteus")) ll_gluteus.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_gluteus.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_gluteus.setBackgroundColor(Color.WHITE)
        }
        if(tags.contains("Abs")) ll_abs.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_abs.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_abs.setBackgroundColor(Color.WHITE)
        }
        if(tags.contains("Cardio")) ll_cardio.setBackgroundResource(R.drawable.item_border_primary_color) else if(darkMode){
            ll_cardio.setBackgroundResource(R.drawable.item_border_dark)
        }else{
            ll_cardio.setBackgroundColor(Color.WHITE)
        }
        button_ll_arms.setOnClickListener {
            val index = tags.indexOf("Arms")
            if(index == -1){
                viewModel.addTag("Arms")
                ll_arms.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_arms.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_arms.setBackgroundResource(R.drawable.item_white_dark_border)
                }
            }
        }
        button_ll_legs.setOnClickListener {
            val index = tags.indexOf("Legs")
            if(index == -1){
                viewModel.addTag("Legs")
                ll_legs.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_legs.setBackgroundResource(R.drawable.item_border_dark)
                }else {
                    ll_legs.setBackgroundResource(R.drawable.item_white_dark_border)
                }
            }
        }
        button_ll_back.setOnClickListener {
            val index = tags.indexOf("Back")
            if(index == -1){
                viewModel.addTag("Back")
                ll_back.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_back.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_back.setBackgroundResource(R.drawable.item_white_dark_border)
                }
            }
        }
        button_ll_chest.setOnClickListener {
            val index = tags.indexOf("Chest")
            if(index == -1){
                viewModel.addTag("Chest")
                ll_chest.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_chest.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_chest.setBackgroundResource(R.drawable.item_white_dark_border)
                }
            }
        }
        button_ll_shoulder.setOnClickListener {
            val index = tags.indexOf("Shoulder")
            if(index == -1){
                viewModel.addTag("Shoulder")
                ll_shoulder.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_shoulder.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_shoulder.setBackgroundColor(Color.WHITE)
                }
            }
        }
        button_ll_gluteus.setOnClickListener {
            val index = tags.indexOf("Gluteus")
            if(index == -1){
                viewModel.addTag("Gluteus")
                ll_gluteus.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_gluteus.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_gluteus.setBackgroundColor(Color.WHITE)
                }
            }
        }
        button_ll_abs.setOnClickListener {
            val index = tags.indexOf("Abs")
            if(index == -1){
                viewModel.addTag("Abs")
                ll_abs.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_abs.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_abs.setBackgroundColor(Color.WHITE)
                }
            }
        }
        button_ll_cardio.setOnClickListener {
            val index = tags.indexOf("Cardio")
            if(index == -1){
                viewModel.addTag("Cardio")
                ll_cardio.setBackgroundResource(R.drawable.item_border_primary_color)
            }else{
                viewModel.removeTag(index)
                if(darkMode){
                    ll_cardio.setBackgroundResource(R.drawable.item_border_dark)
                }else{
                    ll_cardio.setBackgroundColor(Color.WHITE)
                }
            }
        }
        viewModel.tags.value?.clear()
    }


    private fun observeData() {
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
                    Toast.makeText(context, "Exercise deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditDeleteExerciseFragmentDirections.actionEditDeleteExerciseFragmentToNavigationExercisesTrainer())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.exerciseSaved.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "Exercise updated", Toast.LENGTH_SHORT).show()
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
