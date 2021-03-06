package es.iessaladillo.pedrojoya.pr107.data.local;

import java.util.List;

import es.iessaladillo.pedrojoya.pr107.data.model.Student;

@SuppressWarnings({"WeakerAccess", "unused"})
public interface Repository {

    List<Student> getStudents();
    void addFakeStudent();
    void deleteStudent(int position);

}
