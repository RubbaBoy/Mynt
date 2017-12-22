module Server {
    requires gson;
    requires guava;
    requires java.sql;
    requires MyntNet;

    exports mynt to gson;
    opens mynt to gson;
}