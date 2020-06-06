
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ChatInterface, Runnable {
    private static final long serialVersionUID = 1L;
    private ChatInterface server;
    private String ClientName;
    boolean chkExit = true;
    boolean chkLog ;
    FileOutputStream oFile;
    ChatHistoryFile chatHistoryFile = new ChatHistoryFile();

    protected Client(ChatInterface chatInterface, String clientname, String password) throws RemoteException {
        this.server = chatInterface;
        this.ClientName = clientname;
        chkLog = server.checkClientCredintials(this, clientname, password);
    }


    public void sendMessage(String message) throws IOException {
        System.out.println(message);
        oFile = chatHistoryFile.saveChatInFile(oFile, message);
    }

    @Override
    public int removeClient(ChatInterface client) {
        return 0;
    }

    @Override
    public boolean checkClientCredintials(ChatInterface ci, String name, String pass) throws RemoteException {
        return false;
    }

    @Override
    public void broadcastMessage(String name, String message) throws RemoteException {
    }

    @Override
    public void run() {
        if (chkLog) {
            System.out.println("Successfully Connected To Server");
            System.out.println("NOTE : Type Bye Bye to Exit From The Service");
            System.out.println("Now Your Online Now\n");
            Scanner scanner = new Scanner(System.in);
            String message;
            while (chkExit) {
                message = scanner.nextLine();
                try {
                    if (message.equalsIgnoreCase("Bye Bye")) {
                        server.broadcastMessage(ClientName, message);
                        chkExit = false;
                        int clintList = server.removeClient(this);
                        if (clintList == 0) {
                            chatHistoryFile.closeFile(oFile);
                            System.out.println("\nSuccessfully Logout From Chat \nThank You For Using...");
                            System.exit(0);
                        }
                    } else {
                        server.broadcastMessage(ClientName, message);
                    }
                } catch (IOException ioException) {
                }
            }
        } else {
            System.out.println("\n UserName or Password Incorrect...");
        }
    }

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        String UserName = "";
        String UserPassword = "";

        System.out.println("\n~~ Welcome To Chat Program ~~\n");
        System.out.print("Enter Your Name : ");
        UserName = scanner.nextLine();
        System.out.print("Enter Your Password : ");
        UserPassword = scanner.nextLine();
        System.out.println("\nConnecting To Server...\n");

        ChatInterface chatInterface = (ChatInterface) Naming.lookup("rmi://localhost/RMIServer");
        new Thread(new Client(chatInterface, UserName, UserPassword)).start();
    }
}
