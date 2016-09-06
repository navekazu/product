package tools.dbcomparator2.enums;

public enum CompareType {
    IMMEDIATE("Immediate", true),
    PIVOT("Pivot", false),
    ROTATION("Rotation", false);

    private String label;

    /**
     * データベースの再読み込みが可能か？.
     * 可能なら比較結果を表示する際にデータベースを読み込み、不可ならプライマリーキーのみ表示する
     */
    private boolean reloadableDatabase;

    CompareType(String label, boolean reloadableDatabase) {
        this.label = label;
        this.reloadableDatabase = reloadableDatabase;
    }

    public String toString() {
        return label;
    }
}
