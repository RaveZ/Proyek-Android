import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.comment
import com.example.proyek_android.R
import com.google.firebase.firestore.FirebaseFirestore

class adapterComment (
    private var listComment: ArrayList<comment>
): RecyclerView.Adapter<adapterComment.ListViewHolder>()
{
    lateinit var db: FirebaseFirestore
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName : TextView = itemView.findViewById(R.id.comUname)
        var isiComment : TextView = itemView.findViewById(R.id.isiComment)
        var tanggal : TextView = itemView.findViewById(R.id.tanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemcomment,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var comment = listComment[position]
        holder.userName.setText(comment.nama)
        holder.isiComment.setText(comment.isi)
        holder.tanggal.setText(comment.tglComment)
    }
    override fun getItemCount(): Int {
        return listComment.size
    }

}