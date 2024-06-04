package ui;

import delegates.UserPreferencesDelegate;
import model.preferenceModel;
import model.requirementModel;
import model.socialModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class UserPreferencesPage extends JFrame {
    private UserPreferencesDelegate delegate;
    private String username;
    private JComboBox<String> ageRangeComboBox, locationComboBox, genderComboBox, lifestyleComboBox;
    private JComboBox<String> agePreferenceComboBox, locationPreferenceComboBox, genderPreferenceComboBox, lifestylePreferenceComboBox;
    private JTextField emailField, phoneNumberField, instaHandleField;
    private JButton submitButton;

    public UserPreferencesPage() {
        super("User Preferences and Socials");
        initializeComponents();
    }

    public void showFrame(UserPreferencesDelegate delegate, String username) {
        this.delegate = delegate;
        this.username = username;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addPreferencesSection();
        addSocialsSection();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initializeComponents() {
        ageRangeComboBox = new JComboBox<>(new String[]{"18-25", "26-30", "31-40", "41-50", "50+"});
        locationComboBox = new JComboBox<>(new String[]{"On Campus", "Off Campus"});
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        lifestyleComboBox = new JComboBox<>(new String[]{"Active", "Quiet"});

        String[] preferencesOptions = {"Preference", "Requirement", "No Preference"};
        agePreferenceComboBox = new JComboBox<>(preferencesOptions);
        locationPreferenceComboBox = new JComboBox<>(preferencesOptions);
        genderPreferenceComboBox = new JComboBox<>(preferencesOptions);
        lifestylePreferenceComboBox = new JComboBox<>(preferencesOptions);

        emailField = new JTextField(20);
        phoneNumberField = new JTextField(20);
        instaHandleField = new JTextField(20);

        submitButton = new JButton("Submit");
    }

    private void addPreferencesSection() {
        JPanel preferencesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        String[] labels = {"Age Range", "Location", "Gender", "Lifestyle"};
        JComboBox[] attributes = {ageRangeComboBox, locationComboBox, genderComboBox, lifestyleComboBox};
        JComboBox[] preferences = {agePreferenceComboBox, locationPreferenceComboBox, genderPreferenceComboBox, lifestylePreferenceComboBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            preferencesPanel.add(new JLabel(labels[i] + ":"), gbc);

            gbc.gridx = 1;
            preferencesPanel.add(attributes[i], gbc);

            gbc.gridx = 2;
            preferencesPanel.add(preferences[i], gbc);
        }

        this.add(preferencesPanel);
    }

    private void addSocialsSection() {
        JPanel socialsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 10);

        socialsPanel.add(new JLabel("Email:"), gbc);
        socialsPanel.add(emailField, gbc);

        socialsPanel.add(new JLabel("Phone Number:"), gbc);
        socialsPanel.add(phoneNumberField, gbc);

        socialsPanel.add(new JLabel("Instagram Handle:"), gbc);
        socialsPanel.add(instaHandleField, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ageRange = (String)ageRangeComboBox.getSelectedItem();
                String location = (String)locationComboBox.getSelectedItem();
                String gender = (String)genderComboBox.getSelectedItem();
                String lifestyle = (String)lifestyleComboBox.getSelectedItem();

                String prefAgeRange = (String)agePreferenceComboBox.getSelectedItem();
                String prefLocation = (String)locationPreferenceComboBox.getSelectedItem();
                String prefLifestyle = (String)lifestylePreferenceComboBox.getSelectedItem();
                String prefGender = (String)genderPreferenceComboBox.getSelectedItem();

                String pAgeRange;
                String pLocation;
                String pGender;
                String pLifestyle;

                String rAgeRange;
                String rLocation;
                String rGender;
                String rLifestyle;

                if (Objects.equals(prefAgeRange, "Preference")) {
                    pAgeRange = ageRange;
                    rAgeRange = null;
                } else if (Objects.equals(prefAgeRange, "Requirement")) {
                    rAgeRange = ageRange;
                    pAgeRange = null;
                } else {
                    rAgeRange = null;
                    pAgeRange = null;
                }

                if (Objects.equals(prefLocation, "Preference")) {
                    pLocation = location;
                    rLocation = null;
                } else if (Objects.equals(prefLocation, "Requirement")) {
                    rLocation = location;
                    pLocation = null;
                } else {
                    rLocation = null;
                    pLocation = null;
                }

                if (Objects.equals(prefLifestyle, "Preference")) {
                    pLifestyle = lifestyle;
                    rLifestyle = null;
                } else if (Objects.equals(prefLifestyle, "Requirement")) {
                    rLifestyle = lifestyle;
                    pLifestyle = null;
                } else {
                    rLifestyle = null;
                    pLifestyle = null;
                }

                if (Objects.equals(prefGender, "Preference")) {
                    pGender = gender;
                    rGender = null;
                } else if (Objects.equals(prefGender, "Requirement")) {
                    rGender = gender;
                    pGender = null;
                } else {
                    rGender = null;
                    pGender = null;
                }

                preferenceModel preference = new preferenceModel(username, pGender, pAgeRange, pLifestyle, pLocation);
                requirementModel requirement = new requirementModel(username, rGender, rAgeRange, rLifestyle, rLocation);
                delegate.insertPreference(preference);
                delegate.insertRequirement(requirement);

                String email = (String) emailField.getText();
                String phoneNumber = (String) phoneNumberField.getText();
                String instaHandle = (String) instaHandleField.getText();

                socialModel social = new socialModel(email, username, phoneNumber, instaHandle);
                delegate.insertSocial(social);

                delegate.onSubmitSuccess(username);
            }
        });
        socialsPanel.add(submitButton, gbc);

        this.add(socialsPanel);
    }
}
