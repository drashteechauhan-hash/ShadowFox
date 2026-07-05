import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ContactManagerGUI extends JFrame {

    private final List<Contact> contacts = new ArrayList<>();
    private JPanel cardListPanel;
    private JTextField searchField;
    private JLabel countLabel;

    // ---- Baby Pink & White Theme ----
    private final Color BG_MAIN = new Color(255, 245, 248);
    private final Color PINK_SOFT = new Color(255, 214, 227);
    private final Color PINK_ACCENT = new Color(255, 122, 165);
    private final Color PINK_ACCENT_HOVER = new Color(255, 100, 148);
    private final Color TEXT_DARK = new Color(80, 55, 65);
    private final Color TEXT_MUTED = new Color(175, 140, 155);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_HOVER = new Color(255, 240, 245);

    // Avatar color palette (soft pastel tones, cycled by name hash)
    private final Color[] AVATAR_COLORS = {
            new Color(255, 154, 184), new Color(255, 179, 138),
            new Color(158, 200, 255), new Color(178, 223, 158),
            new Color(216, 168, 255), new Color(255, 214, 122)
    };

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public ContactManagerGUI() {
        setTitle("Contact Book");
        setSize(820, 620);
        setMinimumSize(new Dimension(680, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCardScrollPane(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        // Sample data so the UI isn't empty on first run
        refreshCardList();

        setVisible(true);
    }

    // ---------------- TOP PANEL ----------------
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(24, 30, 5, 30));

        JLabel title = new JLabel("Contact Book");
        title.setForeground(PINK_ACCENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        countLabel = new JLabel("0 contacts");
        countLabel.setForeground(TEXT_MUTED);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(BG_MAIN);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(countLabel, BorderLayout.EAST);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(WHITE);
        searchField.setForeground(TEXT_DARK);
        searchField.setCaretColor(PINK_ACCENT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PINK_SOFT, 2, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshCardList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshCardList(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshCardList(); }
        });

        panel.add(titleRow, BorderLayout.NORTH);
        panel.add(searchField, BorderLayout.SOUTH);
        return panel;
    }

    // ---------------- CARD LIST ----------------
    private JScrollPane buildCardScrollPane() {
        cardListPanel = new JPanel();
        cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS));
        cardListPanel.setBackground(BG_MAIN);

        JScrollPane scroll = new JScrollPane(cardListPanel);
        scroll.setBorder(new EmptyBorder(5, 30, 5, 30));
        scroll.getViewport().setBackground(BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    private void refreshCardList() {
        cardListPanel.removeAll();
        String query = searchField.getText().trim().toLowerCase();

        List<Contact> filtered = new ArrayList<>();
        for (Contact c : contacts) {
            if (query.isEmpty() || c.name.toLowerCase().contains(query)) {
                filtered.add(c);
            }
        }

        if (filtered.isEmpty()) {
            JLabel empty = new JLabel("No contacts found", SwingConstants.CENTER);
            empty.setForeground(TEXT_MUTED);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(40, 0, 0, 0));
            cardListPanel.add(empty);
        } else {
            for (Contact c : filtered) {
                cardListPanel.add(buildContactCard(c));
                cardListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        countLabel.setText(contacts.size() + (contacts.size() == 1 ? " contact" : " contacts"));
        cardListPanel.revalidate();
        cardListPanel.repaint();
    }

    private JPanel buildContactCard(Contact c) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 225, 235), 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar circle with initial
        JPanel avatar = buildAvatar(c.name);

        // Info block
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(WHITE);

        JLabel nameLbl = new JLabel(c.name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(TEXT_DARK);

        JLabel detailsLbl = new JLabel(c.phone + "   •   " + c.email);
        detailsLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLbl.setForeground(TEXT_MUTED);

        info.add(nameLbl);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(detailsLbl);

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(WHITE);
        JButton exportBtn = createSmallButton("Export", new Color(200, 225, 255));
        JButton editBtn = createSmallButton("Edit", PINK_SOFT);
        JButton delBtn = createSmallButton("Delete", new Color(255, 210, 210));
        exportBtn.addActionListener(e -> exportSingleContact(c));
        editBtn.addActionListener(e -> openUpdateDialog(c));
        delBtn.addActionListener(e -> deleteContact(c));
        actions.add(exportBtn);
        actions.add(editBtn);
        actions.add(delBtn);

        card.add(avatar, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(CARD_HOVER); info.setBackground(CARD_HOVER); actions.setBackground(CARD_HOVER); }
            public void mouseExited(MouseEvent e) { card.setBackground(WHITE); info.setBackground(WHITE); actions.setBackground(WHITE); }
        });

        return card;
    }

    // Draws a colorful circular avatar with the contact's first initial
    private JPanel buildAvatar(String name) {
        Color color = AVATAR_COLORS[Math.abs(name.hashCode()) % AVATAR_COLORS.length];
        String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();

        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initial, x, y);
            }
        };
        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setOpaque(false);
        return avatar;
    }

    private JButton createSmallButton(String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setForeground(TEXT_DARK);
        btn.setBackground(bg);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---------------- BOTTOM PANEL ----------------
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(8, 30, 22, 30));

        JButton addBtn = new JButton("+  Add New Contact");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(WHITE);
        addBtn.setBackground(PINK_ACCENT);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(new EmptyBorder(14, 30, 14, 30));
        addBtn.setOpaque(true);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { addBtn.setBackground(PINK_ACCENT_HOVER); }
            public void mouseExited(MouseEvent e) { addBtn.setBackground(PINK_ACCENT); }
        });
        addBtn.addActionListener(e -> openAddDialog());

        JButton exportAllBtn = new JButton("Export All (.vcf)");
        exportAllBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportAllBtn.setForeground(TEXT_DARK);
        exportAllBtn.setBackground(new Color(200, 225, 255));
        exportAllBtn.setFocusPainted(false);
        exportAllBtn.setBorder(new EmptyBorder(14, 30, 14, 30));
        exportAllBtn.setOpaque(true);
        exportAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportAllBtn.addActionListener(e -> exportAllContacts());

        panel.add(addBtn);
        panel.add(exportAllBtn);
        return panel;
    }

    // ---------------- ADD CONTACT ----------------
    private void openAddDialog() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel form = buildFormPanel(nameField, phoneField, emailField);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Contact",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            String error = validateContact(name, phone, email, null);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            contacts.add(new Contact(name, phone, email));
            refreshCardList();
            JOptionPane.showMessageDialog(this, "Contact added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------------- UPDATE CONTACT ----------------
    private void openUpdateDialog(Contact c) {
        JTextField nameField = new JTextField(c.name);
        JTextField phoneField = new JTextField(c.phone);
        JTextField emailField = new JTextField(c.email);

        JPanel form = buildFormPanel(nameField, phoneField, emailField);

        int result = JOptionPane.showConfirmDialog(this, form, "Update Contact",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            String error = validateContact(name, phone, email, c);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            c.name = name;
            c.phone = phone;
            c.email = email;
            refreshCardList();
            JOptionPane.showMessageDialog(this, "Contact updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------------- DELETE CONTACT ----------------
    private void deleteContact(Contact c) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + c.name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            contacts.remove(c);
            refreshCardList();
        }
    }

    // ---------------- HELPERS ----------------
    private JPanel buildFormPanel(JTextField nameField, JTextField phoneField, JTextField emailField) {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nameLbl = new JLabel("Name:");
        JLabel phoneLbl = new JLabel("Phone (10 digits):");
        JLabel emailLbl = new JLabel("Email:");
        for (JLabel lbl : new JLabel[]{nameLbl, phoneLbl, emailLbl}) {
            lbl.setForeground(TEXT_DARK);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        form.add(nameLbl);
        form.add(nameField);
        form.add(phoneLbl);
        form.add(phoneField);
        form.add(emailLbl);
        form.add(emailField);
        return form;
    }

    // ---------------- VCARD EXPORT (Tier 2 - Creative Upgrade) ----------------
    private void exportSingleContact(Contact c) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(c.name.replaceAll("\\s+", "_") + ".vcf"));
        int choice = chooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                writer.write(buildVCardText(c));
                JOptionPane.showMessageDialog(this, "Contact exported to:\n" + file.getAbsolutePath(),
                        "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to export contact: " + ex.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportAllContacts() {
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No contacts to export.", "Nothing to Export", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("all_contacts.vcf"));
        int choice = chooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                for (Contact c : contacts) {
                    writer.write(buildVCardText(c));
                }
                JOptionPane.showMessageDialog(this, contacts.size() + " contacts exported to:\n" + file.getAbsolutePath(),
                        "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to export contacts: " + ex.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Builds a single VCard 3.0 formatted text block for one contact
    private String buildVCardText(Contact c) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:3.0\n");
        sb.append("FN:").append(c.name).append("\n");
        sb.append("TEL:").append(c.phone).append("\n");
        sb.append("EMAIL:").append(c.email).append("\n");
        sb.append("END:VCARD\n");
        return sb.toString();
    }

    private String validateContact(String name, String phone, String email, Contact editingContact) {
        if (name.isEmpty()) return "Name cannot be empty.";
        if (!phone.matches("\\d{10}")) return "Phone must be exactly 10 digits.";
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Invalid email format.";

        for (Contact c : contacts) {
            if (c == editingContact) continue;
            if (c.phone.equals(phone)) {
                return "A contact with this phone number already exists.";
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManagerGUI::new);
    }
}

// Contact class (POJO) - demonstrates Encapsulation
class Contact {
    String name;
    String phone;
    String email;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}