![](/src/main/resources/assets/projecte/logo.png?raw=true)

This is a fork of ProjectE with the aim of code modification for inclusion in [MagiTekkit](https://github.com/we-commit-coding-felonies/magitekkit), because crafttweaker just isn't quite up to the task.

Here is a list of changes:
 - Gem of Eternal Density can only create up to dark matter, Void Ring can still make red matter
 - Transmutation table revamp
   - Learning items requires total destruction, meaning you have to sacrifice it and all of it's EMC value to be able to make more!
   - 3 'tiers' of table:
     - Normal
       - Identical to the tablet from stock ProjectE
     - Classic
       - Closer to the table from EE2
       - No search bar or pages
       - Transmutation is locked based on ore dictionary
     - Basic
       - Like classic, but with even more limits
       - EMC is stored in the table itself, rather than the player
         - Slowly decays over time!
       - Covalence loss, unless you use covalence dust!
       - Klein stars cannot be used to fuel it!
 - Archangels smite now applies random potion effects to entities (the effects it applies are configurable, too!)
 - Configurable collector / relay values
   - Stuff like EMC/s, EMC transfer rate, etc.
   - Also a MK0 and MK4 collector/relay, both configurable
 - Keybind to open alchemical bag / transmutation tablet (default B / N respectively)
 - More items are now baubles
   - Klein star (body slot)
   - Transmutation tablet (head slot)
   - Mind stone (head slot)
   - Alchemical bag (belt slot)
 - Armor rebalance
   - Dark matter is mostly unchanged, but its damage resistance was nerfed
   - Red matter armor is roughly as strong as it is in stock ProjectE, but will 'burn out', reducing its damage resistance when taking lots of damage in a short amount of time
   - Gem armor has recieved an EMC-powered alchemical barrier!
     - Completely blocks ALL damage recieved (assuming you can afford it)
       - Certain types of damage can be configured to bypass the barrier (defaults to the void and /kill)
     - Consumes from klein stars & fuel items (or your tansmutation tablet, if you set it in the config)
     - Ramps up in cost vs higher damage attacks
     - Make sure you keep it fueled, or the armor will start to break!
       - When damaged, almost all of the armor's abilities will cease to work
       - Will continue to function as extremely strong normal armor, but has low durability
       - Can be repaired by a repair talisman
       - If it runs out of durability, it will explode and turn into Red Matter armor!
 - Bugfixes
   - Matter tools 'oredict radius mining' (eg matter axe aoe tree harvest) no longer fail when water is nearby
   - Subtitles have been localized (incomplete! please help if you can.)
   - Energy Collector progress bars now work properly

Almost all of these changes are configurable, so if you just want the bugfixes you can disable all of the feature changes!

Thanks to the following people for helping translate some stuff from english:
- Manitara (French): Mû#0001

<br>

<b>Below here is all from the original ProjectE readme:</b>

<hr>


# Can I include it in a modpack?
This mod may be redistributed as part of a modpack, both public and private modpacks are allowed. You may not mirror the mod by itself or create a "mod installer", legal action will be taken against sites found violating this. It is requested that you credit the mod on any lists as ProjectE, there is no space in the name.

# I found a bug
Bugs can be reported at: https://github.com/sinkillerj/ProjectE/issues

Please try the latest release build before reporting, be sure to also include any logs or steps to reproduce you may have, as well as your Forge version, and whether or not you are using a third party loader such as Cauldron. When submitting logs please use a service such as Pastebin, do not paste the log directly into the issue.

# Downloads
http://minecraft.curseforge.com/mc-mods/projecte/files

# Support Development
![](/patreon.png?raw=true)

We accept donations via Patreon, visit the team members section to learn more.

# Join the Conversation

Discord: https://discordapp.com/invite/uTbDkT4

# Current Team Members
Members actively working on ProjectE.

SinKillerJ - Head of Alchemical Studies - Main Project & Community Lead:

* Twitter: https://twitter.com/sinkillerj
* Patreon: https://www.patreon.com/sinkillerj

pupnewfster - Rising superstar - Current Lead of New Version Ports

MaPePeR(Blubberbub) - Alchemical Value Archivist - Lead EMC Mapper Developer: 

* Twitter: https://twitter.com/Blubb3rbub

Lilylicious - Lady of Little Big Things - Assistant Developer:

* Twitter: https://twitter.com/Lilyliciously

# Emeritus (Retired Team Members)
Members that have retired from their role, but are welcome to return and contribute further.

MozeIntel - Original lead developer: https://twitter.com/Moze_Intel

Williewillus - Maintainer, 1.8.x-1.13.x: https://twitter.com/williewillus

# Former Team Members
Members that are no longer part of the team.

Magic Banana - Former texture artist: https://twitter.com/Magic_Banana_

Kolatra - Former collaborator: https://twitter.com/ItsKolatra

# Thanks To
x3n0ph0b3 - EE2 creator, Allowed use of EE2 assets: https://twitter.com/x3n0ph0b3x

MidnightLightning - EE2 GUI Textures: https://github.com/MidnightLightning
