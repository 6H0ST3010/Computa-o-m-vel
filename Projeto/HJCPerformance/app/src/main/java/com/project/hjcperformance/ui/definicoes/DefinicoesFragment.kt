package com.project.hjcperformance.ui.definicoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.project.hjcperformance.R
import com.project.hjcperformance.databinding.FragmentDefinicoesBinding
import com.project.hjcperformance.repository.UserRepository
import com.project.hjcperformance.ui.auth.LoginActivity

class DefinicoesFragment : Fragment(R.layout.fragment_definicoes) {

    private var _binding: FragmentDefinicoesBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDefinicoesBinding.bind(view)

        // Carregar foto do autor a partir dos recursos locais (drawable)
        binding.imgAutor.setImageResource(R.drawable.foto_autor)

        carregarDadosPerfil()

        // Botão de Ajuda
        binding.btnAjuda.setOnClickListener {
            mostrarDialogoAjuda()
        }

        // Lógica para terminar a sessão no Firebase Auth
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            // Redireciona de volta para o Login e limpa o histórico de ecrãs
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun mostrarDialogoAjuda() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.btn_help))
            .setMessage(
                "1. " + getString(R.string.title_clients_vehicles) + "\n\n" +
                "2. " + getString(R.string.title_active_orders) + "\n\n" +
                "3. " + getString(R.string.title_premium_financial)
            )
            .setPositiveButton("OK", null)
            .show()
    }

    private fun carregarDadosPerfil() {
        userRepository.obterDadosUtilizador { user ->
            user?.let {
                val labelNome = getString(R.string.label_name)
                val labelEmail = getString(R.string.label_email)
                
                binding.txtNomeUser.text = "$labelNome ${it.nome}"
                binding.txtEmailUser.text = "$labelEmail ${it.email}"
                
                if (it.isPremium) {
                    binding.txtStatusUser.text = getString(R.string.plan_premium)
                    binding.txtStatusUser.setTextColor(android.graphics.Color.parseColor("#FFD700"))
                } else {
                    binding.txtStatusUser.text = getString(R.string.plan_free)
                    binding.txtStatusUser.setTextColor(android.graphics.Color.GRAY)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}