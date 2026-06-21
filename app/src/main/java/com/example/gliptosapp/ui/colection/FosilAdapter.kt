package com.example.gliptosapp.ui.colection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.databinding.ItemFosilDescBinding
import com.example.gliptosapp.databinding.ItemFosilNoDescBinding
import com.example.gliptosapp.ui.settings.applyFontScale

class FosilAdapter(
    private var lista: List<Fosil>,
    private val onDetalleClick: (Fosil) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DESCUBIERTO = 0
        private const val TYPE_NO_DESCUBIERTO = 1
    }

    // ── ViewHolders ──────────────────────────────
    class FosilDescViewHolder(val binding: ItemFosilDescBinding) :
        RecyclerView.ViewHolder(binding.root)

    class FosilNoDescViewHolder(val binding: ItemFosilNoDescBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (lista[position].descubierto) TYPE_DESCUBIERTO else TYPE_NO_DESCUBIERTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_DESCUBIERTO -> {
                val binding = ItemFosilDescBinding.inflate(inflater, parent, false)
                (binding.root as ViewGroup).applyFontScale()
                FosilDescViewHolder(binding)
            }
            else -> {
                val binding = ItemFosilNoDescBinding.inflate(inflater, parent, false)
                (binding.root as ViewGroup).applyFontScale()
                FosilNoDescViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fosil = lista[position]

        when (holder) {
            is FosilDescViewHolder -> bindDescubierto(holder, fosil)
            is FosilNoDescViewHolder -> bindNoDescubierto(holder, fosil)
        }
    }

    private fun bindDescubierto(holder: FosilDescViewHolder, fosil: Fosil) {
        with(holder.binding) {
            nombreFosil.text = fosil.nombre
            imagenFosil.setImageResource(fosil.obtenerImagen())

            btnDetalle.contentDescription = "Ver detalle del fósil ${fosil.nombre}"
            btnDetalle.setOnClickListener { onDetalleClick(fosil) }

            root.isClickable = true
            root.isFocusable = true
            root.setOnClickListener { onDetalleClick(fosil) }
            root.contentDescription = "Fósil ${fosil.nombre}, descubierto."
        }
    }

    private fun bindNoDescubierto(holder: FosilNoDescViewHolder, fosil: Fosil) {
        with(holder.binding) {
            imagenFosilSilueta.setImageResource(fosil.obtenerImagen())

            root.isClickable = false
            root.isFocusable = true
            root.setOnClickListener(null)
            root.contentDescription = "Fósil ${fosil.nombre}, aún no descubierto."
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateList(nuevaLista: List<Fosil>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}