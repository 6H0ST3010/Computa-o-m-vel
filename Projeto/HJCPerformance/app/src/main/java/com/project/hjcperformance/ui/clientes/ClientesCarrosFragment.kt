package com.project.hjcperformance.ui.clientes

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hjcperformance.R
import com.project.hjcperformance.databinding.FragmentClientesCarrosBinding
import com.project.hjcperformance.repository.UserRepository

class ClientesCarrosFragment : Fragment(R.layout.fragment_clientes_carros) {

    private var _binding: FragmentClientesCarrosBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private lateinit var clienteAdapter: ClienteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClientesCarrosBinding.bind(view)

        configurarRecyclerView()
        observarDados()

        binding.fabAdicionarCliente.setOnClickListener {
            mostrarDialogoAdicionarCliente()
        }
    }

    private fun configurarRecyclerView() {
        clienteAdapter = ClienteAdapter(emptyList()) { cliente ->
            mostrarDialogoOpcoesCliente(cliente)
        }
        binding.recyclerViewClientes.apply {
            adapter = clienteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun mostrarDialogoOpcoesCliente(cliente: com.project.hjcperformance.model.Cliente) {
        val context = requireContext()
        val options = arrayOf(getString(R.string.dialog_edit_client), getString(R.string.dialog_delete_client))
        
        AlertDialog.Builder(context)
            .setTitle(cliente.nome)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> mostrarDialogoEditarCliente(cliente)
                    1 -> confirmarEliminarCliente(cliente)
                }
            }
            .show()
    }

    private fun mostrarDialogoEditarCliente(cliente: com.project.hjcperformance.model.Cliente) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.dialog_edit_client))

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 20)
        }

        val edtNome = EditText(context).apply { 
            hint = getString(R.string.label_name)
            setText(cliente.nome)
        }
        val edtTelefone = EditText(context).apply { 
            hint = getString(R.string.label_phone)
            setText(cliente.telefone)
        }
        val edtEmail = EditText(context).apply { 
            hint = getString(R.string.label_email)
            setText(cliente.email)
        }
        val edtFoto = EditText(context).apply { 
            hint = getString(R.string.label_photo_url)
            setText(cliente.fotoUrl)
        }

        layout.addView(edtNome)
        layout.addView(edtTelefone)
        layout.addView(edtEmail)
        layout.addView(edtFoto)
        builder.setView(layout)

        builder.setPositiveButton(getString(R.string.btn_save)) { dialog, _ ->
            val nome = edtNome.text.toString().trim()
            val telefone = edtTelefone.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val fotoUrl = edtFoto.text.toString().trim()

            if (nome.isNotEmpty()) {
                userRepository.atualizarCliente(cliente.id, nome, telefone, email, fotoUrl) { sucesso ->
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

    private fun confirmarEliminarCliente(cliente: com.project.hjcperformance.model.Cliente) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_client))
            .setMessage(getString(R.string.dialog_confirm_delete, cliente.nome))
            .setPositiveButton(getString(R.string.btn_delete)) { _, _ ->
                userRepository.eliminarCliente(cliente.id) { sucesso ->
                    if (sucesso) {
                        Toast.makeText(requireContext(), getString(R.string.msg_success), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private var listenerClientes: com.google.firebase.firestore.ListenerRegistration? = null
    private fun observarDados() {
        listenerClientes = userRepository.obterClientesDoMecanico { lista ->
            clienteAdapter.atualizarLista(lista)
        }
    }

    private fun mostrarDialogoAdicionarCliente() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.dialog_new_client))

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 20, 40, 20)

        val edtNome = EditText(context).apply { hint = getString(R.string.label_name) }
        val edtTelefone = EditText(context).apply { hint = getString(R.string.label_phone) }
        val edtEmail = EditText(context).apply { hint = getString(R.string.label_email) }
        val edtFoto = EditText(context).apply { hint = getString(R.string.label_photo_url) }

        layout.addView(edtNome)
        layout.addView(edtTelefone)
        layout.addView(edtEmail)
        layout.addView(edtFoto)
        builder.setView(layout)

        builder.setPositiveButton(getString(R.string.btn_save)) { dialog, _ ->
            val nome = edtNome.text.toString().trim()
            val telefone = edtTelefone.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val fotoUrl = edtFoto.text.toString().trim()

            if (nome.isNotEmpty()) {
                userRepository.salvarCliente(nome, telefone, email, fotoUrl) { sucesso ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        listenerClientes?.remove()
        _binding = null
    }
}