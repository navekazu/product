package info.studyup.studyupserver;

import java.io.InputStream;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;

public class ReladomoInitializer {
    private static final String RUNTIME_CONFIG = "MithraRuntimeConfig_%s.xml";
    private static final int MAX_TRANSACTION_TIMEOUT = 120;

    public static void init(String runtime) throws Exception {
        try (InputStream is = ReladomoInitializer.class.getClassLoader()
                .getResourceAsStream(String.format(RUNTIME_CONFIG, (runtime==null? "local": runtime)))) {
            if (is == null) {
                throw new Exception("can't find file: " + RUNTIME_CONFIG + " in classpath");
            }

            MithraManagerProvider.getMithraManager().readConfiguration(is);
            MithraManager mithraManager = MithraManagerProvider.getMithraManager();
            mithraManager.setTransactionTimeout(MAX_TRANSACTION_TIMEOUT);
        }
    }
}
