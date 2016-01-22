package se.brokenpipe.newwws.resource.parser.smhi;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-22
 */
public class CardinalConverter {
    private final int degree;

    public CardinalConverter(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public String toCardinalDirection() {
        if ((degree > 326 && degree <= 360) || (degree >= 0 && degree <= 34))
            return "N";
        else if (degree > 34 && degree <= 146)
            return "E";
        else if (degree > 146 && degree <= 236)
            return "S";
        else if (degree > 236 && degree <= 326)
            return "W";
        else
            throw new IllegalArgumentException("Illegal degree: has to be between 0 and 360.");
    }
}
