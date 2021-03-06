package com.rafhabs.interactintegra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafhabs.interactintegra.R
import com.rafhabs.interactintegra.model.ContatosVO
import kotlinx.android.synthetic.main.item_contato.view.*

class ContatoAdapter (

    private val context: Context,
    private val lista: List<ContatosVO>,
    private val onClick: ((Int) -> Unit)

    ) : RecyclerView.Adapter<ContatoViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contato, parent, false)
        return ContatoViewHolder(view)

    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val contato = lista[position]
        with(holder.itemView){
            tvNome.text = "Tecnico: "+contato.nome
            tvOs.text = "OS: "+contato.os
            //tvCdevento.text = "Codigo: "+contato.codevento
            tvEvento.text = "Evento: "+contato.evento
            tvDataI.text = "Inicio: "+contato.datahorai
            tvDataF.text = "Fim: "+contato.datahoraf
            llItem.setOnClickListener { onClick(contato.id) }
        }
    }

    override fun getItemCount(): Int = lista.size

}


class ContatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)