CraftBukkit Plugin: ServerQueue
===============================

Description:
------------

Lets you reserve Slots, and even automaticly kick players for VIPs. Full permissions support.

Plugin is in the early test-phase

###Commands:

* '/serverqueue' or '/sq' -- Shows aviable sub-commands
* '/sq status' -- Shows a status-string (actual players, max players and slot-assignment)
* '/sq reload' -- Reloads the Config File to apply all user-made changes.
Note: you can always use '/serverqueue' instead of '/sq'

###Permissions:

* 'serverqueue.reload' -- allows access to the '/sq reload' command
* 'serverqueue.status' -- allows access to the '/sq status' command
* 'serverqueue.nokick' -- if a player has this permission, he will not be kicked by the plugin, if a VIP wants to join a full server
* 'serverqueue.vip'    -- sets this player as a VIP
* 'serverqueue.master' -- this player is handlet like a VIP in the config-file (see config-section)

###Config-File

This section is under construction