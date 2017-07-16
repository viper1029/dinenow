# DineNow

DineNow is an online food ordering system, similar to JustEat and GrubHub.

There are three components to this application.

# The Backend Server 

This is a platform independent rest API. It's meant to be used by a variety of clients such as mobile, website, etc.

* Dropwizard is used for the REST API.
* Hibernate is used for the ORM.
* Gradle for dependency management.
* Guice for dependency injection.
* Swagger to REST API documentation.
* JSON Webtoken for Authentication

# Dashboard

The dashboard is a web application which can be used to view pending orders, accept orders, manage item menu, etc. The functionality
available in the dashboard depends on the type of account: Admin or Owner (restaurant owner). The admin can manage all restaurants and
view orders of all the restaurants on the system.

* The dashboard is built using AngularJS.

# Mobile App

Mobile application for customers for placing orders.

* React Native is used for the mobile application.
