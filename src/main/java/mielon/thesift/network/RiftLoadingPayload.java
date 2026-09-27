package mielon.thesift.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record RiftLoadingPayload(boolean start, boolean rift) implements CustomPacketPayload {
   public static final Type<RiftLoadingPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("the_sift", "rift_loading"));
   public static final StreamCodec<RegistryFriendlyByteBuf, RiftLoadingPayload> CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, RiftLoadingPayload::start, ByteBufCodecs.BOOL, RiftLoadingPayload::rift, RiftLoadingPayload::new
   );

   public Type<RiftLoadingPayload> type() {
      return TYPE;
   }

   public static void register() {
      PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
   }

   public static void send(ServerPlayer player, boolean start) {
      send(player, start, true);
   }

   public static void send(ServerPlayer player, boolean start, boolean rift) {
      if (ServerPlayNetworking.canSend(player, TYPE)) {
         ServerPlayNetworking.send(player, new RiftLoadingPayload(start, rift));
      }
   }
}
