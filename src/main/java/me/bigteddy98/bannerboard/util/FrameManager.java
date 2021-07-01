package me.bigteddy98.bannerboard.util;

import me.bigteddy98.bannerboard.PacketManager;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FrameManager {

    private static final Method ADD_ENTITY;
    private static final Field FIELD_NMS_WORLD;
    private static Constructor<?> CREATE_FRAME;
    private static Constructor<?> BLOCK_POSITION;
    private static Class<?> ENUM_DIRECTION;

    static {
        try {
            final Class<?> craftWorld = PacketManager.getCraft("CraftWorld");
            FIELD_NMS_WORLD = craftWorld.getDeclaredField("world");
            FIELD_NMS_WORLD.setAccessible(true);

            final Class<?> entity;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                entity = PacketManager.getNewNMS("net.minecraft.world.entity.Entity");
            } else {
                entity = PacketManager.getNMS("Entity");
            }

            ADD_ENTITY = craftWorld.getDeclaredMethod("addEntity", entity, CreatureSpawnEvent.SpawnReason.class, Class.forName("org.bukkit.util.Consumer"));

            final Class<?> world;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                world = PacketManager.getNewNMS("net.minecraft.world.level.World");
            } else {
                world = PacketManager.getNMS("World");
            }

            final Class<?> blockPosition;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                blockPosition = PacketManager.getNewNMS("net.minecraft.core.BlockPosition");
            } else {
                blockPosition = PacketManager.getNMS("BlockPosition");
            }

            ENUM_DIRECTION = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                ENUM_DIRECTION = PacketManager.getNewNMS("net.minecraft.core.EnumDirection");
            } else {
                ENUM_DIRECTION = PacketManager.getNMS("EnumDirection");
            }

            CREATE_FRAME = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                CREATE_FRAME = PacketManager.getNewNMS("net.minecraft.world.entity.decoration.EntityItemFrame").getConstructor(world, blockPosition, ENUM_DIRECTION);
            } else {
                CREATE_FRAME = PacketManager.getNMS("EntityItemFrame").getConstructor(world, blockPosition, ENUM_DIRECTION);
            }


            BLOCK_POSITION = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                BLOCK_POSITION = PacketManager.getNewNMS("net.minecraft.core.BlockPosition").getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            } else {
                BLOCK_POSITION = PacketManager.getNMS("BlockPosition").getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static BlockFace opposite(BlockFace b) {
        if (b == BlockFace.NORTH)
            return BlockFace.SOUTH;
        if (b == BlockFace.EAST)
            return BlockFace.WEST;
        if (b == BlockFace.SOUTH)
            return BlockFace.NORTH;
        if (b == BlockFace.WEST)
            return BlockFace.EAST;
        if (b == BlockFace.UP)
            return BlockFace.DOWN;
        if (b == BlockFace.DOWN)
            return BlockFace.UP;
        throw new RuntimeException("Unknown block face " + b);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void spawn(Location loc, BlockFace hasWall) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        hasWall = opposite(hasWall);
        final Object nmsWorld = FIELD_NMS_WORLD.get(loc.getWorld());
        final Object pos = BLOCK_POSITION.newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Object frame = CREATE_FRAME.newInstance(nmsWorld, pos, Enum.valueOf((Class<? extends Enum>) ENUM_DIRECTION, hasWall.name()));
        ADD_ENTITY.invoke(loc.getWorld(), frame, CreatureSpawnEvent.SpawnReason.CUSTOM, null);
    }

}
