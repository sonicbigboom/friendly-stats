## Pre-conditions & Database Rules

- No row may actually be deleted. To "delete" a row, set it's `isDeleted` cell to `1`/`True`.
- A `GameRecord` may not be inserted or updated for a "deleted" `game`.
- Any table with a `CreatedTime`, `CreatedByPersonID`, `ModifiedTime`, and `ModifiedByPersonID` must be filled appropriately.