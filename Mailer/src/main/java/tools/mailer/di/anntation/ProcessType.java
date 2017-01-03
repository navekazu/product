package tools.mailer.di.anntation;

public enum ProcessType {
    // Lifecycle event
    BOOT_APPLICATION,
    STARTED_APPLICATION,
    TERM_APPLICATION,

    // Application event
    SEND_MAIL,
    RECV_MAIL,

    //
    REFRESH_LIST,
    ACCOUTN_SETTING,
    
}
