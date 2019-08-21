package code.models;

public class BusinessTfidfHelper extends TfIdfHelper {
    private static BusinessTfidfHelper helper = new BusinessTfidfHelper(CategoryType.BUSINESS);

    public static BusinessTfidfHelper getInstance(){
        return helper;
    }

    private BusinessTfidfHelper(CategoryType type){
        super(type);
    }
}
