package net.fightingpainter.mc.towntime.coined.blocks.custom.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.coined.blocks.custom.ShopBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class ShopBlockEntityRenderer implements BlockEntityRenderer<ShopBlockEntity> {

    public ShopBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShopBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = blockEntity.getStoredItem();
        if (stack.isEmpty()) return; //No item to render

        // Get the item renderer
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Move the item to appear above the block
        poseStack.pushPose();
        poseStack.translate(0.5, 0.3, 0.5); // Center above block (x, y, z)
        poseStack.scale(1f, 1f, 1f); // Scale down the item
        poseStack.mulPose(Axis.YP.rotationDegrees((System.currentTimeMillis() / 20) % 360)); // Rotate slowly

        // Render the item
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);

        poseStack.popPose();
    }
}
