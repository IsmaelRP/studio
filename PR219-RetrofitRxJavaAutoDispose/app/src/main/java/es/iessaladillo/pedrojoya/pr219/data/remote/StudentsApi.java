package es.iessaladillo.pedrojoya.pr219.data.remote;

import java.util.List;

import es.iessaladillo.pedrojoya.pr219.data.model.Student;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface StudentsApi {

    @GET("datos.json")
    Observable<List<Student>> getStudents();

}
