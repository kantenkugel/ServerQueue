CraftBukkit Plugin: ServerQueue
===============================

**by Kantenkugel**

- - -

---

***

* * *

___

_ _ _

Description:
------------

Lets you reserve Slots, and even automaticly kick players for VIPs. Full permissions support.  
Plugin is in the early test-phase

### Commands:

* `/serverqueue` or `/sq` -- Shows aviable sub-commands.
* `/sq status` -- Shows a status-string *(actual players, max players and slot-assignment)*.
* `/sq reload` -- Reloads the Config File to apply all user-made changes.

**Note:** you can always use `/serverqueue` instead of `/sq`.

### Permissions:

* `serverqueue.reload` -- allows access to the `/sq reload` command.
* `serverqueue.status` -- allows access to the `/sq status` command.
* `serverqueue.nokick` -- if a player has this permission, he will not be kicked by the plugin, if a VIP wants to join a full server.
* `serverqueue.vip`    -- sets this player as a VIP.
* `serverqueue.master` -- this player is handlet like a VIP in the config-file *(see config-section)*.

### Config-File

**Example-Config:**

    ServerQueue:
        SlotsToReserve: 2
        PermReserve: true
        Kick:
            VIP: true
            Perm: false
            Notice: true
    VIPs:
        - Kantenkugel

**Description:**

* `SlotsToReserve` -- Defines the number of Slots, which will be reserved for VIPs.
* `PermReserve` -- Wheather to enable or disable slot-reservation for the users with the vip **permission**.
* `Kick->VIP` -- Wheather to kick or not to kick for VIPs from the config-file.
* `Kick->Perm` -- Wheather to kick or not to kick for permission-VIPs.
* `Kick->Notice` -- If a Player would be kicked for a VIP, first show the VIP a message telling him that another connect-attempt of him will kick a User. This will reset after 20 seconds.
* `VIPs` -- A YAML-formatted list of players, which are *(master)*VIPs.