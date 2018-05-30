package mynt.network.packet.incoming;

import mynt.network.MyntClient;

@FunctionalInterface
public interface PacketIncoming {

    void read(MyntClient client);

}