package com.project.hjcperformance.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hjcperformance.R
import com.project.hjcperformance.databinding.FragmentPedidosBinding
import com.project.hjcperformance.repository.UserRepository

class PedidosFragment : Fragment(R.layout.fragment_pedidos) {

    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    private val userRepository = UserRepository()
    private lateinit var pedidoAdapter: PedidoAdapter
    private var listenerPedidos: com.google.firebase.firestore.ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPedidosBinding.bind(view)

        // 1. Configurar a RecyclerView para os Pedidos
        pedidoAdapter = PedidoAdapter(emptyList()) { pedido ->
            mostrarDialogoEditarPedido(pedido)
        }
        binding.rvPedidos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pedidoAdapter
        }

        // 2. Carregar os dados em tempo real
        observarPedidos()

        // 3. Botão para adicionar novo pedido
        binding.fabAdicionarPedido.setOnClickListener {
            mostrarDialogoAdicionarPedido()
        }
    }

    private fun observarPedidos() {
        // Guardamos o listener para remover no onDestroyView
        listenerPedidos = userRepository.obterPedidosAtivos { listaDePedidos ->
            pedidoAdapter.atualizarLista(listaDePedidos)
        }
    }

    private fun mostrarDialogoEditarPedido(pedido: com.project.hjcperformance.model.Pedido) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.dialog_edit_order, pedido.matriculaAutomovel))

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val edtMarca = EditText(context).apply { 
            hint = getString(R.string.label_brand)
            setText(pedido.marcaAutomovel)
        }
        val edtModelo = EditText(context).apply { 
            hint = getString(R.string.label_model)
            setText(pedido.modeloAutomovel)
        }
        val edtDescricao = EditText(context).apply { 
            hint = getString(R.string.label_description)
            setText(pedido.descricaoProblema)
        }
        val edtOrcamento = EditText(context).apply { 
            hint = getString(R.string.label_budget)
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(pedido.orcamentoEstimado.toString())
        }

        // Spinner para o Estado
        val spinnerEstado = Spinner(context)
        val estadosMap = mapOf(
            getString(R.string.status_open) to "Em Aberto",
            getString(R.string.status_in_progress) to "Em Curso",
            getString(R.string.status_completed) to "Concluído",
            getString(R.string.status_cancelled) to "Cancelado"
        )
        val estadosDisplay = estadosMap.keys.toTypedArray()
        val adapterEstado = ArrayAdapter(context, android.R.layout.simple_spinner_item, estadosDisplay)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstado
        
        // Encontrar a posição correta baseada no valor interno (BD)
        val currentDisplay = estadosMap.filterValues { it == pedido.estado }.keys.firstOrNull()
        spinnerEstado.setSelection(estadosDisplay.indexOf(currentDisplay))

        layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_brand) })
        layout.addView(edtMarca)
        layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_model) })
        layout.addView(edtModelo)
        layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_description) })
        layout.addView(edtDescricao)
        layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_budget) })
        layout.addView(edtOrcamento)
        layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_status) })
        layout.addView(spinnerEstado)

        builder.setView(layout)

        builder.setPositiveButton(getString(R.string.btn_save_changes)) { dialog, _ ->
            val novaMarca = edtMarca.text.toString()
            val novoModelo = edtModelo.text.toString()
            val novaDescricao = edtDescricao.text.toString()
            val novoOrcamento = edtOrcamento.text.toString().toDoubleOrNull() ?: 0.0
            val novoEstadoDisplay = spinnerEstado.selectedItem.toString()
            val novoEstadoDb = estadosMap[novoEstadoDisplay] ?: "Em Aberto"

            userRepository.atualizarPedido(pedido.id, novaDescricao, novoOrcamento, novaMarca, novoModelo, novoEstadoDb) { sucesso ->
                if (sucesso) {
                    Toast.makeText(context, getString(R.string.btn_save), Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.btn_close)) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun mostrarDialogoAdicionarPedido() {
        val context = requireContext()
        
        userRepository.obterClientesDoMecanico { clientes ->
            if (clientes.isEmpty()) {
                Toast.makeText(context, getString(R.string.msg_add_client_first), Toast.LENGTH_LONG).show()
                return@obterClientesDoMecanico
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.dialog_new_order))

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 20, 50, 20)
            }

            val spinnerClientes = Spinner(context)
            val adapterClientes = ArrayAdapter(context, android.R.layout.simple_spinner_item, clientes.map { it.nome })
            adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerClientes.adapter = adapterClientes

            val edtMatricula = EditText(context).apply { hint = getString(R.string.label_plate) }
            val edtMarca = EditText(context).apply { hint = getString(R.string.label_brand) }
            val edtModelo = EditText(context).apply { hint = getString(R.string.label_model) }
            val edtDescricao = EditText(context).apply { hint = getString(R.string.label_description) }
            val edtOrcamento = EditText(context).apply { 
                hint = getString(R.string.label_budget)
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

            layout.addView(android.widget.TextView(context).apply { text = getString(R.string.label_client) })
            layout.addView(spinnerClientes)
            layout.addView(edtMatricula)
            layout.addView(edtMarca)
            layout.addView(edtModelo)
            layout.addView(edtDescricao)
            layout.addView(edtOrcamento)

            builder.setView(layout)

            builder.setPositiveButton(getString(R.string.btn_create)) { dialog, _ ->
                val clienteSelecionado = clientes[spinnerClientes.selectedItemPosition]
                val matricula = edtMatricula.text.toString()
                val marca = edtMarca.text.toString()
                val modelo = edtModelo.text.toString()
                val descricao = edtDescricao.text.toString()
                val orcamentoStr = edtOrcamento.text.toString()

                if (matricula.isNotEmpty() && descricao.isNotEmpty()) {
                    val orcamento = orcamentoStr.toDoubleOrNull() ?: 0.0
                    userRepository.salvarPedido(
                        matricula,
                        marca,
                        modelo,
                        clienteSelecionado.id,
                        descricao,
                        orcamento
                    ) { sucesso ->
                        if (sucesso) {
                            Toast.makeText(context, getString(R.string.msg_success), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton(getString(R.string.btn_cancel)) { dialog, _ -> dialog.cancel() }
            builder.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerPedidos?.remove()
        _binding = null
    }
}