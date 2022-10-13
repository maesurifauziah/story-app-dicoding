package com.test.storyappsubmission1.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.test.storyappsubmission1.R
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.databinding.ActivityAddNewStoryBinding
import com.test.storyappsubmission1.ui.ViewModelFactory
import com.test.storyappsubmission1.ui.main.MainActivity
import com.test.storyappsubmission1.ui.main.MainViewModel
import com.test.storyappsubmission1.ui.signin.SigninActivity
import com.test.storyappsubmission1.ui.signin.SigninViewModel
import com.test.storyappsubmission1.utils.rotateBitmap
import com.test.storyappsubmission1.utils.uriToFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")
class AddNewStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewStoryBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var signViewModel: SigninViewModel

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)


        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[MainViewModel::class.java]

        signViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[SigninViewModel::class.java]


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnAddCamera.setOnClickListener { startCameraX() }
        binding.btnAddGalery.setOnClickListener { startGallery() }
        binding.btnSaveStory.setOnClickListener {
//
            if (getFile != null) {

                if(binding.addDesc.text.toString().isNotEmpty()) {
                    val file = reduceFileImage(getFile as File)
                    signViewModel.getUser().observe(this){user->
                        mainViewModel.postNewStory(user.token, file, binding.addDesc.text.toString())
                        Toast.makeText(
                            this@AddNewStoryActivity,
                            "Upload story berhasil!",
                            Toast.LENGTH_SHORT).show()
                        val i = Intent(this, MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                    }

                } else {
                    Toast.makeText(this@AddNewStoryActivity, "Deskripsi harus di isi", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this@AddNewStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }


    }



    private fun startCameraX() {
        val intent = Intent(this, CameraNewStoryActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AddNewStoryActivity.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.tvAddImg.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddNewStoryActivity)
            getFile = myFile
            binding.tvAddImg.setImageURI(selectedImg)
        }
    }

    private fun reduceFileImage(file: File): File {
        return file
    }

}