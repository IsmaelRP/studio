package es.iessaladillo.pedrojoya.pr219.data;

import java.util.List;

import es.iessaladillo.pedrojoya.pr219.data.model.Student;
import io.reactivex.Observable;

public interface Repository {
    Observable<List<Student>> getStudents();
}
