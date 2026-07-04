import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class CalculatorGUI extends JFrame {

    private JTextField display;
    private JLabel historyLabel;
    private String currentExpression = "";

    private final Color BG_DARK = new Color(24, 24, 28);
    private final Color BG_PANEL = new Color(32, 32, 38);
    private final Color ACCENT = new Color(255, 122, 0);
    private final Color BTN_NUM = new Color(45, 45, 52);
    private final Color BTN_OP = new Color(60, 60, 70);
    private final Color TEXT_WHITE = Color.WHITE;

    public CalculatorGUI() {
        setTitle("Enhanced Calculator Pro");
        setSize(400, 620);
        setMinimumSize(new Dimension(360, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 10));

        add(buildDisplayPanel(), BorderLayout.NORTH);
        add(buildButtonPanel(), BorderLayout.CENTER);
        add(buildMenuBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------------- DISPLAY ----------------
    private JPanel buildDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 10, 20));

        historyLabel = new JLabel(" ");
        historyLabel.setForeground(new Color(150, 150, 150));
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        historyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        historyLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 42));
        display.setForeground(TEXT_WHITE);
        display.setBackground(BG_DARK);
        display.setBorder(null);
        display.setMaximumSize(new Dimension(2000, 70));

        panel.add(historyLabel);
        panel.add(display);
        return panel;
    }

    // ---------------- BUTTONS ----------------
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 8, 8));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        String[][] rows = {
            {"sqrt", "pow", "C", "DEL"},
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setForeground(TEXT_WHITE);
        btn.setBorder(new EmptyBorder(15, 10, 15, 10));

        if ("=".equals(label)) {
            btn.setBackground(ACCENT);
        } else if (label.equals("+") || label.equals("-") || label.equals("*") || label.equals("/")
                || label.equals("sqrt") || label.equals("pow") || label.equals("DEL") || label.equals("%")) {
            btn.setBackground(BTN_OP);
        } else if (label.length() > 4) { // conversion buttons (C to F, F to C, USD to INR)
            btn.setBackground(new Color(50, 70, 90));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        } else {
            btn.setBackground(BTN_NUM);
        }

        btn.addActionListener(this::onButtonClick);
        return btn;
    }

    // ---------------- BOTTOM TIP BAR ----------------
    private JPanel buildMenuBar() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel tip = new JLabel("Tip: Type full expressions like 5+3*2 - BODMAS supported");
        tip.setForeground(new Color(140, 140, 140));
        tip.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panel.add(tip);
        return panel;
    }

    // ---------------- BUTTON LOGIC ----------------
    private void onButtonClick(ActionEvent e) {
        String cmd = ((JButton) e.getSource()).getText();

        switch (cmd) {
            case "C":
                currentExpression = "";
                historyLabel.setText(" ");
                display.setText("0");
                break;

            case "DEL":
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
        try {
            double result = evaluateExpression(currentExpression);
            historyLabel.setText(currentExpression + " =");
            String formatted = formatResult(result);
            display.setText(formatted);
            currentExpression = formatted;
        } catch (ArithmeticException ex) {
            display.setText("Cannot divide by 0");
            currentExpression = "";
        } catch (Exception ex) {
            display.setText("Error");
            currentExpression = "";
        }
    }

    private void handleSquareRoot() {
        try {
            BigDecimal num = new BigDecimal(currentExpression);
            if (num.compareTo(BigDecimal.ZERO) < 0) {
                display.setText("Invalid input");
                currentExpression = "";
                return;
            }
            double result = Math.sqrt(num.doubleValue());
            historyLabel.setText("sqrt(" + currentExpression + ") =");
            currentExpression = formatResult(result);
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
            historyLabel.setText(currentExpression + "% =");
            currentExpression = formatResult(result.doubleValue());
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
            if (celsiusToFahrenheit) {
                result = temp.multiply(BigDecimal.valueOf(9))
                        .divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP)
                        .add(BigDecimal.valueOf(32));
                historyLabel.setText(currentExpression + " C =");
            } else {
                result = temp.subtract(BigDecimal.valueOf(32))
                        .multiply(BigDecimal.valueOf(5))
                        .divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP);
                historyLabel.setText(currentExpression + " F =");
            }
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
            BigDecimal rate = BigDecimal.valueOf(83.50);
            BigDecimal result = usd.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            historyLabel.setText("$" + currentExpression + " =");
            currentExpression = formatResult(result.doubleValue());
            display.setText("Rs " + currentExpression);
        } catch (Exception e) {
            display.setText("Enter a number first");
            currentExpression = "";
        }
    }

    private String formatResult(double result) {
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return String.valueOf((long) result);
        }
        BigDecimal bd = BigDecimal.valueOf(result).setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
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