package se.brokenpipe.newwws.resource.parser.rss;

import se.brokenpipe.newwws.database.Database;
import se.brokenpipe.newwws.database.DatabaseException;
import se.brokenpipe.newwws.database.tables.Channel;
import se.brokenpipe.newwws.database.tables.Item;
import se.brokenpipe.newwws.resource.ResourceParser;
import se.brokenpipe.newwws.resource.parser.ParseException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class RssParser implements ResourceParser {

    private final String resource;
    private final Logger LOGGER = Logger.getLogger(RssParser.class.getName());

    public RssParser(final String resource) {
        this.resource = resource;
    }

    @Override
    public void parse(InputStream is) throws ParseException {
        List<Item> items = new ArrayList<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(is);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    switch (event.asStartElement().getName().getLocalPart()) {
                        case Channel.IDENTIFIER:
                            Channel channel = processChannel(eventReader);
                            break;
                        case Item.IDENTIFIER:
                            Item item = processItem(eventReader);
                            try {
                                Database.insertItem(item);
                            } catch (DatabaseException ex) {
                                LOGGER.severe("Could not insert item [" + item.toString() + "] cause: " + ex.getMessage());
                            }
                            break;
                        default:
                            // Unsupported tag
                            break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new ParseException("Failed to parse document", e);
        }
        LOGGER.info("Parsing of resource [" + resource + "] done");
    }

    /*
     * Process the info in the channel tag.
     */
    private Channel processChannel(final XMLEventReader eventReader) throws XMLStreamException {
        Channel channel = new Channel();
        while (eventReader.peek() != null
                && eventReader.peek().isStartElement()
                && channel.isSupported(eventReader.peek().asStartElement().getName().getLocalPart())) {

            XMLEvent newEvent = eventReader.nextEvent();
            String tagName = newEvent.asStartElement().getName().getLocalPart();
            newEvent = eventReader.nextEvent();
            if (newEvent.isCharacters()) {
                channel.addSubTag(tagName, newEvent.asCharacters().getData());
                eventReader.nextEvent();
            }
        }
        return channel;
    }

    /*
     * Process the info in the item tag.
     */
    private Item processItem(final XMLEventReader eventReader) throws XMLStreamException {
        Item item = new Item();

        while (eventReader.peek() != null &&
                !(eventReader.peek().isEndElement() && eventReader.peek().asEndElement().getName().getLocalPart().equals(Item.IDENTIFIER))) {
            if (eventReader.peek().isStartElement() && item.isSupported(eventReader.peek().asStartElement().getName().getLocalPart())) {
                XMLEvent newEvent = eventReader.nextEvent();
                String tagName = newEvent.asStartElement().getName().getLocalPart();
                newEvent = eventReader.nextEvent();
                if (newEvent.isCharacters()) {
                    item.addSubTag(tagName, newEvent.asCharacters().getData());
                }
                // fast forward to end tag for this supported tag
                while (!(eventReader.peek().isEndElement() && eventReader.peek().asEndElement().getName().getLocalPart().equals(tagName))) {
                    eventReader.nextEvent();
                }
            } else {
                eventReader.nextEvent();
            }
        }
        eventReader.nextEvent();

        return item;
    }
}
