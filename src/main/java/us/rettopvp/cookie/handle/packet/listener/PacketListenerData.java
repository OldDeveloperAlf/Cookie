package us.rettopvp.cookie.handle.packet.listener;

import java.lang.reflect.Method;

import us.rettopvp.cookie.handle.packet.Packet;

public class PacketListenerData {
	
    private Object instance;
    private Method method;
    private Class packetClass;
    
    public boolean matches(Packet packet) {
        return this.packetClass == packet.getClass();
    }
    
    public PacketListenerData(Object instance, Method method, Class packetClass) {
        this.instance = instance;
        this.method = method;
        this.packetClass = packetClass;
    }
    
    public Object getInstance() {
        return this.instance;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Class getPacketClass() {
        return this.packetClass;
    }
}
