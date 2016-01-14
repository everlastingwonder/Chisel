package team.chisel.client.render.texture;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeCTMV;
import team.chisel.common.util.Dir;

import com.google.common.collect.Lists;

public class ChiselTextureCTMV extends AbstractChiselTexture<BlockRenderTypeCTMV> {

    public ChiselTextureCTMV(BlockRenderTypeCTMV type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int target) {
        if (side.getAxis().isVertical()) {
            return Lists.newArrayList(QuadHelper.makeNormalFaceQuad(side, sprites[0].getSprite()));
        } else {
            if (context == null) {
                return Lists.newArrayList(QuadHelper.makeUVFaceQuad(side, sprites[1].getSprite(), new float[] { 0, 0, 8, 8 }));
            }
            CTM ctm = ((CTMBlockRenderContext) context).getCTM(side);
            return Lists.newArrayList(getQuad(side, ctm));
        }
    }

    private BakedQuad getQuad(EnumFacing side, CTM ctm) {
        if (ctm.connectedAnd(Dir.TOP, Dir.BOTTOM)) {
            return QuadHelper.makeUVFaceQuad(side, sprites[1].getSprite(), QuadHelper.UVS_BOTTOM_LEFT);
        } else if (ctm.connected(Dir.TOP)) {
            return QuadHelper.makeUVFaceQuad(side, sprites[1].getSprite(), QuadHelper.UVS_BOTTOM_RIGHT);
        } else if (ctm.connected(Dir.BOTTOM)) {
            return QuadHelper.makeUVFaceQuad(side, sprites[1].getSprite(), QuadHelper.UVS_TOP_RIGHT);
        } else {
            return QuadHelper.makeUVFaceQuad(side, sprites[1].getSprite(), QuadHelper.UVS_TOP_LEFT);
        }
    }
}
