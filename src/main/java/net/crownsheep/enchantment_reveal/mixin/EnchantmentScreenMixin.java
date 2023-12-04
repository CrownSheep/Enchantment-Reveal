package net.crownsheep.enchantment_reveal.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends AbstractContainerScreen {
    @Shadow
    @Final
    private RandomSource random;
    EnchantmentScreen enchantmentScreen = (EnchantmentScreen) (Object) (this);

    int x;
    int y;

    public EnchantmentScreenMixin(AbstractContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean addToList(List instance, Object e) {
        for (int i = 0; i < 3; i++) {
            if (this.isHovering(60, 14 + 19 * i, 108, 17, x, y)) {
                List<EnchantmentInstance> list;
                list = getEnchantmentList(enchantmentScreen.getMenu().getSlot(0).getItem(), i, enchantmentScreen.getMenu().costs[i]);
                for (EnchantmentInstance enchantmentInstance : list) {
                    instance.add(enchantmentInstance.enchantment.getFullname(enchantmentInstance.level).copy().withStyle(ChatFormatting.GRAY));
                }
            }
        }
        return true;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EnchantmentScreen;isHovering(IIIIDD)Z"))
    private void isHoveringGetter(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        x = pMouseX;
        y = pMouseY;
    }

    private List<EnchantmentInstance> getEnchantmentList(ItemStack p_39472_, int p_39473_, int p_39474_) {
        this.random.setSeed((long) (enchantmentScreen.getMenu().getEnchantmentSeed() + p_39473_));
        List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(this.random, p_39472_, p_39474_, false);
        if (p_39472_.is(Items.BOOK) && list.size() > 1) {
            list.remove(this.random.nextInt(list.size()));
        }

        return list;
    }
}
