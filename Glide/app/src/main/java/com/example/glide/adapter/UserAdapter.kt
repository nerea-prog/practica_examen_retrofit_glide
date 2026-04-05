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

class UserAdapter(
    private var llista: List<User> = emptyList(),
    private val onVeure: (User) -> Unit,
    private val onEditar: (User) -> Unit,
    private val onEliminar: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserVH>() {

    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvNomComplet: TextView = view.findViewById(R.id.tvNomComplet)
        val tvEmail:      TextView = view.findViewById(R.id.tvEmail)
        val tvId:         TextView = view.findViewById(R.id.tvId)
        val btnEditar:    Button   = view.findViewById(R.id.btnEditar)
        val btnEliminar:  Button   = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserVH(v)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = llista[position]

        holder.tvNomComplet.text  = "${user.nom} ${user.cognom}"
        holder.tvEmail.text       = user.email
        holder.tvId.text          = "#${user.id}"

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_placeholder)
                    .error(R.drawable.ic_person_placeholder)
                    .circleCrop()
            )
            .into(holder.ivAvatar)

        holder.itemView.setOnClickListener { onVeure(user) }
        holder.btnEditar.setOnClickListener { onEditar(user) }
        holder.btnEliminar.setOnClickListener { onEliminar(user.id ?: "") }
    }

    override fun getItemCount() = llista.size

    fun update(novaLlista: List<User>) {
        llista = novaLlista
        notifyDataSetChanged()
    }
}
