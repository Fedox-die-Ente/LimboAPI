/*
 * Copyright (C) 2021 - 2025 Elytrium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.limboapi.protocol.packets.c2s;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import net.elytrium.limboapi.server.LimboSessionHandlerImpl;

public class ArmSwingPacket implements MinecraftPacket {

    private int hand; // 0 = main hand, 1 = off hand

    public ArmSwingPacket() {}

    public ArmSwingPacket(int hand) {
        this.hand = hand;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        if (protocolVersion.lessThan(ProtocolVersion.MINECRAFT_1_9)) {
            this.hand = 0; // before 1.9, only main hand exists
        } else {
            this.hand = ProtocolUtils.readVarInt(buf); // main hand = 0, off-hand = 1
        }
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        throw new IllegalStateException("Encoding not supported in Limbo");
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        if (handler instanceof LimboSessionHandlerImpl) {
            return ((LimboSessionHandlerImpl) handler).handle(this);
        } else {
            return true;
        }
    }

    @Override
    public int expectedMaxLength(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        return 1; // only a single byte for hand
    }

    @Override
    public int expectedMinLength(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        return 1;
    }

    public int getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "ArmSwingPacket{hand=" + hand + "}";
    }
}

