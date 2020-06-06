import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ChatHistoryFile {


    public FileOutputStream saveChatInFile(FileOutputStream oFile, String message) throws IOException {
        String userName[] = message.split(":");
        File file = new File(userName[0].toString() + ".txt");
        file.createNewFile(); // if file already exists will do nothing
        oFile = new FileOutputStream(file, true);
        byte messageByte[] = (message + "\n").getBytes();
        oFile.write(messageByte);

        return oFile;

    }

    public void closeFile(FileOutputStream oFile) throws IOException {
        try {
            if (oFile != null) {
                oFile.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error while closing file chat history: ");
        }
    }
}
