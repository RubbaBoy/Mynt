module Mynt {
    requires gson;
    requires java.sql;
    requires jdk.incubator.httpclient;
    requires MyntLogger;
    requires SimpleNet;

    exports mynt.network;
    exports mynt.network.packet.incoming.impl.status to gson;
    exports mynt.network.packet.incoming.impl.login to gson;

    opens mynt.network.packet.incoming.impl.status to gson;
    opens mynt.network.packet.incoming.impl.login to gson;
}