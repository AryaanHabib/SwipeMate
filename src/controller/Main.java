// FOLLOWS THE STRUCTURE AND FORMAT OF BANK PROJECT FROM https://www.students.cs.ubc.ca/~cs-304/resources/jdbc-oracle-resources/jdbc-java-setup.html
package controller;

import database.DatabaseConnectionHandler;
import delegates.DashBoardDelegate;
import delegates.LoginWindowDelegate;
import delegates.UserLoginDelegate;
import delegates.UserPreferencesDelegate;
import model.*;
import ui.Dashboard;
import ui.LoginWindow;
import ui.UserLogin;
import ui.UserPreferencesPage;

import java.util.List;

public class Main implements LoginWindowDelegate, UserLoginDelegate, UserPreferencesDelegate, DashBoardDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private UserLogin userLogin = null;
    private UserPreferencesPage userPreferencesPage;
    private Dashboard dashboard = null;



    public static void main(String[] args) {
        Main mainController = new Main();
        mainController.start();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
        dbHandler = new DatabaseConnectionHandler();
        userLogin = new UserLogin();
    }

    @Override
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            loginWindow.dispose();
            userLogin.showFrame(this);


        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    @Override
    public void insertUser(userModel user) {
        dbHandler.insertUser(user);
    }

    @Override
    public boolean checkUserId(String userIdInput) {
        return dbHandler.checkUserId(userIdInput);
    }

    @Override
    public List<String> getResIdsName() {
        return dbHandler.getResIdsName();
    }

    @Override
    public void insertLister(listerModel lister) {
        dbHandler.insertLister(lister);
    }

    @Override
    public void insertSeeker(seekerModel seeker) {
        dbHandler.insertSeeker(seeker);
    }

    @Override
    public void onSignUpSuccess(String username) {
        userLogin.dispose();
        userPreferencesPage = new UserPreferencesPage();
        userPreferencesPage.showFrame(this, username);
    }

    @Override
    public void onLoginSuccess(String username) {
        userLogin.dispose();
        dashboard = new Dashboard();
        dashboard.showFrame(this, username);
    }

    @Override
    public void insertPreference(preferenceModel pref) {
        dbHandler.insertPreference(pref);
    }

    @Override
    public void insertRequirement(requirementModel req) {
        dbHandler.insertRequirement(req);
    }

    @Override
    public void insertSocial(socialModel social) {
        dbHandler.insertSocial(social);
    }

    @Override
    public void onSubmitSuccess(String username) {
        userPreferencesPage.dispose();
        dashboard = new Dashboard();
        dashboard.showFrame(this, username);
    }


    @Override
    public void deleteUser(String userID) {
        dbHandler.deleteUser(userID);
    }

    @Override
    public void updateSocialPageHas(String userID, String newEmailID, String newPhoneNumber, String newInstagramUsername) {
        dbHandler.updateSocialPageHas(userID, newEmailID, newPhoneNumber, newInstagramUsername);
    }

    @Override
    public List<List<Object>> queryResidences(String whereClause) {
        return dbHandler.queryResidences(whereClause);
    }

    @Override
    public List<List<Object>> join() {
        return dbHandler.join();
    }

    @Override
    public List<List<Object>> aggGroupBy() {
        return dbHandler.aggGroupBy();
    }

    @Override
    public List<List<Object>> aggHavingWithJoin() {
        return dbHandler.aggHavingWithJoin();
    }

    @Override
    public List<List<Object>> nestedAggGroupBy() {
        return dbHandler.nestedAggGroupBy();
    }

    @Override
    public List<List<Object>> performDivisionQuery() {
        return dbHandler.performDivisionQuery();
    }

    @Override
    public List<List<Object>> projectUser(String query) {
        return dbHandler.projectUser(query);
    }
}
