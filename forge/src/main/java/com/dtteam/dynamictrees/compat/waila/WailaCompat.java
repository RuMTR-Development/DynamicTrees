package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.branch.TrunkShellBlock;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import com.dtteam.dynamictrees.block.pod.PodBlock;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.WaterSoilProperties;

//? if >= 1.19.2 {
/*import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
*///? } else {
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;import mcp.mobius.waila.api.WailaPlugin;
//? }

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        WailaBranchHandler branchHandler = new WailaBranchHandler();

        //? if >= 1.19.2 {
        /*registration.registerBlockComponent(branchHandler, BranchBlock.class);
        registration.registerBlockComponent(branchHandler, TrunkShellBlock.class);

        registration.registerBlockComponent(new WailaFruitHandler(), FruitBlock.class);
        registration.registerBlockComponent(new WailaPodHandler(), PodBlock.class);
        registration.registerBlockComponent(new WailaRootyHandler(), SoilBlock.class);
        registration.registerBlockComponent(new WailaRootyWaterHandler(), WaterSoilProperties.SoilWaterBlock.class);
        *///? } else {
        registration.registerComponentProvider(branchHandler, TooltipPosition.BODY, BranchBlock.class);
        registration.registerComponentProvider(branchHandler, TooltipPosition.BODY, TrunkShellBlock.class);

        registration.registerComponentProvider(new WailaFruitHandler(), TooltipPosition.BODY, FruitBlock.class);
        registration.registerComponentProvider(new WailaPodHandler(), TooltipPosition.BODY, PodBlock.class);
        registration.registerComponentProvider(new WailaRootyHandler(), TooltipPosition.BODY, SoilBlock.class);
        registration.registerComponentProvider(new WailaRootyWaterHandler(), TooltipPosition.BODY, WaterSoilProperties.SoilWaterBlock.class);
        //? }
    }

}
