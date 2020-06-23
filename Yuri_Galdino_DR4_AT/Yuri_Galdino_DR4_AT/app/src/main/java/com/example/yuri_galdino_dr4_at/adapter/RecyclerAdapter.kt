package com.example.yuri_galdino_dr4_at.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuri_galdino_dr4_at.R
import kotlinx.android.synthetic.main.activity_nova_anotacao.view.*
import kotlinx.android.synthetic.main.recycler_layout.view.*

class RecyclerAdapter(val lista: List<Anotacao>) :
    RecyclerView.Adapter<RecyclerAdapter.CamposViewHolder>() {
    class CamposViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val campoTitulo = v.tituloTxtVw
        val campoTexto = v.textoTxtVw
        val campoImg = v.imagemImgVw
        val campoData = v.dataTxtVw
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: CamposViewHolder, position: Int) {
        val list = lista[position]
        holder.campoData.text = list.data_anot
        holder.campoTexto.text = list.texto_anot
        holder.campoTitulo.text = list.titulo_anot
        holder.campoImg.setImageBitmap(list.imagem_anot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CamposViewHolder {
        val result =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false)
        return CamposViewHolder(result)
    }
}