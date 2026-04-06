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
 * Adapter de RecyclerView para mostrar una lista de usuarios ([User]).
 *
 * Permite ver detalles, editar o eliminar cada usuario usando lambdas pasadas desde la Activity o Fragment.
 *
 * @param llista Lista inicial de usuarios. Puede actualizarse posteriormente con [update].
 * @param onVeure Lambda llamada al hacer click sobre el item completo.
 * @param onEditar Lambda llamada al hacer click sobre el botón de editar.
 * @param onEliminar Lambda llamada al hacer click sobre el botón de eliminar.
 */
class UserAdapter(
    private var llista: List<User> = emptyList(),
    private val onVeure: (User) -> Unit,
    private val onEditar: (User) -> Unit,
    private val onEliminar: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserVH>() {

    /**
     * ViewHolder que contiene las vistas de un item de usuario.
     *
     * @property ivAvatar ImageView que muestra el avatar del usuario.
     * @property tvNomComplet TextView que muestra el nombre completo.
     * @property tvEmail TextView que muestra el email.
     * @property tvId TextView que muestra el ID del usuario.
     * @property btnEditar Button que permite editar el usuario.
     * @property btnEliminar Button que permite eliminar el usuario.
     */
    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvNomComplet: TextView = view.findViewById(R.id.tvNomComplet)
        val tvEmail:      TextView = view.findViewById(R.id.tvEmail)
        val tvId:         TextView = view.findViewById(R.id.tvId)
        val btnEditar:    Button   = view.findViewById(R.id.btnEditar)
        val btnEliminar:  Button   = view.findViewById(R.id.btnEliminar)
    }

    /**
     * Infla el layout del item y crea un [UserVH].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserVH(v)
    }

    /**
     * Asocia los datos de un [User] a las vistas del [UserVH].
     *
     * @param holder ViewHolder donde se asignarán los datos.
     * @param position Posición del usuario en la lista.
     */
    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = llista[position]

        holder.tvNomComplet.text  = "${user.nom} ${user.cognom}"
        holder.tvEmail.text       = user.email
        holder.tvId.text          = "#${user.id}"

        // Carga de avatar con Glide
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_placeholder)
                    .error(R.drawable.ic_person_placeholder)
                    .circleCrop()
            )
            .into(holder.ivAvatar)

        // Eventos de click
        holder.itemView.setOnClickListener { onVeure(user) }
        holder.btnEditar.setOnClickListener { onEditar(user) }
        holder.btnEliminar.setOnClickListener { onEliminar(user.id ?: "") }
    }

    /** Devuelve el tamaño de la lista de usuarios. */
    override fun getItemCount() = llista.size

    /**
     * Actualiza la lista de usuarios y notifica cambios al RecyclerView.
     *
     * @param novaLlista Nueva lista de usuarios.
     */
    fun update(novaLlista: List<User>) {
        llista = novaLlista
        notifyDataSetChanged()
    }
}