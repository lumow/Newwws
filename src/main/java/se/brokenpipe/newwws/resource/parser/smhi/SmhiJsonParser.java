package se.brokenpipe.newwws.resource.parser.smhi;

import org.json.JSONArray;
import org.json.JSONObject;
import se.brokenpipe.newwws.resource.ResourceParser;
import se.brokenpipe.newwws.resource.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-14
 */
public class SmhiJsonParser implements ResourceParser {

    private static final Logger LOGGER = Logger.getLogger(SmhiJsonParser.class.getName());

    @Override
    public void parse(InputStream is) throws ParseException {
        LOGGER.info("Starting to parse resource [" + "smhi" + "]");
        long startTime = System.currentTimeMillis();

        StringBuilder jsonDocument = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                jsonDocument.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(jsonDocument.toString());
        JSONArray timeSeries = jsonObject.getJSONArray("timeSeries");

        List<JSONObject> forecast = getValidObjects(timeSeries);
        printForecast(forecast);

        long endTime = System.currentTimeMillis();
        int seconds = (int) (endTime - startTime) / 1000;
        int milliseconds = (int) (endTime - startTime) % 1000;
        LOGGER.info("Parsing of resource [" + "smhi" + "] done in " + seconds + "." + milliseconds + "s");
    }

    private void printForecast(List<JSONObject> forecast) {
        for (JSONObject current : forecast) {
            String validTime = current.getString("validTime");
            JSONArray parameters = current.getJSONArray("parameters");
            for (int i = 0; i < parameters.length(); i++) {
                JSONObject parameter = (JSONObject) parameters.get(i);
                if (parameter.getString("name").equals("t")) {
                    double temperature = parameter.getJSONArray("values").getDouble(0);
                    System.out.print(OffsetDateTime.parse(validTime).toString() + ": ");
                    System.out.print(temperature);
                    System.out.println(" deg C");
                }
            }
        }
    }

    private List<JSONObject> getValidObjects(JSONArray timeSeries) {
        List<JSONObject> list = new ArrayList<>();

        for (int i = 0; i < timeSeries.length(); i++) {
            JSONObject current = (JSONObject) timeSeries.get(i);
            OffsetDateTime currentDateTime = OffsetDateTime.parse(current.getString("validTime"));
            if (matchesTodayPlusTwo(currentDateTime)) {
                list.add(current);
            }
        }

        return list;
    }

    private boolean matchesTodayPlusTwo(OffsetDateTime element) {
        OffsetDateTime now = OffsetDateTime.now();
        return now.getYear() == element.getYear()
                && (now.getDayOfYear() == element.getDayOfYear()
                || now.getDayOfYear() + 1 == element.getDayOfYear()
                || now.getDayOfYear() + 2 == element.getDayOfYear());
    }
}
