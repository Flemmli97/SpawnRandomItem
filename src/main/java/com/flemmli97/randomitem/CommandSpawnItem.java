package com.flemmli97.randomitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CommandSpawnItem implements ICommand {

    private final List<String> aliases = new ArrayList<String>();

    public CommandSpawnItem() {
        this.aliases.add("spawnItem");
    }

    @Override
    public String getName() {
        return "spawnItem";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "spawnItem x y z <nbt>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        BlockPos blockpos = sender.getPosition();
        Vec3d vec3d = sender.getPositionVector();
        double x = vec3d.x;
        double y = vec3d.y;
        double z = vec3d.z;

        if(args.length >= 3){
            x = CommandBase.parseDouble(x, args[0], true);
            y = CommandBase.parseDouble(y, args[1], false);
            z = CommandBase.parseDouble(z, args[2], true);
            blockpos = new BlockPos(x, y, z);
        }

        World world = sender.getEntityWorld();

        if(!world.isBlockLoaded(blockpos)){
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }

        NBTTagCompound nbttagcompound = null;
        if(args.length >= 4){
            String s1 = CommandBase.buildString(args, 4);
            try{
                nbttagcompound = JsonToNBT.getTagFromJson(s1);
            }catch(NBTException nbtexception){
                throw new CommandException("commands.summon.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        ItemStack stack = Config.getRandomItem(world.rand);

        if(nbttagcompound != null)
            if(stack.hasTagCompound())
                stack.getTagCompound().merge(nbttagcompound);
            else
                stack.setTagCompound(nbttagcompound);
        EntityItem item = new EntityItem(world, x, y, z, stack);
        world.spawnEntity(item);
        CommandBase.notifyCommandListener(sender, this, "commands.summon.success", new Object[0]);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(2, this.getName());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        return args.length > 0 && args.length <= 3 ? CommandBase.getTabCompletionCoordinate(args, 0, targetPos) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
