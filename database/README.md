## Pre-conditions & Database Rules

- No row may actually be deleted. To "delete" a row, set it's `isDeleted` cell to `1`/`True`.
- A `GameRecord` may not be inserted or updated for a "deleted" `game`.
- Any table with a `CreatedTime`, `CreatedByPersonID`, `ModifiedTime`, and `ModifiedByPersonID` must be filled appropriately.
- Person with id `1` is reserved for the system. This `System` person should not be exposed to the client.
- Club with id `1` is reserved for the system. This `System Club` club should not be exposed to the client.
- A ClubID pointing NULL means that every club has access to this resource. These may not be "deleted".