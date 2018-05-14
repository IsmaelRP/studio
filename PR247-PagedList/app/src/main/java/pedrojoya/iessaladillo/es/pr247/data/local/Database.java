package pedrojoya.iessaladillo.es.pr247.data.local;

import android.arch.core.util.Function;
import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mooveit.library.Fakeit;

import java.util.ArrayList;
import java.util.List;

import pedrojoya.iessaladillo.es.pr247.data.model.Student;

public class Database {

    private static Database instance;

    private PagedList<Student> students;
    private int mCount;

    private Database() {
        insertInitialData();
    }

    public synchronized static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void insertInitialData() {
        for (int i = 0; i < 155; i++) {
            addFakeStudent();
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Student> getStudents(int position, int count) {
        return students.subList(position, position + count);
    }



    // Debería retornar un DataSouce.Factory<Int, Student>
    // El viewmodel debería construir el LiveData<PagedList<Student>> a partir de la factoría
    // proporcionada por éste método, mediante LivePagedListBuilder(factory, tamaño_página).build()
    // El adaptador que visulice la PagedList debe ser un PagedListAdapter

    // Si queremos personalizar aún más el LivePagedListBuilder, podemos crear un objeto de configuración
    public DataSource.Factory<Integer, Student> queryPagedStudents() {
        return new DataSource.Factory<Integer, Student>() {
            @Override
            public DataSource<Integer, Student> create() {
                return new StudentDataSource(Database.this);
            }
        };
    }

    public void addFakeStudent() {
        mCount++;
        Student student = new Student(mCount, Fakeit.name().name(), Fakeit.address().streetAddress(),
                "http://lorempixel.com/100/100/abstract/" + (mCount % 10 + 1) + "/");
        students.add(student);
    }

    public void deleteStudent(int position) {
        students.remove(position);
    }

    public int getStudentsCount() {
        return students.size();
    }
}
