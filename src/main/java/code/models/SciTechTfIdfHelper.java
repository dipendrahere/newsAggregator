package code.models;

public class SciTechTfIdfHelper extends TfIdfHelper {

    private static SciTechTfIdfHelper helper = new SciTechTfIdfHelper(CategoryType.SPORTS);

    public static SciTechTfIdfHelper getInstance(){
        return helper;
    }

    private SciTechTfIdfHelper(CategoryType type){
        super(type);
    }
}
