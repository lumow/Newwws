package se.brokenpipe.newwws.resource.parser.smhi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-14
 */
public class Forecast {
    private OffsetDateTime validTime;
    private List<Parameter> parameters;

    public Forecast(JSONObject json) {
        parameters = new ArrayList<>();
        validTime = OffsetDateTime.parse(json.getString("validTime"));

        JSONArray jsonParameters = json.getJSONArray("parameters");

        for (int i = 0; i < jsonParameters.length(); i++) {
            JSONObject jsonParameter = (JSONObject) jsonParameters.get(i);
            try {
                Parameter parameter = new Parameter(jsonParameter);
                parameters.add(parameter);
            } catch (IllegalArgumentException ex) {
                continue;
            }
        }
    }

    public OffsetDateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(OffsetDateTime validTime) {
        this.validTime = validTime;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
