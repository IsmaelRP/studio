package es.iessaladillo.pedrojoya.pr107.ui.main;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import es.iessaladillo.pedrojoya.pr107.data.local.Repository;
import es.iessaladillo.pedrojoya.pr107.data.model.Student;

class MainActivityViewModel extends ViewModel {

    private final Repository repository;
    private List<Student> students;

    public MainActivityViewModel(Repository repository) {
        this.repository = repository;
    }

    public List<Student> getStudents() {
        if (students == null) {
            students = repository.getStudents();
        }
        return students;
    }

    public void addFakeStudent() {
        repository.addFakeStudent();
    }
}
