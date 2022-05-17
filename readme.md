# Shorthand Commands

<!-- [![GitHub release](https://img.shields.io/github/downloads/antD97/ShorthandCommands/v1.0/total)](https://github.com/antD97/ShorthandCommands/releases/tag/v1.0) -->

Shorthand Commands is a simple transpiler tool that enables [Minecraft](https://minceraft.net/)
[datapack](https://minecraft.fandom.com/wiki/Data_pack) developers to use additional convenient
syntax that makes `mcfunction` files more readable and organized. Minecraft won't recognize the
additional syntax on its own, so, after creating a Shorthand Commands datapack project, the
converter tool can be used to translate the special syntax into a regular datapack that Minecraft
can understand.

- [Converter tool download](https://github.com/antD97/ShorthandCommands/releases/tag/v1.0)
- [Wiki](https://github.com/antD97/ShorthandCommands/wiki)
- [Issue page](https://github.com/antD97/ShorthandCommands/issues)
<!-- - [Reddit thread]() -->

# Usage

1. Download the converter tool
   [here](https://github.com/antD97/ShorthandCommands/releases/tag/v1.0).
2. Unzip the tool and start a new datapack project or copy an old project to the directory that
   contains the `ShorthandCommands-X.X.jar` file.
3. Edit the `shorthand.conf` file by right-clicking it and clicking "edit".
4. Change `project=project` to `project=` followed by the name of the datapack directory that was
   created or copied in step 1.
5. `build=output` can be left for now, but know that the `build=` option determines where converted
   datapacks are saved to. See the [Workflow](#Workflow) section below to see how this option can
   prove useful.

From then on, the additional Shorthand Commands syntax can be used within your project. To convert
your project into a standard datapack that is usable by Minecraft, double click `run.bat` on
Windows, or run `java -jar ShorthandCommands-X.X.jar` on any other operating system. If there are
any issues with converting your project, the tool will terminate early and print a message stating
what went wrong.

## Workflow

It's annoying having to copy your converted datapack to your world's datapack directory every time
you want to test a change. To get around this, you can modify your `shorthand.conf` file so that the
tool automatically saves your converted projects there for you. Edit your `shorthand.conf` and
change `build=output` to `build=` followed by the location of your world's datapack directory. It
should look something like this if you're on Windows:

`build=C:\Users\<your username>\AppData\Roaming\.minecraft\saves\Test World\datapacks`

After saving your changes to the `shorthand.conf` file, the tool will then start saving your
converted projects to that directory instead.

## Building

Run: `./gradlew createBundledRelease`. The output is located in `build/bundledRelease`.

# Features

Checkout the [wiki](https://github.com/antD97/ShorthandCommands/wiki) for detailed descriptions
on all the features.

## [Function Definitions](https://github.com/antD97/ShorthandCommands/wiki/Function-Definitions)

Define multiple mcfunctions in a single `.mcfunction` file.

```
execute as @a[scores={time_alive=0}] run function example:player/on_death {
    say Oh no!
    say That hurt!
}
```

## [Line Break Backslash](https://github.com/antD97/ShorthandCommands/wiki/Line-Break-Backslash)

Break up long lines into multiple lines using `\`.

```
tellraw @a ["",{"text":"A long tellraw command\n","color":"aqua"},\
               {"text":"that you can actually read.","color":"green"}]
```

## [`$` Namespace Prefix](https://github.com/antD97/ShorthandCommands/wiki/$-Namespace-Prefix)

A shortened way of writing the namespace of your function.

```
function $:some/function
scoreboard players set @a $cool 100
```

## [Simplified Scoreboard Expressions](https://github.com/antD97/ShorthandCommands/wiki/Simplified-Scoreboard-Expressions)

A shortened way of writing `scoreboard player ...` commands.

```
#sb @s[type=creeper,scores={objective=111}] objective = 222
#sb @s[type=creeper,scores={objective=111}] objective %= @s[type=zombie,scores={objective=222}] objective"
```

---
Copyright © 2022 antD97  
Licensed under the [MIT License](LICENSE)
