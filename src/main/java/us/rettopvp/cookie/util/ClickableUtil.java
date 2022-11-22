package us.rettopvp.cookie.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@NoArgsConstructor
public class ClickableUtil {

	private List<TextComponent> components = new ArrayList<>();

	public ClickableUtil(String msg) {
		TextComponent message = new TextComponent(msg);

		this.components.add(message);
	}

	public ClickableUtil(String msg, String hoverMsg, String clickString) {
		this.add(msg, hoverMsg, clickString);
	}

	public TextComponent add(String msg, String hoverMsg, String clickString) {
		TextComponent message = new TextComponent(msg);

		if (hoverMsg != null) {
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMsg).create()));
		}

		if (clickString != null) {
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
		}

		this.components.add(message);

		return message;
	}

	public void add(String message) {
		this.components.add(new TextComponent(message));
	}

	public void sendToPlayer(Player player) {
		player.spigot().sendMessage(this.asComponents());
	}

	public TextComponent[] asComponents() {
		return this.components.toArray(new TextComponent[0]);
	}
}
