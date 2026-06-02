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

        // TODO: hay que reemplazar este código feo por menos if
        with(holder.binding) {
            nombreFosil.text = fosil.nombre
            //estadoFosil.text = if (fosil.descubierto) "Descubierto" else "No descubierto"
            imagenFosil.setImageResource(fosil.obtenerImagen())
            btnDetalle.isEnabled = fosil.descubierto
            btnDetalle.visibility = if (!fosil.descubierto) View.GONE else View.VISIBLE
            // accesibilidad clau clau
            root.contentDescription =
                "Fósil ${fosil.nombre}, " +
                        if (fosil.descubierto) "descubierto" else "no descubierto"

            btnDetalle.contentDescription =
                "Ver detalle del fósil ${fosil.nombre}"
            btnDetalle.setOnClickListener {
                if (fosil.descubierto) {
                    onDetalleClick(fosil)
                }
            }
        }
    }

    override fun getItemCount(): Int = lista.size
    fun updateList(nuevaLista: List<Fosil>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}