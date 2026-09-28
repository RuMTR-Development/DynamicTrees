package com.dtteam.dynamictrees.platform;

import com.dtteam.dynamictrees.platform.services.ICompatHelper;

//? if serene_seasons {
import com.dtteam.dynamictrees.compat.SereneSeasonsSeasonProvider;
//? }

public class FabricCompatHelper implements ICompatHelper {

    @Override
    public void registerSereneSeasonsSeasonProvider() {
        //? if serene_seasons {
        SereneSeasonsSeasonProvider.registerSereneSeasonsProvider();
        //? }
    }

}