**DESCRIPTION**

Adds a vanilla-style block to compress items with a 2x2, 3x3, or hollow-square recipe.
It also adds a vanilla-style decompressor which does the opposite.

**RECIPES**

*Compressor:*
```
C P C
C T C
C P C
```
C = Cobblestone, P = Piston, T = TNT

*Decompressor:*
```
C S C
C T C
C S C
```
C = Cobblestone, S = Sticky Piston, T = TNT

**FUEL**

Both are fueled by gunpowder. After a cool-down, 1 powder is used, a _quiet_ explosion sound
    plays, and the operation completes.
    
**UI**

If I can't get a standard GUI working, it'll be fine without one:
    Right-click with a stack of gunpowder to add it to the fuel, or pump it in via capability.
    Right-click with a stack of anything that can be compressed/decompressed to add it to the
        inventory, or pump it in via capability.

**BLOCK STATES**

Blocks are orientable like furnaces, hence FACING block state.
Blocks have two textures for active/idle, like furnaces, hence ACTIVE block state.

**JAC (Just A Crowbar)**
```
I I X
I X X
I X X
```
I = Iron Ingot, X = Empty Space

Mostly exists for the pun, but has one use: breaking a decompressible block in-world with this
    will make it drop the compression ingredients instead of the block. For instance, breaking
    a diamond block will drop 9 diamonds, breaking a log will drop 4 planks, brick block drops
    4 brick items, etc.