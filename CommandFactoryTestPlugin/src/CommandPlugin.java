import com.teej107.executorhandler.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public class CommandPlugin extends JavaPlugin implements ICommand
{
	@Override
	public void onEnable()
	{
		ExecutorHandler cf = new ExecutorHandler(this);
		cf.registerCommands(this);
	}

	@Executable(command = "test @ @...")
	public boolean test(CommandSender sender, String s, String[] ar)
	{
		sender.sendMessage("test " + s + " " + StringUtils.join(ar, " "));
		return true;
	}

	@Executable(command = "test 1 2 3")
	public boolean test3(CommandSender sender)
	{
		sender.sendMessage("test 1 2 3");
		return true;
	}

	@Executable(command = "hello world @")
	public boolean hello(CommandSender sender, Material m)
	{
		sender.sendMessage("hello world " + m.name());
		return true;
	}

	@Executable(command = "broadcast @... @")
	public boolean broadcast(CommandSender sender, String[] args, String something)
	{
		sender.sendMessage("-=-=-=-=-=-=-");
		Bukkit.broadcastMessage(args.toString());
		Bukkit.broadcastMessage(something);
		sender.sendMessage("-=-=-=-=-=-=-");
		return true;
	}

	@Executable(command = "test @")
	public boolean plugin(CommandSender sender, Plugin p)
	{
		sender.sendMessage(ChatColor.AQUA + p.getName());
		return true;
	}

	@Executable(command = "bc @...")
	public boolean bc2(CommandSender sender, String s)
	{
		sender.sendMessage(s);
		return true;
	}

	@Executable(command = "bc -message @...")
	public boolean bc1(CommandSender sender, String s)
	{
		sender.sendMessage("Message: " + ChatColor.GREEN + s);
		return true;
	}

	@Parser(parseFailedMessage = Parser.OBJECT + " is not a Plugin")
	public Plugin pluginParser(String s)
	{
		return Bukkit.getPluginManager().getPlugin(s);
	}
}
