package com.project.hjcperformance.repository

import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.hjcperformance.model.User
import com.project.hjcperformance.model.PremiumStatus

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * Recupera os dados do utilizador atual.
     */
    fun obterDadosUtilizador(onResult: (User?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.toObject(User::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    /**
     * Verifica o estado Premium do utilizador atual.
     */
    fun verificarEstadoPremium(onResult: (PremiumStatus) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            mainHandler.post { onResult(PremiumStatus.NotAuthenticated) }
            return
        }

        db.collection("users").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user != null && user.isPremium) {
                        mainHandler.post { onResult(PremiumStatus.Premium) }
                    } else {
                        mainHandler.post { onResult(PremiumStatus.Free) }
                    }
                } else {
                    mainHandler.post { onResult(PremiumStatus.Free) }
                }
            }
            .addOnFailureListener { exception ->
                mainHandler.post { onResult(PremiumStatus.Error(exception)) }
            }
    }

    /**
     * Cria o perfil inicial do mecânico após o registo.
     */
    fun criarPerfilUtilizador(nome: String, email: String, onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val novoUser = User(uid = uid, nome = nome, email = email, isPremium = false)
        
        db.collection("users").document(uid).set(novoUser)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Ativa o plano Premium (Simulação).
     */
    fun simularCompraPremium(onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onComplete(false)
        db.collection("users").document(uid).update("isPremium", true)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Desativa o plano Premium (Simulação).
     */
    fun desativarPremium(onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onComplete(false)
        db.collection("users").document(uid).update("isPremium", false)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Grava um novo cliente.
     */
    fun salvarCliente(nome: String, telefone: String, email: String, fotoUrl: String = "", onComplete: (Boolean) -> Unit) {
        val uidMecanico = auth.currentUser?.uid ?: return onComplete(false)
        val docRef = db.collection("clientes").document()
        val novoCliente = com.project.hjcperformance.model.Cliente(
            id = docRef.id, idMecanico = uidMecanico, nome = nome, telefone = telefone, email = email, fotoUrl = fotoUrl
        )
        docRef.set(novoCliente)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Atualiza os dados de um cliente.
     */
    fun atualizarCliente(idCliente: String, nome: String, telefone: String, email: String, fotoUrl: String, onComplete: (Boolean) -> Unit) {
        val updates = mapOf("nome" to nome, "telefone" to telefone, "email" to email, "fotoUrl" to fotoUrl)
        db.collection("clientes").document(idCliente).update(updates)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Elimina um cliente.
     */
    fun eliminarCliente(idCliente: String, onComplete: (Boolean) -> Unit) {
        db.collection("clientes").document(idCliente).delete()
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Recupera todos os clientes do mecânico atual.
     */
    fun obterClientesDoMecanico(onResult: (List<com.project.hjcperformance.model.Cliente>) -> Unit): com.google.firebase.firestore.ListenerRegistration? {
        val uidMecanico = auth.currentUser?.uid ?: return null
        return db.collection("clientes")
            .whereEqualTo("idMecanico", uidMecanico)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    mainHandler.post { onResult(emptyList()) }
                    return@addSnapshotListener
                }
                val lista = snapshot.toObjects(com.project.hjcperformance.model.Cliente::class.java)
                mainHandler.post { onResult(lista) }
            }
    }

    /**
     * Grava um novo pedido.
     */
    fun salvarPedido(matricula: String, marca: String, modelo: String, idCliente: String, descricao: String, orcamento: Double, onComplete: (Boolean) -> Unit) {
        val uidMecanico = auth.currentUser?.uid ?: return onComplete(false)
        val docRef = db.collection("pedidos").document()
        val novoPedido = com.project.hjcperformance.model.Pedido(
            id = docRef.id, idMecanico = uidMecanico, matriculaAutomovel = matricula.uppercase().trim(),
            marcaAutomovel = marca, modeloAutomovel = modelo, idCliente = idCliente,
            descricaoProblema = descricao, orcamentoEstimado = orcamento, estado = "Em Aberto"
        )
        docRef.set(novoPedido)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Atualiza um pedido.
     */
    fun atualizarPedido(idPedido: String, descricao: String, orcamento: Double, marca: String, modelo: String, estado: String, onComplete: (Boolean) -> Unit) {
        val updates = mapOf(
            "descricaoProblema" to descricao, "orcamentoEstimado" to orcamento,
            "marcaAutomovel" to marca, "modeloAutomovel" to modelo, "estado" to estado
        )
        db.collection("pedidos").document(idPedido).update(updates)
            .addOnSuccessListener { mainHandler.post { onComplete(true) } }
            .addOnFailureListener { mainHandler.post { onComplete(false) } }
    }

    /**
     * Recupera os pedidos em tempo real.
     */
    fun obterPedidosAtivos(onResult: (List<com.project.hjcperformance.model.Pedido>) -> Unit): com.google.firebase.firestore.ListenerRegistration? {
        val uidMecanico = auth.currentUser?.uid ?: return null
        return db.collection("pedidos")
            .whereEqualTo("idMecanico", uidMecanico)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    mainHandler.post { onResult(emptyList()) }
                    return@addSnapshotListener
                }
                val lista = snapshot.toObjects(com.project.hjcperformance.model.Pedido::class.java)
                mainHandler.post { onResult(lista) }
            }
    }
}