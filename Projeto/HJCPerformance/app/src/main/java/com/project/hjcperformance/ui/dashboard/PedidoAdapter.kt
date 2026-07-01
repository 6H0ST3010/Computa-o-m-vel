package com.project.hjcperformance.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.hjcperformance.R
import com.project.hjcperformance.model.Pedido
import java.util.Locale

class PedidoAdapter(
    private var listaPedidos: List<Pedido>,
    private val onItemClick: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view
        val txtMatricula: TextView = view.findViewById(R.id.txtMatriculaPedido)
        val txtMarcaModelo: TextView = view.findViewById(R.id.txtDuracaoPedido) // Reutilizando o TextView da duração para marca/modelo
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoPedido)
        val txtDescricao: TextView = view.findViewById(R.id.txtDescricaoPedido)
        val txtOrcamento: TextView = view.findViewById(R.id.txtOrcamentoPedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]
        val context = holder.itemView.context

        holder.txtMatricula.text = pedido.matriculaAutomovel.uppercase(Locale.ROOT)
        
        // Mostrar Marca e Modelo onde era a duração
        holder.txtMarcaModelo.visibility = View.VISIBLE
        holder.txtMarcaModelo.text = "${pedido.marcaAutomovel} ${pedido.modeloAutomovel}"
        
        // Mapeamento dinâmico do estado para tradução
        val estadoTraduzido = when (pedido.estado) {
            "Em Aberto" -> context.getString(R.string.status_open)
            "Em Curso" -> context.getString(R.string.status_in_progress)
            "Concluído" -> context.getString(R.string.status_completed)
            "Cancelado" -> context.getString(R.string.status_cancelled)
            else -> pedido.estado
        }
        holder.txtEstado.text = estadoTraduzido
        
        holder.txtDescricao.text = pedido.descricaoProblema
        val orcamentoLabel = context.getString(R.string.label_budget)
        holder.txtOrcamento.text = String.format(Locale.getDefault(), "$orcamentoLabel %.2f €", pedido.orcamentoEstimado)

        // Aplicar cores diferentes conforme o estado
        when (pedido.estado) {
            "Concluído" -> holder.txtEstado.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
            "Em Curso" -> holder.txtEstado.setTextColor(android.graphics.Color.parseColor("#1565C0"))
            "Cancelado" -> holder.txtEstado.setTextColor(android.graphics.Color.parseColor("#C62828"))
            else -> holder.txtEstado.setTextColor(android.graphics.Color.parseColor("#E65100"))
        }

        holder.root.setOnClickListener { onItemClick(pedido) }
    }

    override fun getItemCount(): Int = listaPedidos.size

    fun atualizarLista(novaLista: List<Pedido>) {
        this.listaPedidos = novaLista
        notifyDataSetChanged()
    }
}