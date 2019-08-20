package code.models;

public enum CategoryType {
    SPORTS(new CategoryFileMapping("src/main/resources/rssSports.txt", 1)),
    BUSINESS(new CategoryFileMapping("src/main/resources/rssBusiness.txt", 2)),
    WORLD(new CategoryFileMapping("src/main/resources/rssWorld.txt", 3)),
    SCITECH(new CategoryFileMapping("src/main/resources/rssSciTech.txt", 4));

    public final CategoryFileMapping value;
    private CategoryType(CategoryFileMapping obj) {
        this.value = obj;
    }
}
