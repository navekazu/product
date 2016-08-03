package tools.dbcomparator2.entity;


import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class ConnectEntity {
    public final String library;
    public final String driver;
    public final String url;
    public final String user;
    public final String password;
    public final String schema;

    public static ConnectEntityBuilder copyBuilder(ConnectEntity entity) {
        return ConnectEntity.builder()
                .library(entity.library)
                .driver(entity.driver)
                .url(entity.url)
                .user(entity.user)
                .password(entity.password)
                .schema(entity.schema);
    }

}
