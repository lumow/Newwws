package se.brokenpipe.newwws.resource.parser.smhi;

import org.json.JSONObject;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-22
 */
public class Parameter {
    private String unit;
    private String value;
    private ParameterType type;

    public Parameter(JSONObject json) {
        ParameterType typeFromString = ParameterType.fromString(json.getString("name"));
        if (typeFromString == null) {
            throw new IllegalArgumentException("Parameter type not supported: " + json.getString("name"));
        }
        this.type = typeFromString;
        this.unit = json.getString("unit");
        this.value = json.getJSONArray("values").get(0).toString();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }
}
