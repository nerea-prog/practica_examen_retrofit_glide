package com.example.glide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.glide.R
import com.example.glide.model.User

/**
 * ADAPTER AMB GLIDE
 *
 * La clau de Glide és a onBindViewHolder:
 *
 *   Glide.with(context)
 *       .load(url)            ← URL de la imatge (String)
 *       .placeholder(R.drawable.ic_person)   ← mentre carrega
 *       .error(R.drawable.ic_error)          ← si falla
 *       .apply(RequestOptions().circleCrop()) ← forma circular
 *       .into(imageView)      ← ImageView destí
 */
class UserAdapter(
    private var llista: List<User> = emptyList(),
    private val onVeure: (User) -> Unit,
    private val onEditar: (User) -> Unit,
    private val onEliminar: (Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserVH>() {

    // ViewHolder: conté referències als Views de cada fila
    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvNomComplet: TextView = view.findViewById(R.id.tvNomComplet)
        val tvEmail:      TextView = view.findViewById(R.id.tvEmail)
        val tvId:         TextView = view.findViewById(R.id.tvId)
        val btnEditar:    Button   = view.findViewById(R.id.btnEditar)
        val btnEliminar:  Button   = view.findViewById(R.id.btnEliminar)
    }

    // 1. Infla el layout XML de cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserVH(v)
    }

    // 2. Omple cada fila amb les dades + carrega la imatge amb Glide
    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = llista[position]

        // Dades de text
        holder.tvNomComplet.text  = "${user.nom} ${user.cognom}"
        holder.tvEmail.text       = user.email
        holder.tvId.text          = "#${user.id}"

        // ── GLIDE: carrega l'avatar des d'una URL ──────────────────────────
        Glide.with(holder.itemView.context)
            .load(user.avatar)                       // URL de la imatge
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_placeholder) // mentre carrega
                    .error(R.drawable.ic_person_placeholder)        // si falla
                    .circleCrop()                                    // forma circular
            )
            .into(holder.ivAvatar)                   // ImageView destí
        // ──────────────────────────────────────────────────────────────────

        // Listeners
        holder.itemView.setOnClickListener { onVeure(user) }
        holder.btnEditar.setOnClickListener { onEditar(user) }
        holder.btnEliminar.setOnClickListener { onEliminar(user.id) }
    }

    // 3. Quantes files hi ha
    override fun getItemCount() = llista.size

    // Actualitzar les dades i refrescar
    fun update(novaLlista: List<User>) {
        llista = novaLlista
        notifyDataSetChanged()
    }
}