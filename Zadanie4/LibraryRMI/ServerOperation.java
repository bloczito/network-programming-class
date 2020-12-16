
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerOperation extends UnicastRemoteObject implements RMIInterface {

    public static void main(String[] args) {

        try {
            Naming.rebind("//localhost:1099/MyServer", new ServerOperation());
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    protected ServerOperation() throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String name, String surname, String pesel, char[] passw) throws RemoteException {

        String password = new String(passw);

        if (name.equals("")) {
            String result = "";
            List<Book> ksiazki = b.getBooks(pesel, password);
            if (!(ksiazki == null)) {
                for (int i = 0; i < ksiazki.size(); i++) {
                    result = result + "\n" + ksiazki.get(i);
                }
            }
            return result;
        } else {
            System.err.println(name + " is trying to contact!");
            b.addReader(name, surname, pesel, password);
            return "Rejestracja " + name + " " + surname + " udana!";
        }
    }

    private static final long serialVersionUID = 1L;
    Library b = new Library();
}
