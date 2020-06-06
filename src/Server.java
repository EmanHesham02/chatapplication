
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends UnicastRemoteObject implements ChatInterface {
    private static final long serialVersionUID = 1L;
    private ArrayList<ChatInterface> clientList;
    FileOutputStream oFile;
    ChatHistoryFile chatHistoryFile = new ChatHistoryFile();

    protected Server() throws RemoteException {
        clientList = new ArrayList<ChatInterface>();
    }

    @Override
    public synchronized boolean checkClientCredintials(ChatInterface client, String UserName, String password) throws RemoteException {
        ReadDataFile dataFile = new ReadDataFile();
        boolean usernameExists = dataFile.checkUserInFile(UserName, password);
        if (usernameExists) {
            addOnlineUserToServer(client);
        }
        return usernameExists;
    }

    public void addOnlineUserToServer(ChatInterface client) {
        clientList.add(client);
    }

    @Override
    public void removeClient(ChatInterface client) {
        clientList.remove(client);
    }

    @Override
    public synchronized void broadcastMessage(String clientname, String message) {
        try {
            for (int i = 0; i < clientList.size(); i++) {
                clientList.get(i).sendMessage(clientname.toUpperCase() + " : " + message);
            }
            oFile = chatHistoryFile.saveChatInFile(oFile, clientname.toUpperCase() + " : " + message);
            if (clientList.size() == 0) {
                chatHistoryFile.closeFile(oFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendMessage(String message) throws RemoteException {
    }


    public static void main(String[] arg) throws RemoteException, MalformedURLException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("RMIServer", new Server());
        System.out.println("server running now........\n");
        System.out.println(".....write shutdown to close server.....");
        Scanner in = new Scanner(System.in);
        String serverShutDown = in.next();
        if (serverShutDown.equalsIgnoreCase("shutdown".trim())) {
            System.exit(0);
        }
    }
}
