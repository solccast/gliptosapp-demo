package com.example.gliptosapp.ui.colection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.databinding.ItemFosilDescBinding
import com.example.gliptosapp.ui.settings.applyFontScale

class FosilAdapter(
    private var lista: List<Fosil>,
    private val onDetalleClick: (Fosil) -> Unit
) : RecyclerView.Adapter<FosilAdapter.FosilViewHolder>() {

    class FosilViewHolder(val binding: ItemFosilDescBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FosilViewHolder {
        val binding = ItemFosilDescBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        (binding.root as ViewGroup).applyFontScale()
        return FosilViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FosilViewHolder, position: Int) {
        val fosil = lista[position]

        with(holder.binding) {
            nombreFosil.text = fosil.nombre
            imagenFosil.setImageResource(fosil.obtenerImagen())

            if (fosil.descubierto) {
                btnDetalle.visibility = View.VISIBLE
                btnDetalle.isEnabled = true
                btnDetalle.alpha = 1f
                btnDetalle.contentDescription = "Ver detalle del fósil ${fosil.nombre}"
                btnDetalle.setOnClickListener { onDetalleClick(fosil) }

                root.isClickable = true
                root.setOnClickListener { onDetalleClick(fosil) }
                root.contentDescription = "Fósil ${fosil.nombre}, descubierto."
            } else {
                btnDetalle.visibility = View.INVISIBLE
                btnDetalle.isEnabled = false
                btnDetalle.alpha = 0f
                btnDetalle.contentDescription = null
                btnDetalle.setOnClickListener(null)

                root.isClickable = false
                root.setOnClickListener(null)   // nunca pisado por código de abajo
                root.contentDescription = "Fósil ${fosil.nombre}, aún no descubierto."
            }
            root.isFocusable = true
        }
    }
    override fun getItemCount(): Int = lista.size
    fun updateList(nuevaLista: List<Fosil>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}