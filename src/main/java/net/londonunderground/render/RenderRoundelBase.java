package net.londonunderground.render;

import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.render.RenderTrains;
import net.londonunderground.blocks.BlockRoundelBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

public abstract class RenderRoundelBase<T extends BlockRoundelBase.TileEntityBlockRoundelBase> extends BlockEntityRenderer<T> implements IGui, IDrawing {

    public RenderRoundelBase(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.shouldRender()) {
            return;
        }

        final WorldAccess world = entity.getWorld();
        if (world == null) {
            return;
        }

        final BlockPos pos = entity.getPos();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockRoundelBase.FACING);
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
            return;
        }

        final int color;
        switch (IBlock.getStatePropertySafe(state, BlockRoundelBase.COLOR)) {
            case 1:
                color = ARGB_LIGHT_GRAY;
                break;
            case 2:
                color = ARGB_BLACK;
                break;
            default:
                color = ARGB_WHITE;
                break;
        }

        matrices.push();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
        final Station station = ClientData.getStation(pos);
        final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        drawStationName(entity, matrices, vertexConsumers, immediate, station == null ? new TranslatableText("gui.mtr.untitled").getString() : station.name, color, light);
        immediate.draw();
        matrices.pop();
    }

    protected abstract void drawStationName(BlockRoundelBase.TileEntityBlockRoundelBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, String stationName, int color, int light);
}