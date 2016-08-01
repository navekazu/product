package tools.dbcomparator2.service;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;

import java.util.ArrayList;
import java.util.List;

public class DBCompareService {
    private static final int MAX_COMPARE_COUNT = 2;
    private List<DBCompareEntity> dbCompareEntityList;

    public DBCompareService() {
        dbCompareEntityList = new ArrayList<>();
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

    }
}
