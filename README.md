# Shopping-List
A shopping list Android app built with Kotlin as a class project for AIT-Budapest's Mobile Software Develeopment course.

## Features
- Starts with a Splash Activity that displays the app logo with an animation and jumps to the shopping list after 3 seconds.
- The user can add items to the shopping list with the following attributes:
    - name
    - category: food/drink, supplies, technology, sport, other
    - the icon of the item (determined by it’s category)
    - description
    - price
    - status: whether the item has been bought yet
- Removing items in three ways: one-by one, all items, all items marked as bought.
- Editing item attributes.
- Database/persistence data storage using [Room](https://developer.android.com/training/data-storage/room/index.html):
- Architecture:
    - SplashActivity
    - ListActivity
        - CoordinatorLayout, AppBarLayout, ...
    - RecyclerView
        - ViewHolder
        - Adapter class
    - DialogFragment for adding and editing items

## Acknowledgements
Peter Ekler, AIT-Budapest
