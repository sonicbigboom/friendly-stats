## Pre-conditions & Database Rules

- No row may actually be deleted. To "delete" a row, set it's `isDeleted` cell to `1`/`True`.
- The only values in a `Game` row that may be modified is `ForCash`, `SeasonID`, `EndDate`, and `IsDeleted`.
- A `GameRecord` may only be inserted or updated for an in-progress game. (A `game` that has no `EndDate` and is not `IsDeleted`.)
- Any table with a `CreatedTime`, `CreatedByPersonID`, `ModifiedTime`, and `ModifiedByPersonID` must be filled appropriately.
- Person with id `1` is reserved for the system. This `System` person should not be exposed to the client.
- Club with id `1` is reserved for the system. This `System Club` club should not be exposed to the client.
- A ClubID pointing NULL means that every club has access to this resource. These may not be "deleted".