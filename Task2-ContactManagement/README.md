About

This project is a Contact Management System built for the ShadowFox Java Development Internship.
It has two versions:


ContactManager.java - Console-based version
ContactManagerGUI.java - GUI version (Swing) with a card-based layout, colorful avatars, and a baby pink theme


Features


Full CRUD operations: Add, View, Search, Update, Delete contacts
Duplicate phone number prevention
Case-insensitive search (live filtering in the GUI version)
Input validation:

Phone number must be exactly 10 digits
Email must match a valid email pattern (regex-based)



Delete confirmation prompt (prevents accidental deletion)
GUI-specific features:

Card-based contact list with colorful initials-based avatars
Live search as you type
Contact counter
VCard (.vcf) export - export a single contact or all contacts to a standard vCard file
that can be imported into phones, Outlook, or Google Contacts





How to Run

Console Version


Open terminal in the project folder
Compile: javac ContactManager.java
Run: java ContactManager


GUI Version


Open terminal in the project folder
Compile: javac ContactManagerGUI.java
Run: java ContactManagerGUI


Technologies Used


Java (Core)
Swing (GUI)
ArrayList (in-memory data storage)
Regex (input validation)
Java File I/O (VCard export)


Note

Contacts are stored in memory only, as required by the task. Data does not persist after the
application is closed (this is expected behavior for this task level - persistent storage with
a database is covered in a later task).

Author

Drashtee Chauhan
Contentpdfpdfexcerpt_from_previous_claude_message.txt4 linestxt
