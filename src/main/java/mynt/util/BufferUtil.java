package mynt.util;

import java.nio.charset.Charset;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import simplenet.Client;
import simplenet.packet.Packet;

public final class BufferUtil {

    private BufferUtil() {
        throw new UnsupportedOperationException("Class should not be instantiated!");
    }

    public static void putBoolean(boolean b, Packet packet) {
        packet.putByte(b ? 1 : 0);
    }

    public static void readBoolean(Client client, Consumer<Boolean> consumer) {
        client.readByte(b -> consumer.accept(b == 1));
    }

    public static void putString(String s, Packet packet) {
        putVarInt(s.length(), packet);
        packet.putBytes(s.getBytes());
    }

    public static void readString(Client client, Consumer<String> consumer) {
        readVarInt(client, i -> {
            client.read(i, payload -> {
                byte[] b = new byte[i];
                payload.get(b);
                consumer.accept(new String(b));
            });
        });
    }

    /**
     * Reads a {@link String} encoded in UTF-16.
     */
    public static void readStringUTF16(Client client, Charset charset, Consumer<String> consumer) {
        client.readShort(s -> {
            client.read(s, payload -> {
                byte[] b = new byte[s << 1];
                payload.get(b);
                consumer.accept(new String(b, charset));
            });
        });
    }

    public static void putVarInt(int value, Packet packet) {
        putVarLong(value, packet);
    }

    public static void putVarLong(long value, Packet packet) {
        do {
            byte temp = (byte) (value & 0b01111111);

            value >>>= 7;

            if (value != 0) {
                temp |= 0b10000000;
            }

            packet.putByte(temp);
        } while (value != 0);
    }

    public static void readVarIntAlways(Client client, IntConsumer consumer) {
        readVarInt(client, new IntConsumer() {
            @Override
            public void accept(int value) {
                consumer.accept(value);
                readVarInt(client, this);
            }
        });
    }

    public static void readVarInt(Client client, IntConsumer consumer) {
        client.readByte(b1 -> {
            if ((b1 & 0b10000000) == 0) {
                consumer.accept(b1);
                return;
            }

            client.readByte(b2 -> {
                if ((b2 & 0b10000000) == 0) {
                    consumer.accept(b2 << 7 | (b1 & 127));
                    return;
                }

                client.readByte(b3 -> {
                    if ((b3 & 0b10000000) == 0) {
                        consumer.accept(b3 << 14 | (b2 & 127) << 7 | (b1 & 127));
                        return;
                    }

                    client.readByte(b4 -> {
                        if ((b4 & 0b10000000) == 0) {
                            consumer.accept(b4 << 21 | (b3 & 127) << 14 | (b2 & 127) << 7 | (b1 & 127));
                            return;
                        }

                        client.readByte(b5 -> {
                            if ((b5 & 0b10000000) != 0) {
                                throw new RuntimeException("VarInt is too big");
                            }

                            consumer.accept(b5 << 28 | (b4 & 127) << 21 | (b3 & 127) << 14 | (b2 & 127) << 7 | b1 & 127);
                        });
                    });
                });
            });
        });
    }

    public static void readVarLong(Client client, LongConsumer consumer) {
        client.readByte(b1 -> {
            if ((b1 & 0b10000000) == 0) {
                consumer.accept(b1);
                return;
            }

            client.readByte(b2 -> {
                if ((b2 & 0b10000000) == 0) {
                    consumer.accept(b2 << 7 | b1 & 127);
                    return;
                }

                client.readByte(b3 -> {
                    if ((b3 & 0b10000000) == 0) {
                        consumer.accept(b3 << 14 | (b2 & 127) << 7 | b1 & 127);
                        return;
                    }

                    client.readByte(b4 -> {
                        if ((b4 & 0b10000000) == 0) {
                            consumer.accept(b4 << 21 | (b3 & 127) << 14 | (b2 & 127) << 7 | b1 & 127);
                            return;
                        }

                        client.readByte(b5 -> {
                            if ((b5 & 0b10000000) == 0) {
                                consumer.accept(b5 << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                return;
                            }

                            client.readByte(b6 -> {
                                if ((b6 & 0b10000000) == 0) {
                                    consumer.accept((b6 & 127L) << 35 | (b5 & 127L) << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                    return;
                                }

                                client.readByte(b7 -> {
                                    if ((b7 & 0b10000000) == 0) {
                                        consumer.accept((b7 & 127L) << 42 | (b6 & 127L) << 35 | (b5 & 127L) << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                        return;
                                    }

                                    client.readByte(b8 -> {
                                        if ((b8 & 0b10000000) == 0) {
                                            consumer.accept((b8 & 127L) << 49 | (b7 & 127L) << 42 | (b6 & 127L) << 35 | (b5 & 127L) << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                            return;
                                        }

                                        client.readByte(b9 -> {
                                            if ((b9 & 0b10000000) == 0) {
                                                consumer.accept((b9 & 127L) << 56 | (b8 & 127L) << 49 | (b7 & 127L) << 42 | (b6 & 127L) << 35 | (b5 & 127L) << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                            }

                                            client.readByte(b10 -> {
                                                if ((b10 & 0b10000000) != 0) {
                                                    throw new RuntimeException("VarLong is too big");
                                                }

                                                consumer.accept((b10 & 127L) << 63 | (b9 & 127L) << 56 | (b8 & 127L) << 49 | (b7 & 127L) << 42 | (b6 & 127L) << 35 | (b5 & 127L) << 28 | (b4 & 127L) << 21 | (b3 & 127L) << 14 | (b2 & 127L) << 7 | b1 & 127L);
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }

}
