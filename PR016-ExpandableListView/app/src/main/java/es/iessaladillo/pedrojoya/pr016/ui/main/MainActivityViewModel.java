package es.iessaladillo.pedrojoya.pr016.ui.main;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.pedrojoya.pr016.data.Repository;
import es.iessaladillo.pedrojoya.pr016.data.local.model.Level;
import es.iessaladillo.pedrojoya.pr016.data.local.model.Student;

class MainActivityViewModel extends ViewModel {


    @NonNull
    private final Repository repository;
    private List<Level> levels;
    private List<List<Student>> students;

    MainActivityViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    @NonNull
    List<Level> getLevels() {
        if (levels == null) {
            levels = repository.queryLevels();
        }
        return levels;
    }

    @NonNull
    List<List<Student>> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
            for (Level level: repository.queryLevels()) {
                students.add(repository.queryStudentsByLevel(level.getId()));
            }
        }
        return students;
    }

}
