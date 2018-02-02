package es.iessaladillo.pedrojoya.pr210.data;

import java.util.ArrayList;
import java.util.Arrays;

public class Database {

    private ArrayList<String> items = null;

    private static Database ourInstance;

    public static synchronized Database getInstance() {
        if (ourInstance == null) {
            ourInstance = new Database();
        }
        return ourInstance;
    }

    private Database() {
        items = new ArrayList<>(
                Arrays.asList("Baldomero", "Sergio", "Pablo", "Rodolfo", "Atanasio", "Gervasio",
                        "Prudencia", "Oswaldo", "Gumersindo", "Gerardo", "Rodrigo", "Óscar"));
    }

    public ArrayList<String> getItems() {
        return items;
    }

}
