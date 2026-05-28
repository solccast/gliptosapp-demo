package com.example.gliptosapp.ui.colection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.databinding.ItemFosilBinding

class FosilAdapter(
    private val lista: List<Fosil>
) : RecyclerView.Adapter<FosilAdapter.FosilViewHolder>() {

    class FosilViewHolder(val binding: ItemFosilBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FosilViewHolder {
        val binding = ItemFosilBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FosilViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FosilViewHolder, position: Int) {
        val fosil = lista[position]

        // TODO: hay que reemplazar este código feo por menos if
        with(holder.binding) {
            nombreFosil.text = fosil.nombre
            estadoFosil.text = if (fosil.descubierto) "Descubierto" else "No descubierto"

            btnDetalle.isEnabled = fosil.descubierto

            // ♿ accesibilidad
            root.contentDescription =
                "Fósil ${fosil.nombre}, " +
                        if (fosil.descubierto) "descubierto" else "no descubierto"

            btnDetalle.contentDescription =
                "Ver detalle del fósil ${fosil.nombre}"
        }
    }

    override fun getItemCount(): Int = lista.size
}