module Mynt {
    requires gson;
    requires java.sql;
    requires jdk.incubator.httpclient;
    requires MyntNet;

    exports mynt.network.packet.status to gson;
    opens mynt.network.packet.status to gson;
}