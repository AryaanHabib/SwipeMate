package delegates;

import model.*;

public interface UserPreferencesDelegate {
    void insertPreference(preferenceModel pref);
    void insertRequirement(requirementModel req);
    void insertSocial(socialModel social);
    void onSubmitSuccess(String username);
}
