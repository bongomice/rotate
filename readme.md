# Rotate
*Rotate Minecraft Blocks and Change Paintings*

**Rotate** is a very simple plugin, which enables you to rotate blocks in Minecraft. Ever had that annoying moment when your stairs got placed in the wrong direction, or logs going crazy? Rotate aims to solve that by giving you a tool to rotate and fix those blocks. To make things even sweeter, Rotate has WorldGuard compatibility, so don't worry about griefers turning all your blocks around. Here's the full list of blocks that are supported:

+ **Stairs**
+ **Heads**
+ **Rails**
+ **Slabs**
+ **Logs**
+ **Pistons**
+ **Furnaces**
+ **Dispensers**
+ **Paintings**
+ **Pumpkins and Jack 'o Lanterns**
+ **Chests, Ender Chests and Double Chests**

Using Rotate couldn't be easier. Once you've downloaded the file and placed it in your plugins folder, simply reload your plugins (or restart your server) and then, while holding a tool (hoe, axe, pickaxe, sword, stick or arrow) run the command. That will active Rotate on that tool. Then, it's just a matter of clicking on blocks to rotate them!

Note that by default permissions are disabled. If you want to use permissions, set it to *true* in the Rotate config file (generated when you first run the plugin). You can see what permissions are available below.
You can also choose to save players' tools or not: if you don't want your players to type each time they connect the **/rotate tool** command, set ``save-players-tools`` to *true* in the ``config.yml``, otherwise, set it to *false*.
Finally, you're able to set a default tool in the config. See **Valids Tools** to get a list of IDs. Set ``default-tool`` to *-1* if you don't want any default tool, as it's setted by default.

Please note that Rotate is in development, and though most features should work, there are and will be bugs. The code is open-source though, so feel free to check out and fork the code, improve it and fix bugs.

To see the Rotate capabilities, you can also watch our [video](http://www.youtube.com/watch?v=scB3uUIyPz4) on Youtube.

-----

# Links

+ [Download](http://dev.bukkit.org/media/files/614/311/Rotate.jar)
+ [BukkitDev](http://dev.bukkit.org/server-mods/rotate/)
+ [Bukkit Forums](http://forums.bukkit.org/threads/misc-rotate-v1-1-rotate-minecraft-blocks-and-change-paintings-1-3-1-r2.95652/)
+ [Video](http://www.youtube.com/watch?v=scB3uUIyPz4)

-----

#Valids tools

To remain consistent, Rotate doesn't let players use items such as blocks of sand, dirts, redstone... Here are the items which can be used with Rotate and their IDs:

+ **Hoes**:
    + Wooden hoe (290) 
    + Stone hoe (291)
    + Iron hoe (292)
    + Diamond hoe (293) 
    + Golden hoe (294)
+ **Axes**:
    + Wooden axe (271)
    + Stone axe (275)
    + Iron axe (258)
    + Golden axe (286)
    + Diamond axe (279),
+ **Pickaxes**:
    + Wooden pickaxe (270)
    + Stone pickaxe (274)
    + Iron pickaxe (257)
    + Diamond pickaxe (278)
    + Golden pickaxe (285)
+ **Shovels**
    + Wooden shovel (269)
    + Stone shovel (273)
    + Iron shovel (256)
    + Golden shovel (284)
    + Diamond shovel(277)
+ **Swords**:
    + Iron sword (267) 
    + Wooden sword (268) 
    + Stone sword (272) 
    + Diamond sword (276)
    + Golden sword (283)
+ **Sticks & Arrows** (280 & 262)

-----

# Commands

``/rotate tool``
Sets the tool you are holding as the rotate tool. If **save-users-tools** is setted to *true* in the config, players won't have to type this command each time they connect.

``/rotate reset``
Resets the rotate tool set using the previous command.

``/rotate getDefaultTool``
Get the name of the default rotate tool setted in the config.

``/rotate setDefaultTool``
Sets the tool you are holding as the default rotate tool in the config. This command is by default for ops only.

-----

# Permissions

``rotate.access``
Gives access to the Rotate command and tools, excepted the **/rotate setDefaultTool** command.

``rotate.set-default-tool``
Gives access to the **/rotate setDefaultTool** command. This permission is by default only given to ops.

-----

# Changelog

+ **1.5** Added support for heads and new 1.4 and 1.5 blocks such as activator rails and trapped chests, and the new painting.
Rotated blocks are now seen as broken and replaced blocks for compatibility with block recording plugins like SWatchdog.
+ **1.4** Added support for a default tool. Rotate can now save users' tools.
+ **1.3** Added support for permissions and a config file.
+ **1.2** Plugin is available on BukkitDev. Fixed tools.
+ **1.1** First Bukkit forum release. Added support for chests and pumpkins.
+ **1.0** Code is available on Github.

-----

###**Your comments are appreciated, so please report errors and bugs you found, and give us your suggestions.**