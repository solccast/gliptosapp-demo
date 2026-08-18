package com.example.gliptosapp.ui.colection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gliptosapp.R
import com.example.gliptosapp.data.relations.FosilConEstado
import com.example.gliptosapp.databinding.ItemFosilDescBinding
import com.example.gliptosapp.databinding.ItemFosilNoDescBinding
import com.example.gliptosapp.ui.settings.appearance.applyAccessibilityPreferences

class FosilAdapter(
    private var lista: List<FosilConEstado>,
    private val onDetalleClick: (FosilConEstado) -> Unit,
    private val onNoDescubiertoClick: (FosilConEstado) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DESCUBIERTO = 0
        private const val TYPE_NO_DESCUBIERTO = 1
    }

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
                (binding.root as ViewGroup).applyAccessibilityPreferences()
                FosilDescViewHolder(binding)
            }
            else -> {
                val binding = ItemFosilNoDescBinding.inflate(inflater, parent, false)
                (binding.root as ViewGroup).applyAccessibilityPreferences()
                FosilNoDescViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = lista[position]
        when (holder) {
            is FosilDescViewHolder -> bindDescubierto(holder, item)
            is FosilNoDescViewHolder -> bindNoDescubierto(holder, item)
        }
    }

    private fun bindDescubierto(holder: FosilDescViewHolder, item: FosilConEstado) {
        with(holder.binding) {
            nombreFosil.text = item.fosil.nombre
            imagenFosil.setImageResource(resolverDrawable(root.context, item.obtenerImagen()))
            btnDetalle.contentDescription = "Ver detalle del fósil ${item.fosil.nombre}"
            btnDetalle.setOnClickListener { onDetalleClick(item) }

            root.isClickable = true
            root.isFocusable = true
            root.setOnClickListener { onDetalleClick(item) }
            root.contentDescription = "Fósil ${item.fosil.nombre}, descubierto."
        }
    }

    private fun bindNoDescubierto(holder: FosilNoDescViewHolder, item: FosilConEstado) {
        with(holder.binding) {
            imagenFosilSilueta.setImageResource(resolverDrawable(root.context, item.obtenerImagen()))

            root.isClickable = true
            root.isFocusable = true
            root.setOnClickListener { onNoDescubiertoClick(item) }
            root.contentDescription = "Fósil ${item.fosil.nombre}, aún no descubierto. Toca para más información."
        }
    }

    private fun resolverDrawable(context: Context, nombreRecurso: String): Int {
        val resId = context.resources.getIdentifier(nombreRecurso, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.gliptodonte_sin_descubrir
    }

    override fun getItemCount(): Int = lista.size

    fun updateList(nuevaLista: List<FosilConEstado>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}