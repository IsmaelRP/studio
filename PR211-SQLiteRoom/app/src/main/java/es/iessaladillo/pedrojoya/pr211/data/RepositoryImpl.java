package es.iessaladillo.pedrojoya.pr211.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import es.iessaladillo.pedrojoya.pr211.Constants;
import es.iessaladillo.pedrojoya.pr211.data.local.AppDatabase;
import es.iessaladillo.pedrojoya.pr211.data.model.Student;
import es.iessaladillo.pedrojoya.pr211.utils.DatabaseUtils;

public class RepositoryImpl implements Repository {

    private static RepositoryImpl instance;

    private final AppDatabase db;

    private RepositoryImpl(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, Constants.DATABASE_NAME).addCallback(
                new RoomDatabase.Callback() {
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        DatabaseUtils.executeSqlFromAssetsFile(db, db.getVersion(), context.getAssets());
                    }
                }).build();
    }

    public static synchronized RepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new RepositoryImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<Student>> getStudents() {
        return db.studentDao().getStudents();
    }

    @Override
    public LiveData<Student> getStudent(long studentId) {
        return db.studentDao().getStudent(studentId);
    }

    @Override
    public long addStudent(Student student) {
        return db.studentDao().insertStudent(student);
    }

    @Override
    public int updateStudent(Student student) {
        return db.studentDao().updateStudent(student);
    }

    @Override
    public int deleteStudent(Student student) {
        return db.studentDao().deleteStudent(student);
    }

}
