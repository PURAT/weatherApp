package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class Controller {
    private static final String startQuery = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String endQuery = "&APPID=bea2483ec382bebaea1fc2d3cb5e7951";

    @FXML
    public Button find;
    @FXML
    public TextField city;
    @FXML
    public Label temperature;
    @FXML
    public Label humidity;
    @FXML
    public Label pressure;
    @FXML
    public Label wind;
    @FXML
    public AnchorPane main_layout;

    @FXML
    void initialize() {
        find.setOnAction(event -> {
            String getCity = city.getText().trim();
            String cityEncoded = URLEncoder.encode(getCity, StandardCharsets.UTF_8);
            if (!getCity.equals("")) {
                String data = getUrlContent(startQuery + cityEncoded + endQuery);

                if (!data.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(data);
                    temperature.setText("Температура: " + (jsonObject.getJSONObject("main").getInt("temp") - 273));
                    humidity.setText("Влажность: " + jsonObject.getJSONObject("main").getInt("humidity"));
                    pressure.setText("Давление: " + (int) (jsonObject.getJSONObject("main").getDouble("pressure") * 0.76));
                    wind.setText("Ветер: " + jsonObject.getJSONObject("wind").getDouble("speed"));
                }
            } else {
                showMessage("Введите название города.");
            }
        });
    }

    public String getUrlContent(String urlAddress) {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlAddress);
            URLConnection connection = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            showMessage("Такой город не найден :(");
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    public void showMessage(String message) {
        Stage stage = (Stage) main_layout.getScene().getWindow();
        int messageTime = 1500;
        int fadeInTime = 500;
        int fadeOutTime= 500;
        Toast.makeText(stage, message, messageTime, fadeInTime, fadeOutTime);
    }
}
