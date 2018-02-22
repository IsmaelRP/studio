package es.iessaladillo.pedrojoya.pr153.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import es.iessaladillo.pedrojoya.pr153.BR;
import es.iessaladillo.pedrojoya.pr153.R;
import es.iessaladillo.pedrojoya.pr153.dbutils.RecyclerBindingAdapter;

@Entity
public class Student extends BaseObservable implements RecyclerBindingAdapter.ItemViewType {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private  String name;
    private  String address;
    private  String photoUrl;

    @Ignore
    public Student(String name, String address, String photoUrl) {
        this.id = 0;
        this.name = name;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public Student() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(es.iessaladillo.pedrojoya.pr153.BR.name);
    }

    @Bindable
    public String getPhotoUrl() {
        return photoUrl;

    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        notifyPropertyChanged(BR.photoUrl);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Ignore
    @Override
    public int getLayoutId() {
        return R.layout.activity_main_item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        if (address != null ? !address.equals(student.address) : student.address != null)
            return false;
        return photoUrl != null ? photoUrl.equals(student.photoUrl) : student.photoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        return result;
    }

}