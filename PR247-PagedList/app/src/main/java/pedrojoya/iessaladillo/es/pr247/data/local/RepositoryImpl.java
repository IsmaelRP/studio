package pedrojoya.iessaladillo.es.pr247.data.local;

import android.arch.paging.DataSource;

import java.util.List;

import pedrojoya.iessaladillo.es.pr247.data.model.Student;

public class RepositoryImpl implements Repository {

    private static RepositoryImpl instance;

    private final Database database;

    private RepositoryImpl(Database database) {
        this.database = database;
    }

    public synchronized static RepositoryImpl getInstance(Database database) {
        if (instance == null) {
            instance = new RepositoryImpl(database);
        }
        return instance;
    }

    @Override
    public DataSource.Factory<Integer, Student> queryPagedStudents() {
        return database.queryPagedStudents();
    }

    @Override
    public void addFakeStudent() {
        database.addFakeStudent();
    }

    @Override
    public void deleteStudent(int position) {
        database.deleteStudent(position);
    }

}
