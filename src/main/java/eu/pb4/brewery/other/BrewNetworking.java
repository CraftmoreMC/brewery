package eu.pb4.brewery.other;

import eu.pb4.brewery.BreweryInit;
import eu.pb4.brewery.drink.DrinkType;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

import static eu.pb4.brewery.BreweryInit.id;

public class BrewNetworking {
    public static final int PROTOCOL = 0;
    public static final Identifier HELLO_PACKET_ID = id("hello");
    public static final Identifier DEFINE_PACKET_ID = id("define");


    public static boolean hasMod(ServerPlayNetworkHandler handler) {
        return PolymerServerNetworking.getMetadata(handler, HELLO_PACKET_ID, NbtInt.TYPE) != null;
    }

    private static void sendInitialData(ServerPlayNetworkHandler handler, PacketSender packetSender, MinecraftServer server) {
        //if (hasMod(handler) && !server.isHost(handler.player.getGameProfile())) {
        //    sendHello(handler);
        //    sendData(handler);
        //}
    }

    private static void sendHello(ServerPlayNetworkHandler handler) {
        //var packet = PolymerServerNetworking.buf(PROTOCOL);
        //PolymerServerNetworking.sendDirect(handler, HELLO_PACKET_ID, packet);
    }

    private static void sendData(ServerPlayNetworkHandler handler) {
        /*var packet = PolymerServerNetworking.buf(PROTOCOL);

        for (var entry : BreweryInit.DRINK_TYPES.entrySet()) {
            packet.writeBoolean(true);

            packet.writeIdentifier(entry.getKey());
            packet.writeNbt((NbtCompound) DrinkType.CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue()).result().get());

            if (packet.writerIndex() >= 1048576 / 4) {
                packet.writeBoolean(false);
                PolymerServerNetworking.sendDirect(handler, DEFINE_PACKET_ID, packet);
                packet = PolymerServerNetworking.buf(PROTOCOL);
            }
        }
        packet.writeBoolean(false);

        PolymerServerNetworking.sendDirect(handler, DEFINE_PACKET_ID, packet);*/
    }

    public static void decodeData(PacketByteBuf buf, BiConsumer<Identifier, DrinkType> consumer) {
        while (buf.readBoolean()) {
            try {
                var id = buf.readIdentifier();
                var type = DrinkType.CODEC.decode(NbtOps.INSTANCE, buf.readNbt()).result().get().getFirst();
                consumer.accept(id, type);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void register() {
        //PolymerServerNetworking.registerSendPacket(HELLO_PACKET_ID, PROTOCOL);
        //PolymerServerNetworking.registerSendPacket(DEFINE_PACKET_ID, PROTOCOL);

        ServerPlayConnectionEvents.JOIN.register(BrewNetworking::sendInitialData);
    }
}
