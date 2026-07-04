LEARNINGS.md - Task 1: Enhanced Calculator

What was the hardest bug?

While building the GUI version of the calculator (CalculatorGUI.java), I used special Unicode
symbols on some buttons, such as a square root symbol and a backspace arrow symbol.
When I ran the program, these symbols did not display correctly — instead of showing the
intended icons, the buttons displayed garbled/broken characters (like unreadable boxes and
random symbols).

How did I fix it?

I realized this was an encoding issue — the terminal and editor were not interpreting the
Unicode characters the same way the Java source file expected, so the special symbols got
corrupted when displayed.

To fix this, I replaced all special Unicode symbols with plain text labels instead
(for example, using "sqrt" instead of the square root symbol, and "DEL" instead of the
backspace arrow symbol). This made the UI both bug-free and more readable, without relying
on characters that might not render correctly across different systems.

Key Takeaway

This taught me that when building UI in Java (or any language), it's safer to avoid relying
on special Unicode symbols unless the encoding is explicitly handled (e.g., setting UTF-8
encoding at the file, compiler, and terminal level). Plain text labels are more portable
and avoid unexpected rendering issues on different machines.
Contentpdfpdfexcerpt_from_previous_claude_message.txt4 linestxt
