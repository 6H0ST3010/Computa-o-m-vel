package com.project.hjcperformance.ui.clientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.project.hjcperformance.R
import com.project.hjcperformance.model.Cliente

class ClienteAdapter(
    private var listaClientes: List<Cliente>,
    private val onItemClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view
        val imgFoto: ImageView = view.findViewById(R.id.imgFotoCliente)
        val txtNome: TextView = view.findViewById(R.id.txtNomeCliente)
        val txtTelefone: TextView = view.findViewById(R.id.txtTelefoneCliente)
        val txtEmail: TextView = view.findViewById(R.id.txtEmailCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.txtNome.text = cliente.nome
        holder.txtTelefone.text = "${cliente.telefone}"
        holder.txtEmail.text = "${cliente.email}"

        // Carregamento de imagem com Coil
        if (cliente.fotoUrl.isNotEmpty()) {
            holder.imgFoto.load(cliente.fotoUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(android.R.drawable.ic_menu_gallery)
            }
        } else {
            holder.imgFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.root.setOnClickListener { onItemClick(cliente) }
    }

    override fun getItemCount(): Int = listaClientes.size

    fun atualizarLista(novaLista: List<Cliente>) {
        this.listaClientes = novaLista
        notifyDataSetChanged()
    }
}