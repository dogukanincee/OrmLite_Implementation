# OrmLite Implementation for Android in Kotlin 

This project demonstrates the use of ORMlite to manage SQLite database on an Android application using Kotlin. 

ORMlite is a lightweight, easy-to-use Object-Relational Mapping library for Java and Android. 
It provides a simple and intuitive API to interact with relational databases.
Then simplifies the management of database objects by converting between Java objects and database records.

The project implements a simple CRUD (Create, Read, Update, Delete) application with a single entity Person. 
A person is represented by a name and an age. 
The user interface of the application allows the user to add, delete, and view persons in a list view. 
The application also provides an option to clear the entire database.

The project follows the MVVM (Model-View-ViewModel) architecture pattern. 
The Model layer is implemented using ORMlite to manage the database operations. 
The ViewModel layer acts as the mediator between the View and the Model layers and manages the business logic of the application. 
The View layer is implemented using the Android framework and provides the user interface for the application.

The following sections provide a detailed description of each layer in the project:

-----
Model Layer
-----
The Model layer consists of a single class Person which represents the entity that will be stored in the database. 
This class is annotated with ORMlite annotations that specify the mapping between the class fields and the database columns.

ORMlite provides two ways to manage database operations, namely the DAO (Data Access Object) and the RuntimeExceptionDao. In this project, the DAO approach is used to manage the database operations. The PersonRepository class is responsible for managing the database operations for the Person entity. This class provides methods to add, delete, and retrieve persons from the database. It also provides a LiveData object that is updated whenever a change is made to the database.

-----
ViewModel Layer
-----
The ViewModel layer consists of a single class PersonViewModel which is responsible for managing the business logic of the application. This class communicates with the PersonRepository class to perform database operations. It also provides LiveData objects that can be observed by the View layer to update the user interface.
The PersonViewModel class is responsible for managing the list of persons in the application. 
It provides methods to add, delete, and retrieve persons from the database. 
These methods are called by the View layer in response to user actions. 
The PersonViewModel class also provides a LiveData object that contains the list of persons in the database. 
This object is updated whenever a change is made to the database.

-----
View Layer
-----
The View layer is responsible for providing the user interface for the application. 
In this project, the user interface is implemented using a single activity MainActivity and a single fragment PersonListFragment.
The PersonListFragment displays the list of persons in the application. 
It observes the LiveData object provided by the PersonViewModel class and updates the list whenever a change is made to the database.

The MainActivity is responsible for managing the navigation between different fragments in the application. 
It uses the Navigation Component provided by the Android framework to implement navigation.

-----
Conclusion
-----
In conclusion, this project demonstrates the use of ORMlite to manage SQLite database on an Android application using Kotlin. 
It follows the MVVM architecture pattern and provides a simple and intuitive API to interact with the database. 
The application provides a simple CRUD functionality with a user interface that is implemented using the Android framework.
