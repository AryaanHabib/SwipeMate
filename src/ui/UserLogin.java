package ui;

import delegates.UserLoginDelegate;
import model.listerModel;
import model.seekerModel;
import model.userModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class UserLogin extends JFrame {

    private UserLoginDelegate delegate;
    private JTextField usernameLoginField;
    private JButton loginButton;
    private JTextField usernameSignupField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextArea bioText;
    private JComboBox<String> userTypeComboBox;
    private JButton signupButton;
    private JPanel cards;
    private CardLayout cardLayout;
    private JComboBox<String> seekerTypeComboBox;
    private JComboBox<String> listerTypeComboBox;
    private JComboBox<String> residenceComboBox;
    private UserPreferencesPage userPreferencesPage;

    public UserLogin() {
        super("User Login/Sign Up");
        initializeComponents();
        beautifyUI();
    }

    public void showFrame(UserLoginDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addLoginSection();
        addSectionDivider();
        addSignupSection();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initializeComponents() {
        usernameLoginField = new JTextField(20);
        loginButton = new JButton("Log In");
        usernameSignupField = new JTextField(20);
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Gender", "Male", "Female", "Other"});
        bioText = new JTextArea(5, 20);
        userTypeComboBox = new JComboBox<>(new String[]{"Select User Type", "Seeker", "Lister"});
        userTypeComboBox.setSelectedItem("Seeker");
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        seekerTypeComboBox = new JComboBox<>(new String[]{"On Campus", "Off Campus"});
        listerTypeComboBox = new JComboBox<>(new String[]{"On Campus", "Off Campus"});
        residenceComboBox = new JComboBox<>(new String[]{"Select Residence"});
        signupButton = new JButton("Sign Up");
    }

    private void addLoginSection() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());

        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(usernameLoginField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameLoginField.getText();
                System.out.println(username);
                if (delegate.checkUserId(username)) {
                    System.out.println("Exists");
                    delegate.onLoginSuccess(username);
                } else {
                    System.out.println("Not Exists");
                }
            }
        });

        this.add(loginPanel);
    }

    private void addSectionDivider() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        this.add(separator);
    }

    private void addSignupSection() {
        String[] residences = delegate.getResIdsName().toArray(new String[0]);
        residenceComboBox = new JComboBox<>(residences);
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        signupPanel.add(new JLabel("Sign Up"), gbc);
        signupPanel.add(new JLabel("Username: "), gbc);
        signupPanel.add(usernameSignupField, gbc);
        signupPanel.add(new JLabel("Full Name: "), gbc);
        signupPanel.add(nameField, gbc);
        JLabel ageLabel = new JLabel("Age: ");
        signupPanel.add(ageLabel, gbc);
        signupPanel.add(ageField, gbc);
        signupPanel.add(new JLabel("Gender: "), gbc);
        signupPanel.add(genderComboBox, gbc);
        signupPanel.add(new JLabel("Bio: "), gbc);
        bioText.setLineWrap(true);
        bioText.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioText);
        signupPanel.add(bioScrollPane, gbc);
        JPanel seekerPanel = new JPanel(new FlowLayout());
        seekerPanel.add(new JLabel("Seeker Type: "));
        seekerPanel.add(seekerTypeComboBox);
        JPanel listerPanel = new JPanel(new FlowLayout());
        listerPanel.add(new JLabel("Lister Type: "));
        listerPanel.add(listerTypeComboBox);
        listerPanel.add(new JLabel("Residence: "));
        listerPanel.add(residenceComboBox);

        cards.add(seekerPanel, "Seeker");
        cards.add(listerPanel, "Lister");
        signupPanel.add(new JLabel("User Type: "), gbc);
        signupPanel.add(userTypeComboBox, gbc);
        signupPanel.add(cards, gbc);
        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userType = (String) userTypeComboBox.getSelectedItem();
                switch (userType) {
                    case "Seeker":
                        cardLayout.show(cards, "Seeker");
                        break;
                    case "Lister":
                        cardLayout.show(cards, "Lister");
                        break;
                    default:
                        cardLayout.show(cards, "Seeker");
                        break;
                }
            }
        });
        signupPanel.add(signupButton, gbc);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameSignupField.getText();
                String name = nameField.getText();
                String ageText = ageField.getText();
                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(ageField, "Please enter a valid integer for age.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    ageField.requestFocus();
                }
                String gender = (String) genderComboBox.getSelectedItem();
                String bio = bioText.getText();

                userModel user = new userModel(username, name, gender, bio, age);
                System.out.println(user.getUserID() + user.getAge() + user.getBio() + user.getName() + user.getGender());
                delegate.insertUser(user);

                if (Objects.equals((String) userTypeComboBox.getSelectedItem(), "Seeker")) {
                    String seekerType = (String) seekerTypeComboBox.getSelectedItem();
                    seekerModel seeker = new seekerModel(username, seekerType);
                    delegate.insertSeeker(seeker);
                } else if (Objects.equals((String) userTypeComboBox.getSelectedItem(), "Lister")) {
                    String listerType = (String) listerTypeComboBox.getSelectedItem();
                    String residence = ((String) residenceComboBox.getSelectedItem()).substring(0, 5);
                    listerModel lister = new listerModel(username, listerType, residence);
                    delegate.insertLister(lister);
                }

                delegate.onSignUpSuccess(username);
            }
        });

        this.add(signupPanel);
    }
    private void beautifyUI() {
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14); // Example of a stylish font
        Color backgroundColor = new Color(240, 240, 240); // Light background color

        // Apply font to all components
        setFontForAllComponents(this.getContentPane(), defaultFont);

        // Set background color
        this.getContentPane().setBackground(backgroundColor);

        // Set button gradients
        GradientButtonUI buttonUI = new GradientButtonUI();
        loginButton.setUI(buttonUI);
        signupButton.setUI(buttonUI);

        // Set padding for text fields
        Insets padding = new Insets(5, 10, 5, 10);
        usernameLoginField.setMargin(padding);
        usernameSignupField.setMargin(padding);
        nameField.setMargin(padding);
        ageField.setMargin(padding);
        bioText.setMargin(padding);

        // Set padding for combo boxes
        setComboBoxPadding(genderComboBox, padding);
        setComboBoxPadding(userTypeComboBox, padding);
        setComboBoxPadding(seekerTypeComboBox, padding);
        setComboBoxPadding(listerTypeComboBox, padding);
        setComboBoxPadding(residenceComboBox, padding);
    }

    private void setComboBoxPadding(JComboBox<?> comboBox, Insets padding) {
        ListCellRenderer<Object> renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right));
                return label;
            }
        };
        comboBox.setRenderer(renderer);
    }

    private void setFontForAllComponents(Container container, Font font) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                setFontForAllComponents((Container) comp, font);
            }
            comp.setFont(font);
        }
    }

    // Custom button UI for gradient effect
    class GradientButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(51, 153, 255), 0, c.getHeight(), new Color(0, 102, 204));
            g2d.setPaint(gradientPaint);
            g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2d.dispose();
            super.paint(g, c);
        }
    }

}
