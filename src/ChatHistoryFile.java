import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChatHistoryFile {


    public FileOutputStream saveChatInFile(FileOutputStream oFile, String message) throws IOException {
        File file = new File("chatHistoryFile.txt");
        file.createNewFile();
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
