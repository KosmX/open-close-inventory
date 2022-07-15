package io.github.kosmx.inventoryFix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    public abstract void onClose();


    @Shadow protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Shadow @Nullable protected Slot focusedSlot;

    @Inject(method = "mouseClicked", at = @At(value = "HEAD"), cancellable = true)
    private void closeInventory(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().options.keyInventory.matchesMouse(button)) {
            this.onClose();
            cir.setReturnValue(true);
            cir.cancel();
        } else if (this.focusedSlot != null && MinecraftClient.getInstance().options.keyDrop.matchesMouse(button)) {
            this.onMouseClick(this.focusedSlot, this.focusedSlot.id, Screen.hasControlDown() ? 1 : 0, SlotActionType.THROW);
        }
    }
}
