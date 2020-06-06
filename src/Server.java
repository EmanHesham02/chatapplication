
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ChatInterface{
    private static final long serialVersionUID = 1L;
    private ArrayList<ChatInterface> clientList;

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
    public int removeClient(ChatInterface client) {
        clientList.remove(client);
        return clientList.size();
    }

    @Override
    public synchronized void broadcastMessage(String clientname, String message) {
        for (int i = 0; i < clientList.size(); i++) {
            try {
                clientList.get(i).sendMessage(clientname.toUpperCase() + " : " + message);
            } catch (IOException e) {

            }
        }
    }

    @Override
    public synchronized void sendMessage(String message) throws RemoteException {
    }


    public static void main(String[] arg) throws RemoteException, MalformedURLException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("RMIServer", new Server());
        System.out.println("server running now........");
    }
}
