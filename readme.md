# Shorthand Commands

[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/antD97/ShorthandCommands/total?logo=github)](https://github.com/antD97/ShorthandCommands/releases)
[![GitHub Repo stars](https://img.shields.io/github/stars/antD97/ShorthandCommands?style=flat&logo=github)](https://github.com/antD97/ShorthandCommands/stargazers)
[![Discord](https://img.shields.io/discord/1327005882113986772?logo=discord&label=discord)](https://discord.gg/kqQQkGVh)
[![Ko-fi](https://img.shields.io/badge/Ko--fi-FF5E5B?logo=ko-fi&logoColor=white)](https://ko-fi.com/antd_)
[![GitHub License](https://img.shields.io/github/license/antD97/ShorthandCommands)](/LICENSE)

Shorthand Commands is a simple transpiler tool that enables [Minecraft](https://minceraft.net/)
[datapack](https://minecraft.fandom.com/wiki/Data_pack) developers to use additional convenient
syntax that makes `.mcfunction` files more readable and organized. Minecraft won't recognize the
additional syntax on its own, so, after creating a Shorthand Commands datapack project, the
converter tool can be used to translate the special syntax into a regular datapack that Minecraft
can understand.

- [Converter tool download](https://github.com/antD97/ShorthandCommands/releases/tag/v1.0)
- [Wiki](https://github.com/antD97/ShorthandCommands/wiki)
- [Issue page](https://github.com/antD97/ShorthandCommands/issues)
- My Shorthand Commands datapack: [Death Curse](https://github.com/antD97/DeathCurse)

I created this project before I found the [MCScript](https://github.com/Stevertus/mcscript) project
which targets many of the same problems as this project and is much more fleshed out than this one
will ever be. Since I found MCScript, I've decided that the target use case for this project will be
something in between a standard datapack and MCScript. It provides some convenient syntax that's
better than a standard datapack, but is simple enough that you don't have to learn a new programming
language.

# Usage

1. Download the converter tool
   [here](https://github.com/antD97/ShorthandCommands/releases/tag/v1.0).
2. Unzip the tool and start a new datapack project or paste an old project in the directory that
   contains the `ShorthandCommands-X.X.jar` file.
3. Open the `shorthand.conf` file in a text editor.
4. Change `project=project` to `project=` followed by the name of the datapack directory that was
   created or copied in step 1.
5. `save=output` can be left for now, but know that the `save=` option determines where converted
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
change `save=output` to `save=` followed by the location of your world's datapack directory. It
should look something like this if you're on Windows:

`save=C:\Users\<your username>\AppData\Roaming\.minecraft\saves\Test World\datapacks`

After saving your changes to the `shorthand.conf` file, the tool will then start saving your
converted projects to that directory instead.

## Building

Run: `./gradlew release`. The output is located in `app/build/release`.

If running from IntelliJ IDEA, first run `./gradlew idea` and be sure to modify the run
configuration by setting "Working directory" to the `app` directory.

# Feature Preview

Checkout the [wiki](https://github.com/antD97/ShorthandCommands/wiki) for a list of all the features
along with detailed descriptions.

## [Function Definitions](https://github.com/antD97/ShorthandCommands/wiki/Function-Definitions)

Define multiple mcfunctions in a single `.mcfunction` file.

```
execute as @a[scores={time_alive=0}] run function example:player/on_death
{
    say Oh no!
    say That hurt!
}
```

## [Repeat Lines](https://github.com/antD97/ShorthandCommands/wiki/Repeat-Lines)

Repeat a line `n` times.

```
#!10x
particle minecraft:ambient_entity_effect ~ ~ ~ 170 0 0 255 0
```

## [Find & Multi-replace](https://github.com/antD97/ShorthandCommands/wiki/Find-&-Multi-replace)

Repeat a line while replacing part of it with values from a list.

```
#!find=creeper
#!replace=creeper|skeleton|zombie
execute as @e[type=creeper] run say I'm a creeper!
```

## [Simplified Scoreboard Expressions](https://github.com/antD97/ShorthandCommands/wiki/Simplified-Scoreboard-Expressions)

A shortened way of writing `scoreboard player ...` commands.

```
#!sb @s[type=creeper,scores={objective=111}] objective = 222
#!sb @s[type=creeper,scores={objective=111}] objective %= @s[type=zombie,scores={objective=222}] objective"
```

## [`__` Namespace Prefix](https://github.com/antD97/ShorthandCommands/wiki/__-Namespace-Prefix)

A shortened way of writing the namespace of your function.

```
function __:some/function
scoreboard players set @a __cool 100
```

## [Line Break Backslash](https://github.com/antD97/ShorthandCommands/wiki/Line-Break-Backslash)

Break up long lines into multiple lines using ``\``.

```
tellraw @a ["",{"text":"A long tellraw command\n","color":"aqua"},\
               {"text":"that you can actually read.","color":"green"}]
```

## [Lint Hiding](https://github.com/antD97/ShorthandCommands/wiki/Lint-Hiding)

A makeshift way of hiding text editor error indicators.

```
function my_namespace:introduce
#! {
    say Hello!
    say I am @s!
#! }

#! tellraw @a ["",{"text":"A long tellraw command\n","color":"aqua"},\
               #! {"text":"that you can actually read.","color":"green"}]
```

---
Copyright © 2022 antD97  
Licensed under the [MIT License](LICENSE)
