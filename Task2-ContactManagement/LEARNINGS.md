What was the hardest bug?

While building the GUI version, I initially used a JTable to display contacts. When I added
a search/filter feature, I ran into an issue: after filtering the table with a RowSorter, the
row index shown in the table no longer matched the actual row index in the underlying data
(the TableModel). This meant that clicking "Update" or "Delete" on a filtered row sometimes
edited or deleted the wrong contact.

How did I fix it?

I learned that a JTable's visible row index (view index) can differ from its actual data index
(model index) once sorting or filtering is applied. The fix was to always convert the selected
row using table.convertRowIndexToModel(row) before reading or modifying data, instead of using
the row index directly. This ensured that Update and Delete always acted on the correct contact,
regardless of any active search filter.

Later, when I redesigned the UI into a card-based layout instead of a table, this specific issue
went away naturally, since each card is directly tied to its own Contact object rather than a
row index. This also reinforced why direct object references are often simpler and safer than
index-based lookups when the underlying list can be filtered or reordered.

Key Takeaway

When working with any UI component that supports sorting or filtering (like JTable), never assume
the visible row number matches the data's actual position. Always convert between view and model
indices, or better yet, design the data flow so that actions reference the object itself rather
than its position in a list.
Contentpdfpdfexcerpt_from_previous_claude_message.txt4 linestxt
