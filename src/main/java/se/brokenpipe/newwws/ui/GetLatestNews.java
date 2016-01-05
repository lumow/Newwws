package se.brokenpipe.newwws.ui;

import org.eclipse.jetty.server.Server;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-04
 */
public class GetLatestNews {

    public static void main(String[] args) throws Exception {
        Server server = new Server(80);
        server.setHandler(new MyHandler());
        server.start();
        server.join();

        /*Database.setup();

        List<Channel> allChannels = Database.getAllChannels();
        for (Channel channel : allChannels) {
            List<Item> items = Database.getItemsForChannel(channel);
            System.out.println("Channel: " + channel.getTitle());
            for (int i = 0; i < 10; i++) {
                Item item = items.get(i);
                System.out.print("Item: ");
                System.out.println(item.getTitle());
                System.out.print("Link: ");
                System.out.println(item.getLink());
            }
        }
        */
    }
}
