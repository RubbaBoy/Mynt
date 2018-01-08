module Mynt {
    requires gson;
    requires java.sql;
    requires jdk.incubator.httpclient;
    requires MyntNet;

    exports mynt.network.packet.incoming.status to gson;
    exports mynt.network.packet.incoming.login to gson;
    opens mynt.network.packet.incoming.status to gson;
    opens mynt.network.packet.incoming.login to gson;
}