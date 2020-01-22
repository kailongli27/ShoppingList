# ShoppingList
An Android application that allows users to create a shopping list. Users can add, update and delete items. To add an item to the list, the user must enter the item's name as well as the quantity to be purchased.

### Important Files

- [Application Layout Files](app/src/main/res/layout)
- Activity Files
  - [Main Activity](app/src/main/java/com/example/shoppinglist/MainActivity.java)
  - [List Activity](app/src/main/java/com/example/shoppinglist/ListActivity.java)
- [Database Handler](app/src/main/java/com/example/shoppinglist/data/DatabaseHandler.java): an implementation of a SQLite database. This class also implements the CRUD (create, read, update, delete) operations that are used to add, update and delete shopping items.
- [Constants](app/src/main/java/com/example/shoppinglist/util/Constants.java): creates the columns of the database table used in the DatabaseHandler class.
- [Recycler View Adapter](app/src/main/java/com/example/shoppinglist/ui/RecyclerViewAdapter.java): connects data to an Android RecyclerView widget that is used for the user interface.

All other relevant files can be found [here](app/src/main/java/com/example/shoppinglist/).

### Demonstration of Application
