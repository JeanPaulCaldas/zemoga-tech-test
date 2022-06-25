# Zemoga Code Test

This is an app that lists all messages and details from the free REST API [{JSON} Placeholder](https://jsonplaceholder.typicode.com/) .
To meet the specified criteria, and expose best practices within my capabilities, some considerations are exposed as follows:

## Configurations

- Min Android API level support: from Oreo (API 26)
- Written with Kotlin v 1.7.0
- JVM Target = 1.8

## Architecture (Clean)

I propose **Clean Architecture**, as it follows SOLID principles, it allows you to decouple units while mantaining organized code, which makes unit maintenance and testing easier, as well as fits better to organize large teams.

Another benefit on this aproach is that you can have a **core sub-module (as this project)** that depends only on Kotlin and has no dependencies on android framework, which is ideal for multiplatfom projects.

For the presentation i used **MVI** (Model-View-Intent) pattern, inspired on unidirectional data flow paradigm that uses reactive instead of imperative programming, which in my opinion is the present and future of mobile apps.

#### Layers

1. [App] **Presentation**
   UI components (Activities, Fragments, Views, ViewModels) and another android framework components
2. [Core] **Use Cases or Interactors**
   Represents common actions on the app
3. [Core] **Domain**
   Bussiness models and rules
4. [Core] **Data**
   Repository pattern implementation and data sources contracts
5. [App] **Framework**
   Framework implementations as cache, network, etc. And data sources contract implementations from data layer.

The following diagram represents the communication between layers, note that the data layer should not depend on the framework layer, so the **principle of dependency inversion** applies, create a data source contract that represents the communication with the framework layer.

![clean-diagram](/images/clean-diagram.png)

## Third party libraries

Use of third party libs as follows:

- **Coroutines**: to execute asyncronous code avoiding blocking the main thread for heavy tasks, usually, writte and read operations in a cleaner and more direct way, in this project is applied to network and cache access
- **View Pager 2**: to implement the swipe All|Favorites post views suggested in the mockup, support tabs, finger gestures, etc. The main benefit it has is the handling fragments in an optimized way
- **Dagger Hilt**: an android library to handle and reduce boilerplate when doing dependency injection, provides containers for android classes that support appropiate scopes, improving development productivity, application performance and scalability
- **Room**: is a persistence library that works as an abstraction over the well known SQLite database, improves development speed and allows integration with libs like coroutines and javaRX to support asynchronous operations
- **Retrofit**: is an easy to use HTTP Client tool, very powerfull to handle network requests, some important features are HTTP annotations, request body access, encoding, multiparts, header manipulation, asynchronous calls, customizable converters for convenient serialization, in this project handles network requests
- **Navigation with Safe Args**: from Jetpack, allows to handle user navigation across the app, improves development speed and handle args safety between destinations, introduces an XML configuration file to define app destinations, in this project is used to navigate between posts pager and post details fragment, passing the post id as argument. 