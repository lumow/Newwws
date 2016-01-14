package se.brokenpipe.newwws.resource.parser.rss;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.brokenpipe.newwws.database.Database;
import se.brokenpipe.newwws.database.DatabaseException;
import se.brokenpipe.newwws.database.tables.Channel;
import se.brokenpipe.newwws.database.tables.Item;
import se.brokenpipe.newwws.resource.ResourceParser;
import se.brokenpipe.newwws.resource.parser.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-04
 */
public class RssDomParser implements ResourceParser {

    private static final Logger LOGGER = Logger.getLogger(RssDomParser.class.getName());
    private final String resource;
    private final DocumentBuilder documentBuilder;

    public RssDomParser(final String resource) {
        this.resource = resource;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.severe("Could not create XML document builder: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void parse(InputStream is) throws ParseException {
        try {
            LOGGER.info("Starting to parse resource [" + resource + "]");
            long startTime = System.currentTimeMillis();
            Document document = documentBuilder.parse(is);
            Channel channel = handleChannels(document);
            deleteItemsForChannel(channel);
            handleItems(document, channel);
            long endTime = System.currentTimeMillis();
            int seconds = (int) (endTime - startTime) / 1000;
            int milliseconds = (int) (endTime - startTime) % 1000;
            LOGGER.info("Parsing of resource [" + resource + "] done in " + seconds + "." + milliseconds + "s");
        } catch (SAXException | IOException e) {
            throw new ParseException("Could not parse stream.", e);
        }
    }

    private void deleteItemsForChannel(Channel channel) {
        try {
            Channel realChannel = Database.getChannelForLink(channel.getLink());
            if (realChannel != null) {
                Database.deleteItemsForChannel(realChannel);
            }
        } catch (DatabaseException ex) {
            LOGGER.severe("Could not delete items for channel [" + channel.toString() + "] cause: " + ex.getMessage());
        }
    }

    private Channel handleChannels(Document document) {
        Channel channel = null;
        NodeList channelList = document.getElementsByTagName(Channel.IDENTIFIER);
        for (int i = 0; i < channelList.getLength(); i++) {
            Node channelNode = channelList.item(i);
            channel = new Channel();
            NodeList children = channelNode.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if (channel.isSupported(child.getNodeName())) {
                    if (child.getFirstChild() instanceof CharacterData) {
                        CharacterData cd = (CharacterData) child.getFirstChild();
                        channel.addSubTag(child.getNodeName(), cd.getData());
                    } else {
                        channel.addSubTag(child.getNodeName(), child.getNodeValue());
                    }
                }
            }
            Channel dbChannel = Database.getChannelForLink(channel.getLink());
            if (dbChannel == null) {
                insertChannel(channel);
            } else {
                channel.setId(dbChannel.getId());
            }
        }
        return channel;
    }

    private void insertChannel(Channel channel) {
        try {
            Database.insertChannel(channel);
        } catch (DatabaseException ex) {
            LOGGER.severe("Could not insert channel [" + channel.toString() + "] cause: " + ex.getMessage());
        }
    }

    private void handleItems(Document document, Channel channel) {
        NodeList itemList = document.getElementsByTagName(Item.IDENTIFIER);
        for (int i = 0; i < itemList.getLength(); i++) {
            Node itemNode = itemList.item(i);
            Item item = new Item();
            NodeList children = itemNode.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if (item.isSupported(child.getNodeName())) {
                    if (child.getFirstChild() instanceof CharacterData) {
                        CharacterData cd = (CharacterData) child.getFirstChild();
                        item.addSubTag(child.getNodeName(), cd.getData());
                    } else {
                        item.addSubTag(child.getNodeName(), child.getNodeValue());
                    }
                }
            }
            insertItem(channel, item);
        }
    }

    private void insertItem(Channel channel, Item item) {
        try {
            Database.insertItem(item, channel);
        } catch (DatabaseException ex) {
            LOGGER.severe("Could not insert item [" + item.toString() + "] cause: " + ex.getMessage() + ex.getCause().getMessage());
        }
    }
}
