package tools.dbcomparator2.enums;

public enum CompareType {
    IMMEDIATE("Immediate"),
    PIVOT("Pivot"),
    ROTATION("Rotation");

    private String label;

    CompareType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
