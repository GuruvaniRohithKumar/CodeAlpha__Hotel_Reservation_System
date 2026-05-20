import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class MainGUI {
    private HotelSystem system;
    private JFrame mainFrame;
    private JTextArea displayArea;
    private JTextField nameField, phoneField, roomField, nightsField; 
    private JComboBox<String> typeDropdown; 
    
    private JComboBox<String> payDropdown;
    private JPanel dynamicPayFieldsContainer;
    private JTextField cardField;
    private JTextField upiIdField;

    private final String ADMIN_PASSWORD = "admin123"; 

    private final Color COLOR_HEADER_BG    = new Color(30, 39, 46);   
    private final Color COLOR_APP_BG       = new Color(241, 242, 246); 
    private final Color COLOR_CARD_BG      = new Color(255, 255, 255); 
    private final Color COLOR_TEXT_MAIN    = new Color(47, 53, 66);    
    private final Color COLOR_GOLD         = new Color(212, 175, 55);  
    private final Color COLOR_BTN_BOOK     = new Color(38, 166, 91);   
    private final Color COLOR_CONSOLE_BG   = new Color(18, 30, 49);    

    public MainGUI() {
        system = new HotelSystem();
        FileHandler.loadData(system); 
        buildAutomaticDashboard();
    }

    private void buildAutomaticDashboard() {
        mainFrame = new JFrame("Luxury Stay Hotel Management System");
        mainFrame.setSize(1150, 840); 
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_BG);
        headerPanel.setPreferredSize(new Dimension(0, 75));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, COLOR_GOLD)); 
        
        JLabel mainTitle = new JLabel(" LUXURY STAY GUEST SYSTEM", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainTitle.setForeground(Color.WHITE);
        headerPanel.add(mainTitle, BorderLayout.CENTER);
        mainFrame.add(headerPanel, BorderLayout.NORTH);

        JPanel topColumnsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        topColumnsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topColumnsContainer.setBackground(COLOR_APP_BG);

        // COLUMN 1: INSTRUCTIONS
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBackground(COLOR_CARD_BG);
        instructionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(223, 228, 234), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel infoTitle = new JLabel("Portal Instructions & Policy:");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitle.setForeground(COLOR_HEADER_BG);
        instructionsPanel.add(infoTitle, BorderLayout.NORTH);

        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setBackground(COLOR_CARD_BG);
        infoText.setForeground(COLOR_TEXT_MAIN);
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setText(
            "\nWelcome! Follow these simple guided steps to book your ideal room:\n\n" +
            "• STEP 1: CHOOSE ROOM CATEGORY\n" +
            "Select an active room type tier from the options dropdown menu.\n\n" +
            "• STEP 2: FILL RESERVATION FORM\n" +
            "Enter your full name, a 10-digit phone number, target room selection, and total nights.\n\n" +
            "• STEP 3: CONFIGURE PAYMENT METHOD\n" +
            "Choose standard card billing, UPI id prompt validations, view automatic QR scanning placeholders, or settle everything directly over the physical counter via Pay at Hotel.\n\n" +
            "• MANAGEMENT NOTICE:\n" +
            "This kiosk terminal logs transactions automatically. For general inquiries, contact the supervisor deck."
        );
        instructionsPanel.add(infoText, BorderLayout.CENTER);
        topColumnsContainer.add(instructionsPanel);

        // COLUMN 2: OPERATIONS
        JPanel formsPanel = new JPanel();
        formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
        formsPanel.setBackground(COLOR_CARD_BG);
        formsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(223, 228, 234), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel secATitle = new JLabel("Step 1: Select Room Category");
        secATitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        secATitle.setForeground(COLOR_HEADER_BG);
        formsPanel.add(secATitle);
        formsPanel.add(Box.createVerticalStrut(5));

        String[] dropdownOptions = {"--- SELECT CATEGORY ---", "STANDARD", "DELUXE", "SUITE"};
        typeDropdown = new JComboBox<>(dropdownOptions);
        typeDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formsPanel.add(typeDropdown);
        
        formsPanel.add(Box.createVerticalStrut(15)); 

        JLabel secBTitle = new JLabel("Step 2: Customer Booking Form");
        secBTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        secBTitle.setForeground(COLOR_HEADER_BG);
        formsPanel.add(secBTitle);
        formsPanel.add(Box.createVerticalStrut(5));

        nameField = new JTextField();
        phoneField = new JTextField(); 
        roomField = new JTextField();
        nightsField = new JTextField();

        formsPanel.add(createModernInputField("Guest Full Name:", nameField));
        formsPanel.add(createModernInputField("Contact Number (10 Digits):", phoneField)); 
        formsPanel.add(createModernInputField("Target Room Number:", roomField));
        formsPanel.add(createModernInputField("Number of Nights:", nightsField));
        
        formsPanel.add(Box.createVerticalStrut(10));
        
        JLabel payLabel = new JLabel("Choose Payment Option:");
        payLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        payLabel.setForeground(COLOR_TEXT_MAIN);
        formsPanel.add(payLabel);
        formsPanel.add(Box.createVerticalStrut(2));
        
        // CHANGED: Default is now an explicit action prompt instruction item
        String[] paymentOptions = {"--- SELECT PAYMENT METHOD ---", "Card Number", "QR Code Scan", "UPI ID Prompt", "Pay at Hotel"};
        payDropdown = new JComboBox<>(paymentOptions);
        payDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        payDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formsPanel.add(payDropdown);
        
        formsPanel.add(Box.createVerticalStrut(10));
        
        dynamicPayFieldsContainer = new JPanel(new CardLayout());
        dynamicPayFieldsContainer.setBackground(COLOR_CARD_BG);
        dynamicPayFieldsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        
        // CHANGED: Added blank empty panel structure for when no selection is made yet
        JPanel defaultBlankPanel = new JPanel(new BorderLayout());
        defaultBlankPanel.setBackground(COLOR_CARD_BG);
        JLabel blankLabel = new JLabel("⚠️ Please select a valid checkout gateway method option above.");
        blankLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        blankLabel.setForeground(Color.RED);
        defaultBlankPanel.add(blankLabel, BorderLayout.CENTER);

        cardField = new JTextField();
        JPanel cardPanel = createModernInputField("Enter 16-Digit Card Number:", cardField);
        
        upiIdField = new JTextField();
        JPanel upiPanel = createModernInputField("Enter Virtual Payment UPI ID (e.g., user@upi):", upiIdField);
        
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(COLOR_CARD_BG);
        JLabel qrLabel = new JLabel("📱 Payment QR code generated below inside the Terminal Monitor Window!");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        qrLabel.setForeground(new Color(41, 128, 185));
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        
        JPanel hotelPanel = new JPanel(new BorderLayout());
        hotelPanel.setBackground(COLOR_CARD_BG);
        JLabel hotelLabel = new JLabel(" Total balance settled directly at the primary hotel lobby counter desk.");
        hotelLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hotelLabel.setForeground(new Color(110, 110, 110));
        hotelPanel.add(hotelLabel, BorderLayout.CENTER);
        
        dynamicPayFieldsContainer.add(defaultBlankPanel, "--- SELECT PAYMENT METHOD ---");
        dynamicPayFieldsContainer.add(cardPanel, "Debit Card");
        dynamicPayFieldsContainer.add(qrPanel, "Payment through QR");
        dynamicPayFieldsContainer.add(upiPanel, "UPI Payment");
        dynamicPayFieldsContainer.add(hotelPanel, "Pay at Hotel");
        
        formsPanel.add(dynamicPayFieldsContainer);
        formsPanel.add(Box.createVerticalStrut(15)); 

        JButton bookBtn = new JButton(" Confirm Room Booking");
        styleButton(bookBtn, COLOR_BTN_BOOK);
        formsPanel.add(bookBtn);

        formsPanel.add(Box.createVerticalStrut(10)); 

        JButton historyBtn = new JButton(" View Booking History (Staff Only)");
        styleButton(historyBtn, new Color(116, 125, 140)); 
        formsPanel.add(historyBtn);

        topColumnsContainer.add(formsPanel);
        mainFrame.add(topColumnsContainer, BorderLayout.CENTER);

        // BOTTOM PANEL MONITOR LOG
        JPanel bottomLogContainer = new JPanel(new BorderLayout());
        bottomLogContainer.setBackground(COLOR_APP_BG);
        bottomLogContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        bottomLogContainer.setPreferredSize(new Dimension(0, 240)); 

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        displayArea.setBackground(COLOR_CONSOLE_BG); 
        displayArea.setForeground(new Color(164, 176, 190)); 
        displayArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane monitorScrollPane = new JScrollPane(displayArea);
        TitledBorder borderTitle = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER_BG, 1), "Live Operational Terminal Output Screen"
        );
        borderTitle.setTitleColor(COLOR_HEADER_BG);
        borderTitle.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        monitorScrollPane.setBorder(borderTitle);
        
        bottomLogContainer.add(monitorScrollPane, BorderLayout.CENTER);
        mainFrame.add(bottomLogContainer, BorderLayout.SOUTH);

        // CHANGED: Intercept event selection to output QR pattern code instantly 
        payDropdown.addActionListener(e -> {
            String selection = (String) payDropdown.getSelectedItem();
            CardLayout cl = (CardLayout) (dynamicPayFieldsContainer.getLayout());
            cl.show(dynamicPayFieldsContainer, selection);
            
            if ("QR Code Scan".equals(selection)) {
                // Read current input data metrics to render clean total live balances inside the QR window view
                double currentPrice = 0.0;
                int nightsCount = 1;
                try {
                    if(!nightsField.getText().trim().isEmpty()) {
                        nightsCount = Integer.parseInt(nightsField.getText().trim());
                    }
                    String roomStr = roomField.getText().trim();
                    if(!roomStr.isEmpty()) {
                        Room currentRoom = system.findRoom(Integer.parseInt(roomStr));
                        if(currentRoom != null) currentPrice = currentRoom.getPricePerNight();
                    }
                } catch(Exception ignored) {}
                
                displayArea.setForeground(new Color(52, 152, 219));
                displayArea.setText("========================================================================================================\n");
                displayArea.append("📱 DYNAMIC SECURE UPI QR CODE INSTANT GENERATION PLATFORM\n");
                displayArea.append("========================================================================================================\n\n");
                displayArea.append("   ┌─────────────────────────────┐\n");
                displayArea.append("   │   █▀▀▀█  ▄ █ ▄▀  █▀▀▀█      │\n");
                displayArea.append("   │   █ ███  █  ▀▀▄  █ ███      │\n");
                displayArea.append("   │   █▄▄▄█  ▀ █▄ █  █▄▄▄█      │\n");
                displayArea.append("   │   ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄      │\n");
                displayArea.append("   │   █ ▀▀▄▄ ▀▄▀▀██  ▀█▄▀█      │\n");
                displayArea.append("   │   ▀▀▀ ▀▀ █  ▀▄█ ▀ ▀  ▀      │\n");
                displayArea.append("   │   █▀▀▀█ ▀▀▀█ ▄▄  █▄▀██      │\n");
                displayArea.append("   │   █ ███ █ ▄ ▀▀▀  ▄▄▀▄█      │\n");
                displayArea.append("   │   █▄▄▄█ █▀  ▀▀▄ ▀▄▀ ▀▀      │\n");
                displayArea.append("   └─────────────────────────────┘\n\n");
                displayArea.append("   👉 Scan via any compatible mobile banking terminal app to authorize bill amount: $" + (currentPrice * nightsCount) + "\n");
            } else if ("--- SELECT PAYMENT METHOD ---".equals(selection)) {
                refreshAvailableRoomsView();
            } else {
                displayArea.setForeground(new Color(164, 176, 190));
                displayArea.setText("\n Waiting for booking confirmation via selection: " + selection.toUpperCase());
            }
        });

        typeDropdown.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshAvailableRoomsView();
            }
        });

        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim(); 
            String roomStr = roomField.getText().trim();
            String nightsStr = nightsField.getText().trim();
            String selectedPayment = (String) payDropdown.getSelectedItem();

            // CHANGED: Blocks confirmation if user has not chosen an actual payment gateway option
            if (selectedPayment.equals("--- SELECT PAYMENT METHOD ---")) {
                JOptionPane.showMessageDialog(mainFrame, "❌ Checkout Aborted: Please choose a valid payment option before clicking confirm.", "Payment Method Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (name.isEmpty() || phone.isEmpty() || roomStr.isEmpty() || nightsStr.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, " Input error: Please complete all registration fields before proceeding.", "Empty Form Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(mainFrame, " Invalid Contact Number!\nMobile number must be exactly 10 digits.", "Phone Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedPayment.equals("Card Number") && !cardField.getText().trim().matches("\\d{16}")) {
                JOptionPane.showMessageDialog(mainFrame, " Card Validation Error!\nPlease supply a valid 16-digit card number.", "Card Missing", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (selectedPayment.equals("UPI ID Prompt") && !upiIdField.getText().trim().contains("@")) {
                JOptionPane.showMessageDialog(mainFrame, " UPI Address Error!\nPlease input a valid structured format endpoint string (e.g., name@bank).", "UPI Config", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int roomNum = Integer.parseInt(roomStr);
                int nights = Integer.parseInt(nightsStr);
                
                Room targetRoom = system.findRoom(roomNum);
                if (targetRoom == null || !targetRoom.isAvailable()) {
                    JOptionPane.showMessageDialog(mainFrame, " Selected room is unavailable or does not exist.", "Room Selection Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                displayArea.setForeground(new Color(241, 196, 15)); 
                displayArea.setText("========================================================================================================\n");
                displayArea.append(" ENCRYPTED TRANSACTION PIPELINE ROUTING [" + selectedPayment.toUpperCase() + "]...\n");
                displayArea.append("========================================================================================================\n\n");
                displayArea.append(" Connecting authorization nodes... Verification status clearing... Recording ledger data keys...\n");

                SwingWorker<Reservation, Void> gatewayWorker = new SwingWorker<>() {
                    @Override
                    protected Reservation doInBackground() throws Exception {
                        return system.bookRoom(name, phone, roomNum, nights, selectedPayment);
                    }

                    @Override
                    protected void done() {
                        try {
                            Reservation invoice = get();
                            if (invoice != null) {
                                displayArea.setForeground(new Color(46, 204, 113)); 
                                displayArea.setText("========================================================================================================\n");
                                displayArea.append(" RESERVATION COMPLETED VIA: " + selectedPayment.toUpperCase() + "\n");
                                displayArea.append("========================================================================================================\n\n");
                                displayArea.append(invoice.toString());
                                
                                nameField.setText("");
                                phoneField.setText("");
                                roomField.setText("");
                                nightsField.setText("");
                                cardField.setText("");
                                upiIdField.setText("");
                                payDropdown.setSelectedIndex(0); // Reset dropdown option select context
                                
                                JOptionPane.showMessageDialog(mainFrame, " Room Booked successfully via " + selectedPayment + "!\nID: " + invoice.getReservationId(), "System Success", JOptionPane.INFORMATION_MESSAGE);
                                refreshAvailableRoomsView();
                            }
                        } catch (Exception ex) {
                            displayArea.setForeground(Color.RED);
                            displayArea.setText(" Critical error routing checkout.");
                        }
                    }
                };
                gatewayWorker.execute();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, " Numerical Error: Use clean numbers only.", "Data Format Exception", JOptionPane.ERROR_MESSAGE);
            }
        });

        historyBtn.addActionListener(e -> {
            JPasswordField passwordField = new JPasswordField();
            int action = JOptionPane.showConfirmDialog(mainFrame, passwordField, " Enter Admin Security Access Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (action == JOptionPane.OK_OPTION) {
                if (new String(passwordField.getPassword()).equals(ADMIN_PASSWORD)) {
                    displayArea.setForeground(Color.WHITE);
                    displayArea.setText("========================================================================================================\n");
                    displayArea.append(" COMPREHENSIVE HOTEL RESERVATION HISTORY LOG DATABASE (ADMIN VIEW)\n");
                    displayArea.append("========================================================================================================\n\n");
                    
                    List<Reservation> allHistory = system.getAllReservations();
                    if (allHistory.isEmpty()) {
                        displayArea.append(" No data records found.");
                    } else {
                        for (Reservation res : allHistory) {
                            displayArea.append(res.toString() + "\n");
                            displayArea.append("--------------------------------------------------------------------------------------------------------\n");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, " Access Denied: Incorrect administrative key signature.", "Security Violation", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshAvailableRoomsView();
        mainFrame.setVisible(true);
    }

    private void refreshAvailableRoomsView() {
        String selectedString = (String) typeDropdown.getSelectedItem();
        if (selectedString == null || selectedString.equals("--- SELECT CATEGORY ---")) {
            displayArea.setForeground(new Color(164, 176, 190));
            displayArea.setText("========================================================================================================\n");
            displayArea.append(" RESERVATION SYSTEM STATUS: IDLE / WAITING FOR CUSTOMER\n");
            displayArea.append("========================================================================================================\n\n");
            displayArea.append("• Please choose an explicit Room Category Tier from the dropdown menu to view open rooms.");
            return;
        }

        Room.RoomType selectedType = Room.RoomType.valueOf(selectedString);
        List<Room> availableRooms = system.searchAvailableRooms(selectedType);
        
        displayArea.setForeground(new Color(235, 243, 250)); 
        displayArea.setText("========================================================================================================\n");
        displayArea.append(" " + selectedString + " ROOMS AVAILABILITY\n");
        displayArea.append("========================================================================================================\n\n");
        
        if (availableRooms.isEmpty()) {
            displayArea.append(" Sorry, there are currently zero vacant rooms listed under the " + selectedString + " class tier.");
        } else {
            int itemsInRow = 0;
            for (Room r : availableRooms) {
                displayArea.append(String.format(" Room #%-3d ($%-3.0f/night)     ", r.getRoomNumber(), r.getPricePerNight()));
                itemsInRow++;
                if (itemsInRow % 4 == 0) displayArea.append("\n  ");
            }
        }
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setPreferredSize(new Dimension(0, 36));
    }

    private JPanel createModernInputField(String labelTitle, JTextField field) {
        JPanel container = new JPanel(new BorderLayout(0, 2));
        container.setBackground(COLOR_CARD_BG);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel titleLabel = new JLabel(labelTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(COLOR_TEXT_MAIN);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 26));
        container.add(titleLabel, BorderLayout.NORTH);
        container.add(field, BorderLayout.CENTER);
        return container;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}