**09.12.2022** </br>
**Version: 1.4.8** </br>

The plugin now has so many naming changes, see the plugin page on CloudBurst to know them all and get it right </br>
Changed name from `OstagPNX` to `OsTag` </br>
Changed permision names </br>

Permisions: </br>
`ostag.admin` - all comands and colors in chat </br>
`ostag.colors` - allow to use `§` in chat </br>

Added support for Nukkit (Vanilla) (required java 8 or higer) </br>
Added Ping colors Added in config.yml </br>
Added colors improvement in `/ostag ver` and start message </br>
Added showing plugin activation speed </br>
Now you can use `/ostag r` for reload plugin </br>
Now you can use `&` for colors in config </br>
Now you can use `&` for colors in chat if you have `ostag.colors` and disable `§` in chat </br>
When the plugin encounters an error while reloading the config, it will inform about it and show an error in the console </br>
------------------------------------------------------------------------
**11.12.2022** </br>
**Version: 1.4.8.01** </br>
Now if you want everyone to be able to use `&` by default instead of `§` set `and-for-all` to true </br>
Still canot use `§` in chat , I try to fix it </br>
Now  `<suffix>` `<prefix>` `<groupDisName>` support `&` in chat </br>
Now you can use <xp> in name and chat </br>
Block `\n` in chat formating </br>
Changed `message.format` to `message-format` </br>
----------------------------------------------------------------------------
**Started in 11.12.2022 released on the day 12.12.2022**  </br>
**Version: 1.4.9** </br>
Config look improvements </br>
When you just type only `/ostag` it will bring up a list of parameters you can use </br>
Added `<unique-description>` using it in the `Players` section you can add an emoji or some text for this player only </br>
How to create custom emojis? Visit this page https://wiki.bedrock.dev/concepts/emojis.html#custom-emoji </br>
How to make player face emoji? Visit this page https://mcskins.top/avatar-maker </br>
Changed some naming in config again! </br>
```yml
cooldown.enable: true
cooldown.delay: 10 
cooldown.message: "aYou must wait &b<left> &eSeconds&a"
```
Changed to </br>
```yml
cooldown: 
  enable: true 
  delay: 10 
  message: "aYou must wait &b<left> &eSeconds&a!"
```
And </br>
```yml
enable-censorship: true 
censorship: "Bread" 
```
Changed to 
```yml
censorship: 
  word: "Bread" 
  enable: true 
```
Added breaks between messages </br>

-----------------------------------
**Released on the day 15.12.2022** </br>
**Version: 1.4.9.01** </br>
No breaks between messages when player was operator, fixed! </br>
some improvements in code </br>
-----------------------------------
**Released on the day 17.12.2022** </br>
**1.4.9.02** </br>
Code improvements </br>
Now `§` working in chat :D </br>
Added disable ostag in a given world! if the player logs into a world where it is allowed, the name and information will remain until no other plugin changes it or enters a allowed world </br>
Factions plugin support (Placeholder `<faction>`) </br>
---------------------------------
**Released on the day 18.12.2022** </br>
**1.5.0** </br>
Added PlaceholderAPI support </br>
Removed `<faction>` placeholder </br>
Want to use OsTag but don't want to use PlaceholderAPI? You can still use it without the placeholder api
-----------------------------------
**Released on the day 18.12.2022** </br>
**1.5.1** </br>
Code improvements </br>
Bug fixes (`<device>` placeholder) </br>
------------------------------------
**Released on the day 13.12.2022** </br>
**1.5.2** </br>
Big code improvements </br>
Now add `Windows Phone` in <device> placeholder </br>
Now when you move away from the player you only see the name line </br>
Added placeholders that already existed but can now be used in other plugins using PlaceholderAPI </br>
`%ostag_cps%` 
`%ostag_test%` 
`%ostag_device%` 
`%ostag_controler%` 
`%ostag_prefix%` 
`%ostag_suffix%`  </br>
Delated <deathskull> placeholder , now this placeholder is in DeathSkull plugin </br>
-----------------------------------------------
**Released on the day 03.01.2023** </br>
**1.5.3** </br>
Added better support for `PowerNukkit` </br>
Added in `<gamemode>` placeholder spectator detect </br>
Code improvements </br>
Added `%ostag_group%` placeholder to papi </br>
changed `PowerNukkiX-movement-server` to `movement-server` </br>
----------------------------------------------
**Released on the day 22.02.2023** </br>
**1.5.4** </br>
If you used old versions, remove config to generate a new one!!! </br>
Some code improvements and so many code style fixes </br>
Windows phone and gear vr removed from <device> placeholder , because not suported by mojang </br>
`
IMPORTANT NEWS:
As of October 2020, Minecraft will no longer be updated or supported on Windows 10 Mobile, Gear VR, iOS 10 or lower & Android devices with less than 768MB of RAM.
`</br>
Added in `<device>` placeholder `Linux` detect </br>
Smal naming changes , check new config in github (https://github.com/IndianBartonka/OsTag/blob/main/src/main/resources/config.yml) </br>
You can choose whether to display name or score tag </br>
Added food placeholders `<food>` and `<food_max>` </br>
now `breaks between messages` don't require enable cooldown </br>
Cooldown fix and cooldown placeholder `%ostag_cooldown%` </br>
---------------------------
**Released on the day xx.03.2023** </br>
**1.5.5** </br>
__Naming changes__: </br>
Fixed naming `<controler>` and `%ostag_controler%` to `<controller>` `%ostag_controller%` and also fixed `<preffix>` and `%ostag_preffix%` and `<unique_description>` </br>
Changed in Modules section  `ChatFormater`to `ChatFormatter` in config </br>

__New things__: </br>
Added `PrefixesUtil` interface to faster changing plugin placeholder naming (my first interface ~IndianPL) </br>
Added 35 and 5 levels in `<xp>` placeholder </br>
Added `%ostag_xp%` placeholder for PlaceholderApi </br>

__Fixes__: </br>
Code style fixes and efficency </br>
Fixed `Dedicated` in `<device>` placeholder </br>
Fixed decimals in `<health>` placeholder </br>
Cooldown fix </br>

__Others__: </br>
Cooldown disable info </br>
Better code optymalization </br>

---------------------------------------------------------------
**Released on the day xx.xx.2023** </br>
**1.5.6** </br>

__Naming changes__: </br>

__New things__: </br>

__Fixes__: </br>

__Others__: </br>


-------------------------------------------------
<div align="center">

__**General info**__  </br>
</div>

* After beta 1.5.5 each beta version will have additional numbering for example : **1.5.6.01** </br>
* Since 1.5.5 `ChangeLod.md` gets a new information format
*




