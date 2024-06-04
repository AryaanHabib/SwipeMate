package delegates;

import java.util.List;

public interface DashBoardDelegate {
    void deleteUser(String userID);
    void updateSocialPageHas(String userID, String newEmailID, String newPhoneNumber, String newInstagramUsername);
    List<List<Object>> queryResidences(String whereClause);
    List<List<Object>> join();
    List<List<Object>> aggGroupBy();
    List<List<Object>> aggHavingWithJoin();
    List<List<Object>> nestedAggGroupBy();
    List<List<Object>> performDivisionQuery();
    List<List<Object>> projectUser(String query);
}
