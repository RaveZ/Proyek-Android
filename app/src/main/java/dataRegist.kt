import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import com.example.proyek_android.R

data class dataRegist(
    val nama: String?,
    val noTelp: String?,
    val email: String?,
    val tglLahir: String?,
    val pass: String?,
    val alamat: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeString(noTelp)
        parcel.writeString(email)
        parcel.writeString(pass)
        parcel.writeString(alamat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<dataRegist> {
        override fun createFromParcel(parcel: Parcel): dataRegist {
            return dataRegist(parcel)
        }

        override fun newArray(size: Int): Array<dataRegist?> {
            return arrayOfNulls(size)
        }
    }
}

