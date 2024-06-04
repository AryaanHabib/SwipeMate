package delegates;

import model.listerModel;
import model.seekerModel;
import model.userModel;

import java.util.List;

public interface UserLoginDelegate {
    void insertUser(userModel user);
    boolean checkUserId(String userIdInput);
    List<String> getResIdsName();
    void insertLister(listerModel lister);
    void insertSeeker(seekerModel seeker);
    void onSignUpSuccess(String username);
    void onLoginSuccess(String username);
}
