import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagerGUI extends JFrame {

    private final List<Product> products = new ArrayList<>();
    private JPanel cardListPanel;
    private JTextField barcodeSearchField;
    private JLabel countLabel;
    private int nextBarcode = 1001;

    // ---- Mint & White Theme ----
    private final Color BG_MAIN = new Color(240, 250, 248);
    private final Color MINT_SOFT = new Color(198, 235, 226);
    private final Color MINT_ACCENT = new Color(38, 166, 138);
    private final Color MINT_ACCENT_HOVER = new Color(30, 145, 120);
    private final Color TEXT_DARK = new Color(40, 60, 55);
    private final Color TEXT_MUTED = new Color(130, 160, 152);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_HOVER = new Color(235, 250, 246);
    private final Color LOW_STOCK_BG = new Color(255, 228, 225);
    private final Color LOW_STOCK_BORDER = new Color(230, 100, 90);

    public InventoryManagerGUI() {
        setTitle("Inventory Manager");
        setSize(860, 640);
        setMinimumSize(new Dimension(700, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCardScrollPane(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        refreshCardList();

        setVisible(true);
    }

    // ---------------- TOP PANEL ----------------
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(24, 30, 5, 30));

        JLabel title = new JLabel("Inventory Manager");
        title.setForeground(MINT_ACCENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        countLabel = new JLabel("0 products");
        countLabel.setForeground(TEXT_MUTED);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(BG_MAIN);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(countLabel, BorderLayout.EAST);

        // ---- Barcode Search (Tier 2 - Creative Upgrade) ----
        barcodeSearchField = new JTextField();
        barcodeSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        barcodeSearchField.setBackground(WHITE);
        barcodeSearchField.setForeground(TEXT_DARK);
        barcodeSearchField.setCaretColor(MINT_ACCENT);
        barcodeSearchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MINT_SOFT, 2, true),
                new EmptyBorder(10, 16, 10, 16)
        ));

        JLabel searchHint = new JLabel("Type a Barcode ID and press Enter to instantly find & scroll to a product");
        searchHint.setForeground(TEXT_MUTED);
        searchHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        searchHint.setBorder(new EmptyBorder(4, 4, 0, 0));

        barcodeSearchField.addActionListener(e -> searchByBarcode());

        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setBackground(BG_MAIN);
        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setBackground(BG_MAIN);
        JLabel barcodeIcon = new JLabel("Barcode ID:");
        barcodeIcon.setForeground(TEXT_DARK);
        barcodeIcon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchRow.add(barcodeIcon, BorderLayout.WEST);
        searchRow.add(barcodeSearchField, BorderLayout.CENTER);
        searchWrap.add(searchRow, BorderLayout.NORTH);
        searchWrap.add(searchHint, BorderLayout.SOUTH);

        panel.add(titleRow, BorderLayout.NORTH);
        panel.add(searchWrap, BorderLayout.SOUTH);
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

        if (products.isEmpty()) {
            JLabel empty = new JLabel("No products in inventory", SwingConstants.CENTER);
            empty.setForeground(TEXT_MUTED);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(40, 0, 0, 0));
            cardListPanel.add(empty);
        } else {
            for (Product p : products) {
                cardListPanel.add(buildProductCard(p));
                cardListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        int lowStockCount = 0;
        for (Product p : products) {
            if (p.quantity < 5) lowStockCount++;
        }
        String countText = products.size() + (products.size() == 1 ? " product" : " products");
        if (lowStockCount > 0) {
            countText += "  •  " + lowStockCount + " low stock";
        }
        countLabel.setText(countText);

        cardListPanel.revalidate();
        cardListPanel.repaint();
    }

    // Tier 1 - Grounded Upgrade: Low Stock Alerts (RED highlight if quantity < 5)
    private JPanel buildProductCard(Product p) {
        boolean isLowStock = p.quantity < 5;

        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(isLowStock ? LOW_STOCK_BG : WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(isLowStock ? LOW_STOCK_BORDER : new Color(220, 240, 235), isLowStock ? 2 : 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.putClientProperty("barcode", p.barcode);

        // Barcode badge
        JPanel badge = buildBarcodeBadge(p.barcode, isLowStock);

        // Info block
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel nameLbl = new JLabel(p.name + (isLowStock ? "   ⚠ LOW STOCK" : ""));
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(isLowStock ? LOW_STOCK_BORDER : TEXT_DARK);

        JLabel detailsLbl = new JLabel("Qty: " + p.quantity + "   •   Price: Rs." + String.format("%.2f", p.price)
                + "   •   Total Value: Rs." + String.format("%.2f", p.getTotalValue()));
        detailsLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLbl.setForeground(TEXT_MUTED);

        info.add(nameLbl);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(detailsLbl);

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton editBtn = createSmallButton("Edit", MINT_SOFT);
        JButton delBtn = createSmallButton("Delete", new Color(255, 210, 210));
        editBtn.addActionListener(e -> openUpdateDialog(p));
        delBtn.addActionListener(e -> deleteProduct(p));
        actions.add(editBtn);
        actions.add(delBtn);

        card.add(badge, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        Color hoverColor = isLowStock ? new Color(255, 218, 214) : CARD_HOVER;
        Color baseColor = isLowStock ? LOW_STOCK_BG : WHITE;
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { card.setBackground(baseColor); }
        });

        return card;
    }

    private JPanel buildBarcodeBadge(String barcode, boolean isLowStock) {
        Color badgeColor = isLowStock ? LOW_STOCK_BORDER : MINT_ACCENT;
        JPanel badge = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(badgeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        badge.setPreferredSize(new Dimension(60, 50));
        badge.setOpaque(false);
        JLabel lbl = new JLabel(barcode);
        lbl.setForeground(WHITE);
        lbl.setFont(new Font("Consolas", Font.BOLD, 12));
        badge.add(lbl);
        return badge;
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

        JButton addBtn = new JButton("+  Add New Product");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(WHITE);
        addBtn.setBackground(MINT_ACCENT);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(new EmptyBorder(14, 30, 14, 30));
        addBtn.setOpaque(true);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { addBtn.setBackground(MINT_ACCENT_HOVER); }
            public void mouseExited(MouseEvent e) { addBtn.setBackground(MINT_ACCENT); }
        });
        addBtn.addActionListener(e -> openAddDialog());

        panel.add(addBtn);
        return panel;
    }

    // ---------------- ADD PRODUCT ----------------
    private void openAddDialog() {
        JTextField nameField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField priceField = new JTextField();

        JPanel form = buildFormPanel(nameField, qtyField, priceField);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String error = validateProduct(name, qtyField.getText().trim(), priceField.getText().trim());
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int qty = Integer.parseInt(qtyField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String barcode = String.valueOf(nextBarcode++);

            products.add(new Product(barcode, name, qty, price));
            refreshCardList();
            JOptionPane.showMessageDialog(this, "Product added successfully!\nBarcode ID: " + barcode,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------------- UPDATE PRODUCT ----------------
    private void openUpdateDialog(Product p) {
        JTextField nameField = new JTextField(p.name);
        JTextField qtyField = new JTextField(String.valueOf(p.quantity));
        JTextField priceField = new JTextField(String.valueOf(p.price));

        JPanel form = buildFormPanel(nameField, qtyField, priceField);

        int result = JOptionPane.showConfirmDialog(this, form, "Update Product (Barcode: " + p.barcode + ")",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String error = validateProduct(name, qtyField.getText().trim(), priceField.getText().trim());
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            p.name = name;
            p.quantity = Integer.parseInt(qtyField.getText().trim());
            p.price = Double.parseDouble(priceField.getText().trim());
            refreshCardList();
            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------------- DELETE PRODUCT ----------------
    private void deleteProduct(Product p) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + p.name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            products.remove(p);
            refreshCardList();
        }
    }

    // ---------------- BARCODE SEARCH (Tier 2 - Creative Upgrade) ----------------
    private void searchByBarcode() {
        String barcode = barcodeSearchField.getText().trim();
        if (barcode.isEmpty()) return;

        Product found = null;
        for (Product p : products) {
            if (p.barcode.equals(barcode)) {
                found = p;
                break;
            }
        }

        if (found == null) {
            JOptionPane.showMessageDialog(this, "No product found with barcode: " + barcode,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Scroll to and briefly flash the matching card
        for (Component comp : cardListPanel.getComponents()) {
            if (comp instanceof JPanel) {
                Object cardBarcode = ((JPanel) comp).getClientProperty("barcode");
                if (barcode.equals(cardBarcode)) {
                    cardListPanel.scrollRectToVisible(comp.getBounds());
                    flashCard((JPanel) comp);
                    break;
                }
            }
        }
        barcodeSearchField.setText("");
    }

    private void flashCard(JPanel card) {
        Color original = card.getBackground();
        Color flash = new Color(255, 244, 180);
        Timer timer = new Timer(150, null);
        int[] count = {0};
        timer.addActionListener(e -> {
            card.setBackground(count[0] % 2 == 0 ? flash : original);
            count[0]++;
            if (count[0] >= 4) {
                card.setBackground(original);
                timer.stop();
            }
        });
        timer.start();
    }

    // ---------------- HELPERS ----------------
    private JPanel buildFormPanel(JTextField nameField, JTextField qtyField, JTextField priceField) {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nameLbl = new JLabel("Product Name:");
        JLabel qtyLbl = new JLabel("Quantity:");
        JLabel priceLbl = new JLabel("Price per unit (Rs):");
        for (JLabel lbl : new JLabel[]{nameLbl, qtyLbl, priceLbl}) {
            lbl.setForeground(TEXT_DARK);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        form.add(nameLbl);
        form.add(nameField);
        form.add(qtyLbl);
        form.add(qtyField);
        form.add(priceLbl);
        form.add(priceField);
        return form;
    }

    private String validateProduct(String name, String qtyText, String priceText) {
        if (name.isEmpty()) return "Product name cannot be empty.";

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            return "Quantity must be a valid whole number.";
        }
        if (qty < 0) return "Quantity cannot be negative.";

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return "Price must be a valid number.";
        }
        if (price < 0) return "Price cannot be negative.";

        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagerGUI::new);
    }
}

// Product class (POJO) - demonstrates Encapsulation
class Product {
    String barcode;
    String name;
    int quantity;
    double price;

    Product(String barcode, String name, int quantity, double price) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    double getTotalValue() {
        return quantity * price;
    }
}