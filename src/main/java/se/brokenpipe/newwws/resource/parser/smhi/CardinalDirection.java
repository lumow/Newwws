package se.brokenpipe.newwws.resource.parser.smhi;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-02-16
 */
public enum CardinalDirection {
    NORTH(326, 34);

    private int start;
    private int stop;

    CardinalDirection(int startDegree, int stopDegree) {
        start = startDegree;
        stop = stopDegree;
    }
}
