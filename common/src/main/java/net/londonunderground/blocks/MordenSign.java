package net.londonunderground.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.londonunderground.MyBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MordenSign extends BlockRoundelBase {

	public MordenSign(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(-1.5, 0, 7.575, 17.5, 31, 8.425, state.getValue(FACING));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new MordenSign.TileEntityMordenSign(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class TileEntityMordenSign extends BlockRoundelBase.TileEntityBlockRoundelBase {

		public TileEntityMordenSign(BlockPos pos, BlockState state) {
			super(MyBlockEntityTypes.MORDEN_SIGN_TILE_ENTITY, pos, state);
		}

		@Override
		public boolean shouldRender() {
			return true;
		}
	}
}


