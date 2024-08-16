## Bibiliography

## Terms
`cash balance`      - The total (or per-club total) amount of money (in cents) that a person has in their account.

### Actions
`delete`            - In essence deletes the item, however, no data is every truly deleted, and can potentially be recovered.

### Objects
`user`              - A user of the application. \
`client`            - The `user` that is sending the HTTP request. \
`group`             - A group of `users` that play `games` together. \
`membership`        - A `user`-`group` pair that determines user membership. \
`cash-admin`        - A `user` with permission to make money changes in a group.
`game`              - A session of a game that keeps score, such as one night of poker, or one fantasy football league. \
`game-type`         - A type of game, such as poker, or mahjong. \
`game-record`       - A record of a single action in a `game`. \
`score`             - The accumulated score of a `person`, in a `club`, for a `game-type`, for cash or for points. \
`transaction`       - Money given from one person to another person. \
`bank-transaction`  - Money deposited into the `club's` bank.

### Parameters
`*Object*-id`       - The id of the `*Object*`, like `user` or `game-record`. Default: null \
`ongoing`           - Whether the `game` is still being played. Default: null \
`for-cash`          - Whether the `games` were played for cash or not. Default: `true` \
`filter`            - Text which the app searches for based on.  For `users` this would compare against any names associated with the user. Default: null


## Resouce Table

| **Resource** | **POST** | **GET** | **PATCH** | **DELETE** |
|:---:|:---:|:---:|:---:|:---:|
| **/users** | ERROR | Gets all of the `users`. Parameters: `filter`. | ERROR | ERROR |
| **/me** | ERROR | Gets `client` details. | Updates `client` details. | `Deletes` `client` account. |
| **/balance** | ERROR | Gets the `client's` total `cash balance`. | ERROR | ERROR |
| **/memberships** | Creates a new `membership`. | ERROR | Updates a `membership`. | `Deletes` a `membership`. |
| **/groups** | Creates a new `group`. | Gets all `groups` that the `client` is a member of. | ERROR | ERROR |
| **/groups/{group-id}** | ERROR | Gets details on `group` `{group-id}`. | Updates `group` `{group-id}`. | If the `client` is the owner of the group, `deletes` the group.  Otherwise, removes the `client` from the `group`. |
| **/groups/{group-id}/users** | Adds a `user` to `group` `{group-id}` with a `{user-id}`. | Get all of the `users` in `group` `{group-id}`. | ERROR | ERROR |
| **/groups/{group-id}/games** | Creates a `game` in `group` `{group-id}`. | Gets all of the `games` in `group` `{group-id}`. Parameters: `ongoing`. | ERROR | ERROR |
| **/groups/{group-id}/transactions** | Creates a new `transaction`. This must originate from the `client`. | If the `client` is an `admin` of the `group`, gets all of the `group `{group-id}` `transactions`. Parameters: `{user-id}`. Otherwise, gets all  `group` `{group-id}` `transactions` for the `client`.  | ERROR | ERROR |
| **/groups/{group-id}/bank-transactions** | Creates a new `bank-transaction`. `Client` must be a `cash-admin`. | If the `client` is an `admin` of the `group`, gets all of the `group `{group-id}` `bank-transactions`. Parameters: `{user-id}`. Otherwise, gets all  `group` `{group-id}` `bank-transactions` for the `client`. | ERROR | ERROR |
| **/groups/{group-id}/balance** | ERROR | Gets `cash balances` of all of the `users` in `group` `{group-id}`. Parameters: `{user-id}`. | ERROR | ERROR |
| **/groups/{group-id}/game-type** | ERROR | Gets all of the game types used by `{group}` `{group-id}`. | ERROR | ERROR |
| **/groups/{group-id}/game-type/{game-type-id}** | ERROR | Gets all of the `scores` for a `game-type` `{game-type-id}` in `group` `{group-id}`. Parameters: `{user-id}`, `{for-cash}`. | ERROR | ERROR |
| **/game-types** | Creates a new `game-type`. | Gets all of the available `game-types`. | ERROR | ERROR |
| **/game-types/{game-type-id}** | ERROR | Gets details on `game-type` `{game-type-id}`. | Updates `game-type` `{game-type-id}`. | `Deletes` `game-type` `{game-type-id}`. |
| **/games** | Creates a new `game`. | ERROR | ERROR | ERROR |
| **/games/{game-id}** | ERROR | Gets details on `game` `{game-id}`. | Updates `game` `{game-id}`. | `Deletes` `game` `{game-id}`. |
| **/games/{game-id}/finish** | Finishes the `game` `{game-id}`. | ERROR | ERROR | ERROR |
| **/games/{game-id}/game-records** | Creates a new `game-record` for the `game`. | Gets all of the `game-records` for the `game` `{game-id`. Parameters: `{user-id}`. | ERROR | ERROR |
| **/game-records/{game-record-id}** | ERROR | Gets details on `game-record` `{game-record-id}`. | Updates `game-record` `{game-record-id}`. | `Deletes` `game-record` `{game-record-id}`. |
| **/transaction/{transaction-id}** | ERROR | Gets details on `transaction` `{transaction-id}`. | Updates `transaction` `{transaction-id}`. | `Deletes` `transaction` `{transaction-id}`. |
| **/bank-transaction/{bank-transaction-id}** | ERROR | Gets details on `bank-transaction` `{bank-transaction-id}`. | Updates `bank-transaction` `{bank-transaction-id}`. | `Deletes` `bank-transaction` `{bank-transaction-id}`. |
