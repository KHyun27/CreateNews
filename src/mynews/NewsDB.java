package mynews;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewsDB {

    private DatabaseConnector dbConnector;

    public NewsDB(DatabaseConnector db) {
        dbConnector = db;
    }

    public long queryCountryId(String countryCode) {
        String sql = String.format("select * from country where c_code='%s';", countryCode);
        try (ResultSet rs = dbConnector.getStatement().executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong("id");    // 성공
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;   // 실패
    }

    public long queryCategoryId(String categoryCode) {
        String sql = String.format("select * from category where ct_code='%s';", categoryCode);
        try (ResultSet rs = dbConnector.getStatement().executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public long querySourceId(String name) throws RuntimeException {
        String sql = String.format("select * from source where s_name='%s';", name);
        try (ResultSet rs = dbConnector.getStatement().executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong("id");
            }

            // 해당 데이터가 없는 경우에는 새로 추가를 해주고 그에 대한 id를 다시 쿼리한다.
            sql = String.format("insert into source(s_name) values('%s');", name);
            int ret = dbConnector.getStatement().executeUpdate(sql);
            if (ret == 0) {
                return 0; // 실패
            }

            sql = String.format("select * from source where s_name='%s';", name);
            try (ResultSet rs2 = dbConnector.getStatement().executeQuery(sql)) {
                if (rs2.next()) {
                    return rs2.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
