import java.io.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class MyHttpServer implements HttpHandler {

    Room r0 = new Room(0);
    Room r1 = new Room(1);
    Room r2 = new Room(2);


    @Override
    public void handle(HttpExchange t) throws IOException {

        String response = "<!DOCTYPE html>\n" +
                "<html>" +
                " <body style='background-color: #46b9a6;'> " +
                "<h2>Logowanie</h2>" +
                "<form action='/messenger'>" +
                "  <label for='fname'>Nazwa:</label><br>" +
                "  <input type='text' id='nick' name='nick'><br><br>" +
                "<select id='rooms' name='room'>" +
                "  <option value='0'>Party 0</option>" +
                "  <option value='1'>Party 1</option>" +
                "  <option value='2'>Party 2</option>" +
                "</select>" +
                "  <input type='submit' value='Zatwierdz'>" +
                "</form>" +
                "</body>" +
                "</html>";

        String query = t.getRequestURI().getQuery(); //Podgladając to metodę moga państwo podejrzeć gdzić jakie zmienne przychodzą w pasku adresu.
        String path = t.getRequestURI().getPath();

        if (path.equals("/messenger")) {
            if (query.contains("nick") && query.contains("room")) {
                int startIndex = query.indexOf("=");
                int endIndex = query.indexOf("&");

                String nick = "----";
                String roomNumber;
                if ((endIndex != -1) && (startIndex != -1)) {
                    nick = query.substring((startIndex + 1), endIndex);
                    roomNumber = query.substring((query.length() - 1));
                    int roomNr = Integer.parseInt(roomNumber);

                    switch (roomNr) {
                        case 0:
                            r0.addUser(nick);
                            break;
                        case 1:
                            r1.addUser(nick);
                            break;
                        case 2:
                            r2.addUser(nick);
                            break;
                        default:
                            System.err.println("nie ma takiego pokoju");
                            break;
                    }
                }

                response = "<!DOCTYPE html> " +
                        "<html>" +
                        " <body style='background-color: #46b9a6;'> " +
                        "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                        "<pre>" +
                        "Witaj!" +
                        "</pre>" +
                        "</div> " +
                        "<p>Wyslij wiadomosc:</p> " +
                        "<form action='/" + nick + "/messenger'>" +
                        " <textarea name='message' rows='2' cols='50'></textarea> " +
                        "<br> " +
                        "<input type='submit'> " +
                        "</form> " +
                        "<meta http-equiv='refresh' content='100' >" +
                        "</body> " +
                        "</html>";
            } else if (query == null) {

                response = "<!DOCTYPE html> " +
                        "<html>" +
                        " <body style='background-color: #46b9a6;'> " +
                        "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                        "<pre>" +
                        "</pre>" +
                        "</div> " +
                        "<p>Wyslij wiadomosc:</p> " +
                        "<form action='/messenger'>" +
                        " <textarea name='message' rows='2' cols='50'></textarea> " +
                        "<br> " +
                        "<input type='submit'> " +
                        "</form> " +
                        "<meta http-equiv='refresh' content='100' >" +
                        "</body> " +
                        "</html>";
            } else if (query.contains("message")) {
                int startIndex = path.indexOf("/");
                int endIndex = path.indexOf("/", startIndex + 1);
                String nick = "----";
                if ((endIndex != -1) && (startIndex != -1)) {
                    nick = path.substring((startIndex + 1), endIndex);
                }
                startIndex = query.indexOf("=");
                String message = "----";

                if ((startIndex != -1)) {
                    message = query.substring((startIndex + 1));
                }

                if (r0.users.contains(nick)) {
                    r0.sendMessage(message, nick);
                    response = "<!DOCTYPE html> " +
                            "<html>" +
                            " <body style='background-color: #46b9a6;'> " +
                            "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                            "<pre>" +
                            r0.mesages() +
                            "</pre>" +
                            "</div> " +
                            "<p>Wyslij wiadomosc:</p> " +
                            "<form action='/messenger'>" +
                            " <textarea name='message' rows='2' cols='50'></textarea> " +
                            "<br> " +
                            "<input type='submit'> " +
                            "</form> " +
                            "<meta http-equiv='refresh' content='100' >" +
                            "</body> " +
                            "</html>";
                }
                if (r1.users.contains(nick)) {
                    r1.sendMessage(message, nick);
                    response = "<!DOCTYPE html> " +
                            "<html>" +
                            " <body style='background-color: #46b9a6;'> " +
                            "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                            "<pre>" +
                            r1.mesages() +
                            "</pre>" +
                            "</div> " +
                            "<p>Wyslij wiadomosc:</p> " +
                            "<form action='/messenger'>" +
                            " <textarea name='message' rows='2' cols='50'></textarea> " +
                            "<br> " +
                            "<input type='submit'> " +
                            "</form> " +
                            "<meta http-equiv='refresh' content='100' >" +
                            "</body> " +
                            "</html>";
                }
                if (r2.users.contains(nick)) {
                    r2.sendMessage(message, nick);
                    response = "<!DOCTYPE html> " +
                            "<html>" +
                            " <body style='background-color: #46b9a6;'> " +
                            "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                            "<pre>" +
                            r2.mesages() +
                            "</pre>" +
                            "</div> " +
                            "<p>Wyslij wiadomosc:</p> " +
                            "<form action='/messenger'>" +
                            " <textarea name='message' rows='2' cols='50'></textarea> " +
                            "<br> " +
                            "<input type='submit'> " +
                            "</form> " +
                            "<meta http-equiv='refresh' content='100' >" +
                            "</body> " +
                            "</html>";
                }
            } else {
                response = " UPS...";
            }
        } else if (path.indexOf("/", (path.indexOf("/") + 1)) > 0) {

            String message = "q";

            if (query.contains("message")) {

                int startIndex = path.indexOf("/");
                int endIndex = path.indexOf("/", startIndex + 1);
                String nick = "----";
                String nr;
                if ((endIndex != -1) && (startIndex != -1)) {
                    nick = path.substring((startIndex + 1), endIndex);
                }
                startIndex = query.indexOf("=");
                if ((startIndex != -1)) {
                    message = query.substring((startIndex + 1));
                }
            }
            int startIndex = path.indexOf("/");
            int endIndex = path.indexOf("/", startIndex + 1);
            String nick = "----";
            if ((endIndex != -1) && (startIndex != -1)) {
                nick = path.substring((startIndex + 1), endIndex); //this will give abc
            }
            if (r0.users.contains(nick)) {
                if (!(message.equals("q")))
                    r0.sendMessage(message, nick);
                System.out.println("w zerowym");
                response = "<!DOCTYPE html> " +
                        "<html>" +
                        " <body style='background-color: #46b9a6;'> " +
                        "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                        "<pre>" +
                        r0.mesages() +
                        "</pre>" +
                        "</div> " +
                        "<p>Wyslij wiadomosc:</p> " +
                        "<form action='" + path + "'>" +
                        " <textarea name='message' rows='2' cols='50'></textarea> " +
                        "<br> " +
                        "<input type='submit'> " +
                        "</form> " +
                        "</body> " +
                        "</html>";
            } else if (r1.users.contains(nick)) {
                if (!(message.equals("q")))
                    r1.sendMessage(message, nick);
                response = "<!DOCTYPE html> " +
                        "<html>" +
                        " <body style='background-color: #46b9a6;'> " +
                        "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                        "<pre>" +
                        r1.mesages() +
                        "</pre>" +
                        "</div> " +
                        "<p>Wyslij wiadomosc:</p> " +
                        "<form action='" + path + "'>" +
                        " <textarea name='message' rows='2' cols='50'></textarea> " +
                        "<br> " +
                        "<input type='submit'> " +
                        "</form> " +
                        "</body> " +
                        "</html>";
            } else if (r2.users.contains(nick)) {
                if (!(message.equals("q")))
                    r2.sendMessage(message, nick);
                response = "<!DOCTYPE html> " +
                        "<html>" +
                        " <body style='background-color: #46b9a6;'> " +
                        "<div style='width:800px;height:400px;line-height:normal;padding:5px;background-color:#FCFADD;color:#714D03;scrollbar-base-color:#DEBB07;'>" +
                        "<pre>" +
                        r2.mesages() +
                        "</pre>" +
                        "</div> " +
                        "<p>Wyslij wiadomosc:</p> " +
                        "<form action='" + path + "'>" +
                        " <textarea name='message' rows='2' cols='50'></textarea> " +
                        "<br> " +
                        "<input type='submit'> " +
                        "</form> " +
                        "</body> " +
                        "</html>";
            }
        } else if (path.equals("/src/test")) {
            response = "jakas inna strona";
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
