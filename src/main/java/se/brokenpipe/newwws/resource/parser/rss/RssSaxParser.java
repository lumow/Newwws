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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class RssSaxParser implements ResourceParser {

    private final String resource;
    private static final Logger LOGGER = Logger.getLogger(RssSaxParser.class.getName());

    public RssSaxParser(final String resource) {
        this.resource = resource;
    }

    @Override
    public void parse(InputStream is) throws ParseException {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(is);

            Channel channel = null;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    switch (event.asStartElement().getName().getLocalPart()) {
                        case Channel.IDENTIFIER:
                            channel = processChannel(eventReader);
                            channelDatabaseTransactions(channel);
                            break;
                        case Item.IDENTIFIER:
                            Item item = processItem(eventReader);
                            saveItem(item, channel);
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

    private void channelDatabaseTransactions(final Channel channel) {
        try {
            if (Database.channelExists(channel)) {
                channel.setId(Database.getChannelForLink(channel).getId());
            } else {
                Database.insertChannel(channel);
            }
            Database.deleteItemsForChannel(channel);
        } catch (DatabaseException ex) {
            LOGGER.severe("Could not insert channel [" + channel.toString() + "] cause: " + ex.getMessage());
        }
    }

    private void saveItem(final Item item, final Channel channel) {
        try {
            Database.insertItem(item, channel);
        } catch (DatabaseException ex) {
            LOGGER.severe("Could not insert item [" + item.toString() + "] cause: " + ex.getMessage());
        }
    }

    private Channel processChannel(final XMLEventReader eventReader) throws XMLStreamException {
        Channel channel = new Channel();

        int maxDepth = 1;
        int depth = 0; // depth relative to channel tag
        XMLEvent currentEvent = eventReader.nextEvent();
        String elementName = null;

        while (!isTagStartElement(Channel.IDENTIFIER, eventReader)) {
            if (currentEvent.isStartElement()) {
                depth++;
                if (depth > maxDepth) {
                    currentEvent = eventReader.nextEvent();
                    continue;
                }

                StartElement startElement = currentEvent.asStartElement();
                elementName = startElement.getName().getLocalPart();
            } else if (currentEvent.isCharacters() && !currentEvent.asCharacters().isWhiteSpace()) {
                Characters data = currentEvent.asCharacters();
                if (depth > maxDepth) {
                    currentEvent = eventReader.nextEvent();
                    continue;
                }

                if (channel.isSupported(elementName)) {
                    channel.addSubTag(elementName, data.getData());
                }
            } else if (currentEvent.isEndElement()) {
                depth--;
            }
            currentEvent = eventReader.nextEvent();
        }
        return channel;
    }

    /*
     * Process the info in the item tag.
     */
    private Item processItem(final XMLEventReader eventReader) throws XMLStreamException {
        Item item = new Item();

        while (eventReader.peek() != null && !isTagEndElement(Item.IDENTIFIER, eventReader)) {
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

    private boolean isTagStartElement(final String tag, final XMLEventReader eventReader) throws XMLStreamException {
        return isStartElement(eventReader) && isTagElement(tag, eventReader);
    }

    private boolean isTagEndElement(final String tag, final XMLEventReader eventReader) throws XMLStreamException {
        return isEndElement(eventReader) && isTagElement(tag, eventReader);
    }

    private boolean isTagElement(final String tag, final XMLEventReader eventReader) throws XMLStreamException {
        return eventReader.peek().asStartElement().getName().getLocalPart().equals(tag);
    }

    private boolean isStartElement(final XMLEventReader eventReader) throws XMLStreamException {
        return eventReader.peek().isStartElement();
    }

    private boolean isEndElement(final XMLEventReader eventReader) throws XMLStreamException {
        return eventReader.peek().isEndElement();
    }
}
