package com.example.gliptosapp.ui.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.gliptosapp.databinding.DialogAvisoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AvisoDialog {

    fun mostrar(
        context: Context,
        mensaje: CharSequence,
        onEntendido: () -> Unit = {}
    ) {
        val dialogBinding = DialogAvisoBinding.inflate(LayoutInflater.from(context))
        dialogBinding.tvMensajeAviso.text = mensaje

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnEntendido.setOnClickListener {
            dialog.dismiss()
            onEntendido()
        }

        dialog.show()
    }
}