import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Vector;

public class Room {

    public Room(int nr) {
        opcja = nr;
        runRoom();
    }

    public void addUser(String nick) {
        users.add(nick);
    }

    public void addMessage(String message) {
        wiadomosci.add(message);
    }

    public String mesages() {
        StringBuilder total = new StringBuilder();
        for (int index = 0; index < wiadomosci.size(); index++)
            total.append(wiadomosci.get(index) + "\n");
        return total.toString();
    }

    public void runRoom() {
        try {
            String adres = adr.substring(0, adr.length() - 1) + (opcja);
            System.out.println("adres to " + adres);
            group = InetAddress.getByName(adres); //Adres grupy multicast UWAGA!!! Sprawdzić jaka jest adresacja dla tachi grup.

            socket = new MulticastSocket(port); //tworzenie podłaczenia pod multicastową grupę
            socket.setTimeToLive(0); //dla sieci lokalnych 0 dla dla podsieci 1.
            socket.joinGroup(group); //dołaączanie do grupy milticast
            Thread t = new Thread(new ReadThread(socket, group, port, this));
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String mesage, String name) throws IOException {

        String message = name + ": " + mesage;
        message = message.replace('+', ' ');

        addMessage(message);
        byte[] buffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer, 0, buffer.length, group, port);
        socket.send(datagram);
        System.out.println(message);

    }

    static InetAddress group;
    static int port = 10;
    static String adr = "226.225.225.225";
    static MulticastSocket socket;
    static int opcja;
    public Vector<String> wiadomosci = new Vector<>();
    public Vector<String> users = new Vector<>();
}


class ReadThread implements Runnable {

    ReadThread(MulticastSocket socket, InetAddress group, int port, Room grc) {
        this.socket = socket;
        this.group = group;
        this.port = port;
        rm = grc;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[ReadThread.MAX_LEN];
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
        String message = "";
        while (true) {
            try {
                socket.receive(datagram);
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!(message == null) || !(message.length() == 0)) {
                    if (message.contains("zakoncz")) {
                        int iend = message.indexOf(":");
                        String nick = "----";
                        if (iend != -1) {
                            nick = message.substring(0, iend); //this will give abc
                        }
                        rm.addMessage(ANSI_RED + "Uzytkownik " + nick + " opuscil chat" + ANSI_RED);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        }

    }

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;

    public static final String ANSI_RED = "\u001B[31m";
    Room rm;
}
