import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class CalculatorGUI extends JFrame {

    private JTextField display;
    private JTextArea historyArea;
    private String currentExpression = "";

    private final Color BG_DARK = new Color(24, 24, 28);
    private final Color BG_PANEL = new Color(32, 32, 38);
    private final Color ACCENT = new Color(255, 122, 0);
    private final Color BTN_NUM = new Color(45, 45, 52);
    private final Color BTN_OP = new Color(60, 60, 70);
    private final Color TEXT_WHITE = Color.WHITE;

    private static final BigDecimal USD_TO_INR_RATE = BigDecimal.valueOf(83.50);

    public CalculatorGUI() {
        setTitle("Enhanced Calculator Pro");
        setSize(420, 700);
        setMinimumSize(new Dimension(380, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 5));

        add(buildHistoryPanel(), BorderLayout.NORTH);
        add(buildDisplayAndButtons(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------------- HISTORY PANEL ----------------
    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(8, 15, 8, 15));

        JLabel title = new JLabel("History");
        title.setForeground(new Color(140, 140, 140));
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));

        historyArea = new JTextArea(4, 20);
        historyArea.setEditable(false);
        historyArea.setBackground(BG_PANEL);
        historyArea.setForeground(new Color(170, 170, 170));
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(380, 90));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void addToHistory(String line) {
        historyArea.append(line + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    // ---------------- DISPLAY + BUTTONS ----------------
    private JPanel buildDisplayAndButtons() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setBackground(BG_DARK);

        JPanel displayPanel = new JPanel();
        displayPanel.setBackground(BG_DARK);
        displayPanel.setBorder(new EmptyBorder(15, 20, 5, 20));
        displayPanel.setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 40));
        display.setForeground(TEXT_WHITE);
        display.setBackground(BG_DARK);
        display.setBorder(null);

        displayPanel.add(display, BorderLayout.CENTER);
        wrapper.add(displayPanel, BorderLayout.NORTH);
        wrapper.add(buildButtonPanel(), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 8, 8));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        String backspaceLabel = "\u232B"; // Unicode backspace/erase symbol

        String[][] rows = {
            {"sqrt", "pow", "C", backspaceLabel},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"},
            {"C to F", "F to C", "USD to INR", "%"}
        };

        for (String[] row : rows) {
            for (String label : row) {
                panel.add(createButton(label));
            }
        }
        return panel;
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setForeground(TEXT_WHITE);
        btn.setBorder(new EmptyBorder(15, 10, 15, 10));

        if ("=".equals(label)) {
            btn.setBackground(ACCENT);
        } else if (label.equals("+") || label.equals("-") || label.equals("*") || label.equals("/")
                || label.equals("sqrt") || label.equals("pow") || label.equals("\u232B") || label.equals("%")) {
            btn.setBackground(BTN_OP);
        } else if (label.length() > 4) { // conversion buttons
            btn.setBackground(new Color(50, 70, 90));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        } else {
            btn.setBackground(BTN_NUM);
        }

        btn.addActionListener(this::onButtonClick);
        return btn;
    }

    // ---------------- FOOTER (disclaimer + tip) ----------------
    private JPanel buildFooter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(6, 10, 6, 10));

        JLabel tip = new JLabel("Tip: Type full expressions like 5+3*2 - BODMAS supported");
        tip.setForeground(new Color(140, 140, 140));
        tip.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JLabel disclaimer = new JLabel("Note: USD to INR uses a fixed rate (1 USD = " + USD_TO_INR_RATE + " INR), not live rates");
        disclaimer.setForeground(new Color(120, 120, 120));
        disclaimer.setFont(new Font("Segoe UI", Font.ITALIC, 10));

        panel.add(tip);
        panel.add(disclaimer);
        return panel;
    }

    // ---------------- BUTTON LOGIC ----------------
    private void onButtonClick(ActionEvent e) {
        String cmd = ((JButton) e.getSource()).getText();

        switch (cmd) {
            case "C":
                currentExpression = "";
                display.setText("0");
                break;

            case "\u232B": // Backspace
                if (!currentExpression.isEmpty()) {
                    currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                }
                display.setText(currentExpression.isEmpty() ? "0" : currentExpression);
                break;

            case "=":
                calculateResult();
                break;

            case "sqrt":
                handleSquareRoot();
                break;

            case "pow":
                currentExpression += "^";
                display.setText(currentExpression);
                break;

            case "%":
                handlePercentage();
                break;

            case "C to F":
                convertTemperature(true);
                break;

            case "F to C":
                convertTemperature(false);
                break;

            case "USD to INR":
                convertCurrency();
                break;

            default: // numbers, operators, decimal
                currentExpression += cmd;
                display.setText(currentExpression);
        }
    }

    private void calculateResult() {
        String original = currentExpression;
        try {
            validateExpression(currentExpression);
            double result = evaluateExpression(currentExpression);
            String formatted = formatResult(result);
            display.setText(formatted);
            addToHistory(original + " = " + formatted);
            currentExpression = formatted;
        } catch (ArithmeticException ex) {
            display.setText("Cannot divide by 0");
            addToHistory(original + " = Error (divide by 0)");
            currentExpression = "";
        } catch (Exception ex) {
            display.setText("Invalid expression");
            addToHistory(original + " = Error (invalid expression)");
            currentExpression = "";
        }
    }

    // Checks for malformed expressions like "5++2", "5**", "+5+", trailing operators etc.
    private void validateExpression(String expr) {
        if (expr.isEmpty()) throw new IllegalArgumentException("Empty expression");

        // Reject two operators in a row (except a legitimate negative sign after an operator)
        for (int i = 0; i < expr.length() - 1; i++) {
            char c1 = expr.charAt(i);
            char c2 = expr.charAt(i + 1);
            boolean bothOperators = isOperator(c1) && isOperator(c2);
            boolean validNegative = isOperator(c1) && c2 == '-' && (i + 2 >= expr.length() || Character.isDigit(expr.charAt(i + 2)));
            if (bothOperators && !validNegative) {
                throw new IllegalArgumentException("Consecutive operators");
            }
        }

        // Reject expression ending in an operator
        char last = expr.charAt(expr.length() - 1);
        if (isOperator(last)) {
            throw new IllegalArgumentException("Expression ends with operator");
        }

        // Reject expression starting with an operator (except minus for negative numbers)
        char first = expr.charAt(0);
        if (isOperator(first) && first != '-') {
            throw new IllegalArgumentException("Expression starts with operator");
        }
    }

    private void handleSquareRoot() {
        try {
            BigDecimal num = new BigDecimal(currentExpression);
            if (num.compareTo(BigDecimal.ZERO) < 0) {
                display.setText("Invalid: negative number");
                addToHistory("sqrt(" + currentExpression + ") = Error (negative input)");
                currentExpression = "";
                return;
            }
            double result = Math.sqrt(num.doubleValue());
            String formatted = formatResult(result);
            addToHistory("sqrt(" + currentExpression + ") = " + formatted);
            currentExpression = formatted;
            display.setText(currentExpression);
        } catch (Exception e) {
            display.setText("Enter a number first");
            currentExpression = "";
        }
    }

    private void handlePercentage() {
        try {
            BigDecimal num = new BigDecimal(currentExpression);
            BigDecimal result = num.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            String formatted = formatResult(result.doubleValue());
            addToHistory(currentExpression + "% = " + formatted);
            currentExpression = formatted;
            display.setText(currentExpression);
        } catch (Exception e) {
            display.setText("Enter a number first");
            currentExpression = "";
        }
    }

    private void convertTemperature(boolean celsiusToFahrenheit) {
        try {
            BigDecimal temp = new BigDecimal(currentExpression);
            BigDecimal result;
            String label;
            if (celsiusToFahrenheit) {
                result = temp.multiply(BigDecimal.valueOf(9))
                        .divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP)
                        .add(BigDecimal.valueOf(32));
                label = currentExpression + " C = " + formatResult(result.doubleValue()) + " F";
            } else {
                result = temp.subtract(BigDecimal.valueOf(32))
                        .multiply(BigDecimal.valueOf(5))
                        .divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP);
                label = currentExpression + " F = " + formatResult(result.doubleValue()) + " C";
            }
            addToHistory(label);
            currentExpression = formatResult(result.doubleValue());
            display.setText(currentExpression);
        } catch (Exception e) {
            display.setText("Enter a number first");
            currentExpression = "";
        }
    }

    private void convertCurrency() {
        try {
            BigDecimal usd = new BigDecimal(currentExpression);
            BigDecimal result = usd.multiply(USD_TO_INR_RATE).setScale(2, RoundingMode.HALF_UP);
            String formatted = formatResult(result.doubleValue());
            addToHistory("$" + currentExpression + " = Rs " + formatted + " (fixed rate)");
            currentExpression = formatted;
            display.setText("Rs " + currentExpression);
        } catch (Exception e) {
            display.setText("Enter a number first");
            currentExpression = "";
        }
    }

    // Removes trailing .0 for whole numbers, but keeps real decimals like 5.25
    private String formatResult(double result) {
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            return "Error";
        }
        BigDecimal bd = BigDecimal.valueOf(result).setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
        // stripTrailingZeros can return scientific notation for whole numbers like "5E+1"
        if (bd.scale() < 0) {
            bd = bd.setScale(0);
        }
        return bd.toPlainString();
    }

    // ---------------- EXPRESSION PARSER (Shunting Yard: handles + - * / ^) ----------------
    private double evaluateExpression(String expr) {
        if (expr.isEmpty()) throw new IllegalArgumentException("Empty expression");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;

        while (i < expr.length()) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            } else if (c == '-' && (i == 0 || isOperator(expr.charAt(i - 1)))) {
                StringBuilder sb = new StringBuilder("-");
                i++;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
            i++;
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        if (numbers.size() != 1) throw new IllegalArgumentException("Invalid expression");
        return numbers.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        if (op == '^') return 3;
        return 0;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Divide by zero");
                return a / b;
            case '^': return Math.pow(a, b);
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }
}
