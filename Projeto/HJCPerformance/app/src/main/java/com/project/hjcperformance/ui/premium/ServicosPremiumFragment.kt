package com.project.hjcperformance.ui.premium

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.hjcperformance.R
import com.project.hjcperformance.databinding.FragmentServicosPremiumBinding
import com.project.hjcperformance.repository.UserRepository
import com.project.hjcperformance.model.PremiumStatus
import java.util.Locale
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ServicosPremiumFragment : Fragment(R.layout.fragment_servicos_premium) {

    private var _binding: FragmentServicosPremiumBinding? = null
    private val binding get() = _binding!!

    private val userRepository = UserRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentServicosPremiumBinding.bind(view)

        configurarInterfacePorPerfil()
    }

    private fun configurarInterfacePorPerfil() {
        binding.progressBar.visibility = View.VISIBLE

        userRepository.verificarEstadoPremium { status ->
            binding.progressBar.visibility = View.GONE

            when (status) {
                is PremiumStatus.Premium -> {
                    binding.layoutPaywall.visibility = View.GONE
                    binding.layoutConteudoPremium.visibility = View.VISIBLE

                    carregarDadosFinanceiros()
                    
                    binding.btnDesativarPremium.setOnClickListener {
                        userRepository.desativarPremium { sucesso ->
                            if (sucesso) {
                                Toast.makeText(context, getString(R.string.msg_success), Toast.LENGTH_SHORT).show()
                                configurarInterfacePorPerfil()
                            }
                        }
                    }
                }
                is PremiumStatus.Free -> {
                    binding.layoutConteudoPremium.visibility = View.GONE
                    binding.layoutPaywall.visibility = View.VISIBLE

                    configurarBotaoAssinatura()
                }
                else -> {}
            }
        }
    }

    private fun carregarDadosFinanceiros() {
        userRepository.obterPedidosAtivos { lista ->
            var total = 0.0
            val faturacaoPorEstado = mutableMapOf<String, Float>()
            
            // Estados internos da BD
            val estadosDb = listOf("Em Aberto", "Em Curso", "Concluído", "Cancelado")
            estadosDb.forEach { faturacaoPorEstado[it] = 0f }

            lista.forEach { pedido ->
                if (pedido.estado != "Cancelado") {
                    total += pedido.orcamentoEstimado
                    val atual = faturacaoPorEstado[pedido.estado] ?: 0f
                    faturacaoPorEstado[pedido.estado] = atual + pedido.orcamentoEstimado.toFloat()
                }
            }

            val totalFormatado = String.format(Locale.getDefault(), "%.2f", total)
            binding.txtTotalFaturado.text = getString(R.string.label_total_billing, totalFormatado)
            
            configurarGrafico(faturacaoPorEstado)
        }
    }

    private fun configurarGrafico(dados: Map<String, Float>) {
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        var index = 0f
        // Mapear labels para o idioma atual
        dados.forEach { (estadoDb, valor) ->
            entries.add(BarEntry(index, valor))
            
            val labelTraduzida = when(estadoDb) {
                "Em Aberto" -> getString(R.string.status_open)
                "Em Curso" -> getString(R.string.status_in_progress)
                "Concluído" -> getString(R.string.status_completed)
                "Cancelado" -> getString(R.string.status_cancelled)
                else -> estadoDb
            }
            labels.add(labelTraduzida)
            index += 1f
        }

        val dataSet = BarDataSet(entries, "Euros (€)")
        dataSet.colors = listOf(
            android.graphics.Color.parseColor("#E65100"), // Em Aberto
            android.graphics.Color.parseColor("#1565C0"), // Em Curso
            android.graphics.Color.parseColor("#2E7D32"), // Concluído
            android.graphics.Color.parseColor("#C62828")  // Cancelado
        )
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        binding.barChart.apply {
            data = barData
            description.isEnabled = false
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            animateY(1000)
            invalidate()
        }
    }

    private fun configurarBotaoAssinatura() {
        binding.btnAssinarPremium.setOnClickListener {
            simularCompraPremium()
        }
    }

    private fun simularCompraPremium() {
        binding.progressBar.visibility = View.VISIBLE
        userRepository.simularCompraPremium { sucesso ->
            binding.progressBar.visibility = View.GONE
            if (sucesso) {
                Toast.makeText(context, "Premium Ativado!", Toast.LENGTH_SHORT).show()
                configurarInterfacePorPerfil()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}