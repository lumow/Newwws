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
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        String line;
        try {
            while ((line = br.readLine()) != null) {
                jsonDocument.append(line);
            }
        } catch (IOException e) {
            LOGGER.warning("Couldn't read from stream: " + e.getMessage());
        }
        JSONObject jsonObject = new JSONObject(jsonDocument.toString());
        JSONArray timeSeries = jsonObject.getJSONArray("timeSeries");

        List<JSONObject> validObjects = getValidObjects(timeSeries);
        List<Forecast> forecasts = convertToForecast(validObjects);
        printForecast(forecasts);

        long endTime = System.currentTimeMillis();
        int seconds = (int) (endTime - startTime) / 1000;
        int milliseconds = (int) (endTime - startTime) % 1000;
        LOGGER.info("Parsing of resource [" + "smhi" + "] done in " + seconds + "." + milliseconds + "s");
    }

    private List<Forecast> convertToForecast(List<JSONObject> validObjects) {
        return validObjects.stream().map(Forecast::new).collect(Collectors.toList());
    }

    private void printForecast(List<Forecast> forecasts) {
        for (Forecast current : forecasts) {
            OffsetDateTime dateTime = current.getValidTime();
            System.out.println(dateTime.toString() + " (" + dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("sv-SE")) + ")");

            for (Parameter parameter : current.getParameters()) {
                ParameterType type = parameter.getType();
                String value = parameter.getValue();
                String unit = parameter.getUnit();
                if (type == ParameterType.PRECIPITATION_CATEGORY) {
                    value = PrecipitationCategory.fromKey(value).getDescription();
                    unit = "";
                } else if (type == ParameterType.WIND_DIRECTION) {
                    CardinalConverter converter = new CardinalConverter(Integer.parseInt(value));
                    value = converter.toCardinalDirection() + " (" + value + ")";
                    unit = "";
                }
                System.out.println(type.getDescription() + ": " + value + "" + unit);
            }
            System.out.println();
        }
    }

    private List<JSONObject> getValidObjects(JSONArray timeSeries) {
        List<JSONObject> list = new ArrayList<>();

        for (int i = 0; i < timeSeries.length(); i++) {
            JSONObject current = (JSONObject) timeSeries.get(i);
            OffsetDateTime currentDateTime = OffsetDateTime.parse(current.getString("validTime"));
            if (matchesTodayPlusThree(currentDateTime) && matchesInterestingTime(currentDateTime)) {
                list.add(current);
            }
        }

        return list;
    }

    private boolean matchesInterestingTime(OffsetDateTime dateTime) {
        return dateTime.getHour() == 6 || dateTime.getHour() == 12 || dateTime.getHour() == 18;
    }

    private boolean matchesTodayPlusThree(OffsetDateTime element) {
        OffsetDateTime now = OffsetDateTime.now();
        return now.getYear() == element.getYear()
                && (now.getDayOfYear() == element.getDayOfYear()
                || now.getDayOfYear() + 1 == element.getDayOfYear()
                || now.getDayOfYear() + 2 == element.getDayOfYear()
                || now.getDayOfYear() + 3 == element.getDayOfYear());
    }
}
