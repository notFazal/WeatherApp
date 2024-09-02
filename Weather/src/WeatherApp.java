import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.json.JSONObject;

public class WeatherApp extends JFrame {

    private JTextField searchField;
    private JLabel weatherInfoLabel;

    public WeatherApp() {
        // Set up the frame
        setTitle("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(60, 63, 65)); // Dark gray background

        // Add a header panel with an application title
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 450, 70);
        headerPanel.setBackground(new Color(33, 150, 243)); // Blue header
        add(headerPanel);

        JLabel headerLabel = new JLabel("Weather App");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Add search field
        searchField = new JTextField();
        searchField.setBounds(30, 100, 300, 50);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        searchField.setMargin(new Insets(10, 10, 10, 10));
        searchField.setBackground(new Color(45, 45, 48)); // Darker background for text field
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE); // Caret color for text field
        add(searchField);

        // Add search button with an icon
        JButton searchButton = new JButton(loadImage("541346-20.png")); // Adjust the path to your button icon image
        searchButton.setBounds(340, 100, 75, 50);
        searchButton.setBackground(new Color(33, 150, 243)); // Blue button
        searchButton.setBorder(null);
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setOpaque(true);
        add(searchButton);

        // Add a label to display weather information
        weatherInfoLabel = new JLabel();
        weatherInfoLabel.setBounds(30, 170, 380, 300);
        weatherInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        weatherInfoLabel.setForeground(Color.WHITE);
        weatherInfoLabel.setVerticalAlignment(JLabel.TOP);
        add(weatherInfoLabel);

        // Set action for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getWeatherData(searchField.getText());
            }
        });

        setVisible(true);
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't find the image file: " + resourcePath);
            return null;
        }
    }

    private void getWeatherData(String userInput) {
        String apiKey = "62bdb652123fcace0863fabdadca4c92";
        String urlString;

        try {
            // Check if the input is a ZIP code 
            if (userInput.matches("\\d{5}")) {
                // Input is a ZIP code
                urlString = String.format(
                        "http://api.openweathermap.org/data/2.5/weather?zip=%s,US&appid=%s&units=metric", userInput, apiKey);
            } else {
                // Input is assumed to be a city name 
                String cityName = userInput.split(",")[0].trim(); // Get city name
                String stateCode = userInput.contains(",") ? userInput.split(",")[1].trim() : ""; // Get state code if provided

                // Encode city name to handle spaces and special characters
                cityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());

                if (!stateCode.isEmpty()) {
                    urlString = String.format(
                            "http://api.openweathermap.org/data/2.5/weather?q=%s,%s,US&appid=%s&units=metric",
                            cityName, stateCode, apiKey);
                } else {
                    urlString = String.format(
                            "http://api.openweathermap.org/data/2.5/weather?q=%s,US&appid=%s&units=metric",
                            cityName, apiKey);
                }
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Check if connection is made
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

                // Parse JSON and display the weather information
                parseAndDisplayWeather(response.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "The URL is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "There was an IO exception.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parseAndDisplayWeather(String responseBody) {
        JSONObject jsonObject = new JSONObject(responseBody);
        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");

        String weatherMessage = String.format(
                "<html><strong>City:</strong> %s<br><strong>Temperature:</strong> %.2f°C<br><strong>Feels Like:</strong> %.2f°C<br><strong>Humidity:</strong> %d%%</html>",
                cityName, temp, feelsLike, humidity);

        weatherInfoLabel.setText(weatherMessage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp();
            }
        });
    }
}
