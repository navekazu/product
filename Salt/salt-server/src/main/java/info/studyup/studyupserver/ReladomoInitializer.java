package info.studyup.studyupserver;

import java.io.InputStream;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;

public class ReladomoInitializer {
    private static final String RUNTIME_CONFIG = "MithraRuntimeConfig.xml";
    private static final int MAX_TRANSACTION_TIMEOUT = 120;

    public static void init() throws Exception {
        try (InputStream is = ReladomoInitializer.class.getClassLoader().getResourceAsStream(RUNTIME_CONFIG)) {
            if (is == null) {
                throw new Exception("can't find file: " + RUNTIME_CONFIG + " in classpath");
            }

            MithraManagerProvider.getMithraManager().readConfiguration(is);
            MithraManager mithraManager = MithraManagerProvider.getMithraManager();
            mithraManager.setTransactionTimeout(MAX_TRANSACTION_TIMEOUT);
        }
    }
}
