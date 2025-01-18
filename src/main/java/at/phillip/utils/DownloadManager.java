package at.phillip.utils;
import java.io.*;
import java.net.*;

public class DownloadManager {

    public void downloadFile(String fileURL, String saveDir, String newFileName) throws IOException {
        URL url = new URL(fileURL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();

            String outputFilePath = saveDir + File.separator + newFileName;

            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();

            System.out.println("Datei erfolgreich heruntergeladen: " + outputFilePath);
        } else {
            System.out.println("Fehler beim Herunterladen der Datei. HTTP-Response-Code: " + responseCode);
        }

        connection.disconnect();
    }
}
