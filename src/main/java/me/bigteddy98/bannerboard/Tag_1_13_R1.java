package me.bigteddy98.bannerboard;

import me.bigteddy98.bannerboard.util.VersionUtil;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Tag_1_13_R1 {

    private static Constructor<?> nmsItemStack;
    private static Method getOrCreate;
    private static Method setInt;
    private static Method toBukkitStack;
    private static Object filledMap;

    static {
        try {
            nmsItemStack = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                nmsItemStack = PacketManager.getNewNMS("net.minecraft.world.item.ItemStack").getConstructor(PacketManager.getNewNMS("net.minecraft.world.level.IMaterial"));
            } else {
                nmsItemStack = PacketManager.getNMS("ItemStack").getConstructor(PacketManager.getNMS("IMaterial"));
            }

            getOrCreate = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                getOrCreate = PacketManager.getNewNMS("net.minecraft.world.item.ItemStack").getMethod("getOrCreateTag");
            } else {
                getOrCreate = PacketManager.getNMS("ItemStack").getMethod("getOrCreateTag");
            }


            setInt = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                setInt = PacketManager.getNewNMS("net.minecraft.nbt.NBTTagCompound").getMethod("setInt", String.class, Integer.TYPE);
            } else {
                setInt = PacketManager.getNMS("NBTTagCompound").getMethod("setInt", String.class, Integer.TYPE);
            }

            toBukkitStack = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                toBukkitStack = PacketManager.getCraft("inventory.CraftItemStack").getMethod("asBukkitCopy", PacketManager.getNewNMS("net.minecraft.world.item.ItemStack"));
            } else {
                toBukkitStack = PacketManager.getCraft("inventory.CraftItemStack").getMethod("asBukkitCopy", PacketManager.getNMS("ItemStack"));
            }


            filledMap = null;

            if (VersionUtil.isHigherThan("v1_16_R3")) {
                filledMap = PacketManager.getNewNMS("net.minecraft.world.item.Items").getDeclaredField("pp").get(null);
            } else {
                filledMap = PacketManager.getNMS("Items").getDeclaredField("FILLED_MAP").get(null);
            }

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // call itemstack.getOrCreateTag().setInt("map", i2);
    public static ItemStack buildMap(short id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchFieldException, SecurityException {
        final Object nmsStack = nmsItemStack.newInstance(filledMap);
        final Object nbtTag = getOrCreate.invoke(nmsStack);
        setInt.invoke(nbtTag, "map", id);

        final ItemStack stack = (ItemStack) toBukkitStack.invoke(null, nmsStack);
        stack.setDurability(id);

        return stack;
    }
}
