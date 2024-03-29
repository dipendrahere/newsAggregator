package code.idfHelper;

import code.models.CategoryType;

public class WorldTfIdfHelper extends TfIdfHelper {
    private static WorldTfIdfHelper helper = new WorldTfIdfHelper(CategoryType.WORLD);

    public static WorldTfIdfHelper getInstance(){
        return helper;
    }

    private WorldTfIdfHelper(CategoryType type){
        super(type);
    }
}
