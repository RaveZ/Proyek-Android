import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.comment
import com.example.proyek_android.R

class adapterComment (
    private var listComment: ArrayList<comment>
): RecyclerView.Adapter<adapterComment.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName : TextView = itemView.findViewById(R.id.comUname)
        var isiComment : TextView = itemView.findViewById(R.id.isiComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}