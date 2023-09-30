package com.example.drawingapp

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var drawingView: DrawingView
    private lateinit var brushButton: ImageButton
    private lateinit var purpleButton: ImageButton
    private lateinit var redButton: ImageButton
    private lateinit var orangeButton: ImageButton
    private lateinit var blueButton: ImageButton
    private lateinit var greenButton: ImageButton
    private lateinit var undoButton: ImageButton
    private lateinit var colorPickerButton: ImageButton
    private lateinit var galleryButton: ImageButton

    private val requestPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted && permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    if (permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        brushButton = findViewById(R.id.brush_button)
        drawingView = findViewById(R.id.drawing_view)
        purpleButton = findViewById(R.id.purple_button)
        blueButton = findViewById(R.id.blue_button)
        orangeButton = findViewById(R.id.orange_button)
        redButton = findViewById(R.id.red_button)
        greenButton = findViewById(R.id.green_button)
        undoButton = findViewById(R.id.undo_button)
        colorPickerButton = findViewById(R.id.color_picker_button)
        galleryButton = findViewById(R.id.gallery_button)

        drawingView.changeBrushSize(23.toFloat())
        brushButton.setOnClickListener {
            showBrushChooseDialog()
        }

        purpleButton.setOnClickListener(this)
        blueButton.setOnClickListener(this)
        orangeButton.setOnClickListener(this)
        redButton.setOnClickListener(this)
        greenButton.setOnClickListener(this)
        undoButton.setOnClickListener(this)
        colorPickerButton.setOnClickListener(this)
        galleryButton.setOnClickListener(this)
    }

    private fun showBrushChooseDialog() {
        val brushDialog = Dialog(this@MainActivity)
        brushDialog.setContentView(R.layout.dialog_brush)

        val seekBarProgress = brushDialog.findViewById<SeekBar>(R.id.dialog_seek_bar)
        val showProgressTv = brushDialog.findViewById<TextView>(R.id.dialog_text_view_progress)

        seekBarProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, p1: Int, p2: Boolean) {
                drawingView.changeBrushSize(seekBar.progress.toFloat())
                showProgressTv.text = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        brushDialog.show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.purple_button -> {
                drawingView.setColor("#D14EF6")
            }

            R.id.red_button -> {
                drawingView.setColor("#FA5B68")
            }

            R.id.orange_button -> {
                drawingView.setColor("#EFB041")
            }

            R.id.green_button -> {
                drawingView.setColor("#2DC40B")
            }

            R.id.blue_button -> {
                drawingView.setColor("#2F6FF1")
            }

            R.id.undo_button -> {
                drawingView.undoPath()
            }

            R.id.color_picker_button -> {
                showColorPickerDialog()
            }

            R.id.gallery_button -> {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestStoragePermission()
                } else {
                    // Get the image
                }
            }
        }
    }

    private fun showColorPickerDialog() {
        val dialog = AmbilWarnaDialog(this, Color.GREEN, object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {}

            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                drawingView.setColor(color)
            }
        })

        dialog.show()
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            showRationaleDialog()
        } else {
            requestPermissions.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private fun showRationaleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Storage permission")
            .setMessage("We need this permission in order to access the internal storage")
            .setPositiveButton(R.string.dialog_yes) { dialog, _ ->
                requestPermissions.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                dialog.dismiss()
            }

        builder.create().show()
    }
}