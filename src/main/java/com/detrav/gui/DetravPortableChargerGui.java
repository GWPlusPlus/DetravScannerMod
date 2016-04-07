package com.detrav.gui;

import com.detrav.gui.containers.DetravPortableChargerContainer;
import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class DetravPortableChargerGui extends GuiContainer {
    public static final int GUI_ID = 30;

    ResourceLocation location = null;
    private String mName = "testName";
    ItemStack mItem = null;

    public DetravPortableChargerGui(InventoryPlayer player, World aWorld, ItemStack aStack) {
        super(new DetravPortableChargerContainer(player, aWorld, aStack));
        mItem = aStack;
        location = new ResourceLocation(RES_PATH_GUI + "1by1.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
        if(mItem!=null) {
            //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //EnumChatFormatting.AQUA + "" +  + EnumChatFormatting.GRAY);
            Long[] tStats = getElectricStats(mItem);
            long tCharge = getRealCharge(mItem);
            fontRendererObj.drawString(GT_Utility.formatNumbers(tCharge) + " / " + GT_Utility.formatNumbers(Math.abs(tStats[0])) + " EU - Voltage: " + V[(int) (tStats[2] >= 0 ? tStats[2] < V.length ? tStats[2] : V.length - 1 : 1)], 8, 15, 4210752);
        }
    }

    public Long[] getElectricStats(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null && aNBT.getBoolean("Electric"))
                return new Long[]{aNBT.getLong("MaxCharge"), aNBT.getLong("Voltage"), aNBT.getLong("Tier"), aNBT.getLong("SpecialData")};
        }
        return null;
    }

    public final long getRealCharge(ItemStack aStack) {
        Long[] tStats = getElectricStats(aStack);
        if (tStats == null) return 0;
        if (tStats[3] > 0) return (int) (long) tStats[3];
        NBTTagCompound tNBT = aStack.getTagCompound();
        return tNBT == null ? 0 : tNBT.getLong("GT.ItemCharge");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.renderEngine.bindTexture(location);
        if (GregTech_API.sColoredGUI && mItem != null && DetravMetaGeneratedTool01.getSecondaryMaterial(mItem) != null) {
            short[] tColors = DetravMetaGeneratedTool01.getSecondaryMaterial(mItem).mColor.getRGBA();
            GL11.glColor4f(tColors[0]/255F, tColors[1]/255F, tColors[2]/255F, 1F);
        } else GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
