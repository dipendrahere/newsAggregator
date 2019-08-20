package code.models;

public enum CategoryType {
    SPORTS(1),
    BUSINESS(2),
    WORLD(3),
    SCITECH(4);

    public final int value;
    private CategoryType(int label) {
        this.value = label;
    }
}
