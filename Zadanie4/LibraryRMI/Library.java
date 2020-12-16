import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Library {

    public Library() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(
                    "jdbc:hsqldb:file:/opt/db/testdb;ifexists=true", "SA", "");
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
        createTable();
    }

    public boolean createTable()  {
        String Readers = "CREATE TABLE IF NOT EXISTS czytelnicy (id_czytelnika INTEGER PRIMARY KEY AUTOINCREMENT, imie varchar(255), nazwisko varchar(255), pesel int,haslo varchar(255))";
        String Books = "CREATE TABLE IF NOT EXISTS ksiazki (id_ksiazki INTEGER PRIMARY KEY AUTOINCREMENT, tytul varchar(255), autor varchar(255))";
        String Rentals = "CREATE TABLE IF NOT EXISTS wypozyczenia (id_wypozycz INTEGER PRIMARY KEY AUTOINCREMENT, id_czytelnika int, id_ksiazki int)";
        try {
            stat.execute(Readers);
            stat.execute(Books);
            stat.execute(Rentals);
        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addReader(String name, String surname, String pesel, String password) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into czytelnicy values (NULL, ?, ?, ?,?);");
            prepStmt.setString(1, name);
            prepStmt.setString(2, surname);
            prepStmt.setString(3, pesel);
            prepStmt.setString(4, password);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("Blad przy wstawianiu czytelnika");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertBook(String title, String author) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into ksiazki values (NULL, ?, ?);");
            prepStmt.setString(1, title);
            prepStmt.setString(2, author);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("Blad przy wypozyczaniu");
            return false;
        }
        return true;
    }

    public boolean insertWypozycz(int ReaderID, int BookID) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into wypozyczenia values (NULL, ?, ?);");
            prepStmt.setInt(1, ReaderID);
            prepStmt.setInt(2, BookID);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("Blad przy wypozyczaniu");
            return false;
        }
        return true;
    }

    public List<Reader> getReader() {
        List<Reader> readersList = new LinkedList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM czytelnicy");
            int id;
            String name, surname, pesel,password;
            while(result.next()) {
                id = result.getInt("id_czytelnika");
                name = result.getString("imie");
                surname = result.getString("nazwisko");
                pesel = result.getString("pesel");
                password = result.getString("haslo");
                readersList.add(new Reader(id, name, surname, pesel,password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return readersList;
    }

    public List<Book> getBooksFromDB(List<Integer>rented) {
        List<Book> ksiazki = new LinkedList<Book>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM ksiazki");
            int id;
            String tytul, autor;
            while(result.next()) {
                id = result.getInt("id_ksiazki");
                if(rented.contains(id)) {
                    tytul = result.getString("tytul");
                    autor = result.getString("autor");
                    ksiazki.add(new Book(id, tytul, autor));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return ksiazki;
    }

    public int passwordCheck(String typedPesel, String typedPassword) {
        int id = -1;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM czytelnicy");

            String pesel, password;
            while (result.next()) {
                pesel = result.getString("pesel");
                password = result.getString("haslo");
                if (password.equals(typedPassword) && pesel.equals(typedPesel))
                    id = result.getInt("id_czytelnika");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<Book> getBooks(String sel, String pas)
    {
        int usr= passwordCheck(sel,pas);
        if(usr==-1)
            return null;
        List<Integer> rentedBooksIDs = new ArrayList<Integer>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM wypozyczenia");
            int readerID, bookID;

            while(result.next()) {
                readerID = result.getInt("id_czytelnika");
                bookID = result.getInt("id_ksiazki");
                if(readerID == usr)
                    rentedBooksIDs.add(bookID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return getBooksFromDB(rentedBooksIDs);
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Problem z zamknieciem polaczenia");
            e.printStackTrace();
        }
    }

    private Connection conn;
    private Statement stat;
}


class Reader {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public Reader() {}

    public Reader(int id, String name, String surname, String pesel, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.PESEL = pesel;
        this.password =password;
    }

    @Override
    public String toString() { return "["+id+"] - "+ name +" "+ surname +" - "+ PESEL; }

    private int id;
    private String name;
    private String surname;
    private String PESEL;
    private String password;

}

class Book {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Book() {}

    public Book(int id, String tytul, String autor) {
        this.id = id;
        this.title = tytul;
        this.author = autor;
    }
    @Override
    public String toString() {
        return "["+id+"] - "+ title +" - "+ author;
    }

    private int id;
    private String title;
    private String author;
}