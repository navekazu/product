package tools.directorymirroringtool.process;

public enum MirroringStatus {
    WAITING("Waiting"),
    RUNNING("Running"),
    STOP("Stop"),
    ;

    String label;
    MirroringStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
