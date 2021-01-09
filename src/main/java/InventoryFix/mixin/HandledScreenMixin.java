package InventoryFix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow public abstract void onClose();

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE"), cancellable = true)
    private void closeInventory(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        if(MinecraftClient.getInstance().options.keyInventory.matchesMouse(button)){
            this.onClose();
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
