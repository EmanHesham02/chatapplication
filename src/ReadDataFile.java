import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadDataFile {

    public boolean checkUserInFile(String userName, String password) {
        String filePath = "clientCredintialsfile.txt";
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(userName + "/" + password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
