package se.brokenpipe.newwws.resource;

import se.brokenpipe.newwws.resource.parser.ParseException;

import java.io.InputStream;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public interface ResourceParser {

    void parse(InputStream is) throws ParseException;
}
