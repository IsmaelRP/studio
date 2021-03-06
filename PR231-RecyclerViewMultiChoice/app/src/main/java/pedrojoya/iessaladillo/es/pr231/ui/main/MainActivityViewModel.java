package pedrojoya.iessaladillo.es.pr231.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pedrojoya.iessaladillo.es.pr231.data.Repository;
import pedrojoya.iessaladillo.es.pr231.data.local.model.Student;

class MainActivityViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<Student>> students;

    MainActivityViewModel(Repository repository) {
        this.repository = repository;
    }

    LiveData<List<Student>> getStudents() {
        if (students == null) {
            students = repository.queryStudents();
        }
        return students;
    }

    @SuppressWarnings("unused")
    void insertStudent(Student student) {
        repository.insertStudent(student);
    }

    @SuppressWarnings("unused")
    void deleteStudent(Student student) {
        repository.deleteStudent(student);
    }

}
