import javax.swing.SwingUtilities;
import java.io.*;
import java.net.*;

public class main {

    public static void main(String[] args) {

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=62bdb652123fcace0863fabdadca4c92");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                System.out.println("Response: " + response.toString()); // Output the result
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("The URL is not valid.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an IO exception.");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp();
            }
        });
    }
}
