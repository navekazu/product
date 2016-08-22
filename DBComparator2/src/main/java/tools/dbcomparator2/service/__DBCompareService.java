package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.enums.DBCompareStatus;

import java.util.ArrayList;
import java.util.List;

public class __DBCompareService {
    Logger logger = LoggerFactory.getLogger(__DBCompareService.class);

    private static final int MAX_COMPARE_COUNT = 2;
    private List<DBCompareEntity> dbCompareEntityList;
    private __DBParseService dbParseService;

    public __DBCompareService() {
        dbCompareEntityList = new ArrayList<>();
        dbParseService = new __DBParseService();
    }

    public void startCompare(ConnectEntity connectEntity) {
        dbCompareEntityList.clear();
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(connectEntity).build());
        startCompare();
    }

    public void startCompare(ConnectEntity firstConnectEntity, ConnectEntity secondConnectEntity) {
        dbCompareEntityList.clear();
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(firstConnectEntity).build());
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(secondConnectEntity).build());
        startCompare();
    }

    public void restartCompare(ConnectEntity connectEntity) {
        // MAX_COMPARE_COUNT未満になるよう、先頭要素から削除する
        while (true) {
            if (dbCompareEntityList.size()<=(MAX_COMPARE_COUNT-1)) {
                break;
            }
            dbCompareEntityList.remove(0);
        }
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(connectEntity).build());

        // 古い結果を削除して、最新の結果と現在の状態を比較するようパラメータを作って比較を開始する
        startCompare();
    }

    private void startCompare() {
        // 事前準備
        dbCompareEntityList.parallelStream().forEach(dbParseService::prepare);

        // DB解析開始
        dbCompareEntityList.parallelStream().forEach(dbParseService::startParse);

        // 比較対象が1つなら終了
        if (dbCompareEntityList.size()<=1) {
            return ;
        }

        // 全部解析終了まで待機
        waitForStatus(DBCompareStatus.SCAN_FINISHED);
        logger.info("DB scan finished.");
    }

    private void waitForStatus(DBCompareStatus status) {
        while (true) {
            boolean notFinish = false;
            for (DBCompareEntity entity: dbCompareEntityList) {
                if (entity.getStatus()!=status) {
                    notFinish = true;
                    break;
                }
            }
            if (!notFinish) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
