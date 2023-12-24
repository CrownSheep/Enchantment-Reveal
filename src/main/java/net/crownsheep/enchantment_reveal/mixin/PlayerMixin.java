package net.crownsheep.enchantment_reveal.mixin;


import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getEnchantmentSeed", at = @At("TAIL"), cancellable = true)
    public void seedCorrectForServer(CallbackInfoReturnable<Integer> ci){
        ci.setReturnValue((int)((short)ci.getReturnValueI()));
    }
}
