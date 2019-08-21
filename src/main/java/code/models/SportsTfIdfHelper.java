package code.models;

public class SportsTfIdfHelper extends TfIdfHelper{
    private static SportsTfIdfHelper helper = new SportsTfIdfHelper(CategoryType.SPORTS);

    public static SportsTfIdfHelper getInstance(){
        return helper;
    }

    private SportsTfIdfHelper(CategoryType type){
        super(type);
    }
}
