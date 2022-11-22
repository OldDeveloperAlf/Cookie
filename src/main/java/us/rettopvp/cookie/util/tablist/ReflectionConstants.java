package us.rettopvp.cookie.util.tablist;

public class ReflectionConstants {

	public static final Class<?> TAB_PACKET_CLASS = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo");
	public static final Class<?> CRAFT_PLAYER_CLASS = Reflection.getCraftBukkitClass("entity.CraftPlayer");
	public static final Class<?> NMS_PACKET_CLASS = Reflection.getMinecraftClass("Packet");
	public static final Class<?> NMS_PLAYER_CLASS = Reflection.getMinecraftClass("EntityPlayer");
	public static final Class<?> PLAYER_CONNECTION_CLASS = Reflection.getMinecraftClass("PlayerConnection");
	public static final Class<?> NETWORK_MANAGER_CLASS = Reflection.getMinecraftClass("NetworkManager");
	public static final Reflection.ConstructorInvoker TAB_PACKET_CONSTRUCTOR = Reflection.getConstructor(TAB_PACKET_CLASS);
	public static final Reflection.MethodInvoker GET_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getHandle");
	public static final Reflection.MethodInvoker GET_PROFILE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getProfile");
	public static final Reflection.MethodInvoker VERSION_METHOD = Reflection.getMethod(NETWORK_MANAGER_CLASS, "getVersion");
	public static final Reflection.MethodInvoker SEND_PACKET = Reflection.getMethod(PLAYER_CONNECTION_CLASS, "sendPacket", NMS_PACKET_CLASS);
	public static final Reflection.FieldAccessor<String> TAB_PACKET_NAME = Reflection.getField(TAB_PACKET_CLASS, String.class, 0);
	public static final Reflection.FieldAccessor<Integer> TAB_PACKET_ACTION = Reflection.getField(TAB_PACKET_CLASS, int.class, 5);
	public static final Reflection.FieldAccessor<?> PLAYER_CONNECTION = Reflection.getField(NMS_PLAYER_CLASS, PLAYER_CONNECTION_CLASS, 0);
	public static final Reflection.FieldAccessor<?> NETWORK_MANAGER = Reflection.getField(PLAYER_CONNECTION_CLASS, NETWORK_MANAGER_CLASS, 0);

}
