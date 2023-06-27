# Modules
### OsTag and ChatFormatter
Choose what you will use on the server
### Disable
If you don't want to remove the plugin but you don't want it to work enable this option
### Update checker
Choose whether a player with the `ostag.admin` permission should receive information about the new version when he joins the server
### AutoUpdate
Download the latest version automatically if it possible <br/>
You can update the plugin manually using `/ostag update`
### CpsLimiter
Clicks per second limit


# Important notes

### Movement server
Check this [For Nukkit Forks](https://github.com/IndianBartonka/OsTag/wiki#for-nukkit-forks)
### Debug
If you enable debug in the console you will see more messages, they are for developers to easily determine the cause of the plugin not working for the user
### Forms Debug
If it is enabled, information about what a given player has done in forms is sent

## Ostag
### ScoreTag and NameTag
Enable what you want to be shown, you can choose whether the name tag or score tag can be both at once or one of them (don't reload the server!! only restart ) <br/>
Scoretag is ``a-subtag`` and ``subtag``

### Refreshing infromations
If you want the information above the player's head to update for more than 1 tick, change the ``refresh-time`` to as many seconds as you need
### Advanced and Normal player
If you want a player to have different information overhead than others, list him or use ``/otag add <player>`` to do so

Set what should be displayed in the name or subtag of the player, you can freely (Placeholder api is supported but you can still use it without this plugin, the list of default placeholders can be found in comfig.yml)

You can set the name of the world where the information above the player's head will not be updated (This is still experimental and may not always work well)

## ChatFormatter
(Placeholder api and Luck perms are supported)

### And (&) in chat
If you want players to be able to use "and" (&) to write in color in chat set ``And-for-all`` to ``true``
### Mention Sound
When someone mentions a player, they are informed on the subtitle + they will get the sound they set in the settings (FormConstructor plugin required)
### Break Between Messages
If you want one line of break between messages, set ``break-between-messages.enable`` to ``true``. Example:<br/>
![image](https://user-images.githubusercontent.com/68228599/210433043-c71265f3-9862-4822-a000-860c22295ef7.png)
### Censorship
If you want players not to be able to type certain words this useful, but it converts uppercase characters to lowercase because it's an experimental feature
### Cooldown
If you want players to be able to type every certain number of seconds, this may be useful to you, but this is also an experimental feature

## CpsLimiter
`Cps.max` = max value of player cps <br/>
`Cps.message` = cps limit reached message <br/>
# Messages and informations
Here you set everything you like




















