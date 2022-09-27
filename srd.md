# Software Engineering

### ****Software Requirements Specification****

### ***(SRS) Document***

**Sandstone**

**9/15/2022**

**0.1.0**

**By: Fox Johnjulio, Benjamin Beach, Silas Bucur**

**We agree to abide by the UNCG academic integrity policy**


____

| **Table of Contents**                |
|--------------------------------------|
| 1. Introduction                      |
| 2. General Description               |
| 3. Functional Requirements           |
| 4. Technical Requirements            |
| 4.1 Operating System & Compatibility |
| 4.2 Interface requirements           |
| 4.2.1 User Interfaces                |
| 4.2.2 Hardware Interfaces            |
| 4.2.3 Communications Interfaces      |
| 4.2.4 Software Interfaces            |
| 5. Non-Functional Requirements       |

## 1. **Introduction**

### a. **Purpose:**

> Sandstone is designed to allow inventory management to see the inventory of their stock,
> the sales of their stock,

### b. **Document conventions:**

> "The purpose of this Software Requirements Document (SRD) is to
> describe the client and developer view requirements for Sandstone.
> Client-oriented requirements describe the system from the client's perspective.
> These requirements include a description of the different types of users served by
> the system. Developer-oriented requirements describe the system from the view of
> a software developer. These requirements include a detailed description of functional,
> data, performance, and other requirements."

### c. **Definitions, Acronyms, and Abbreviations**

| **Term**                              | **Definition, Acronym, Abbreviation**                                                                                                      |
|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Supply                                | A stock of a resource from which a person or place can be provided with the necessary amount of resource.                                  |                                                                                                                                            |
| Demand                                | An economic principle that the consumer wants to purchase goods or services and willingness to pay a price for a specific good or service. |                                                                                                                                         |
| Java                                  | A programming language created by Sun. We will be using this language to build Sandstone.                                                  |
| DB                                    | An abbreviation for Database.                                                                                                              |
| Vaadin                                | Vaadin is a website design tool that we use for the web pages                                                                              |
| Spring Boot                           | Spring Boot is a Java-based framework used to create spring applications with the help of microservices                                    |                                                                                                  
| Spring                                | Spring is a framework that is used to build Java applications                                                                              |
| JPA                                   | Java Persistence API is an API specification that is used to persistently store Java objects in a database.                                |                                                                                                                                  |
| API                                   | Application Programming Interface is a set of routines, protocols, and tools for building software applications.                           |
| JSON                                  | JavaScript Object Notation is a lightweight data-interchange format that is easy for humans to read and write.                             |
| HTML                                  | Hypertext Markup Language is the standard markup language for creating web pages and web applications.                                     |
| CSS                                   | Cascading Style Sheets is a style sheet language used for describing the presentation of a document written in a markup language           |

### d. **Intended audience:**

> The intended audience for this document is the developers and the
> project managers. The developers will use this document to understand
> the requirements of the project and the project managers will use this
> document to understand the requirements of the project.
>
> Secondary audiences include the users of the application. The users
> will make use of this document to understand the functionality of the application.
>
> The sections of the document that are intended for the developers are
> sections 3, 4, and 5. The sections of the document that are intended for
> the project managers are sections 2, 3, 4, and 5. The sections of the
> document that are intended for the users are sections 1, 2, and 3.

### e. **Project Scope:**

> The goals of the project are to create a software that will allow
> businesses to manage their inventory and sales. The software will allow
> businesses to see the inventory of their stock, the sales of their stock,
> and the profit of their stock. There are many benefits to this software as
> one might expect. The software goals align with the majority of all business
> goals as it will allow businesses to keep close watch over their sales and help
> them reorient their business to better suit their customers.
>

### f. **Technology Challenges:**

> The technology challenges that the project may face are those general to any
> software development project. The challenges include the development of the
> software, the testing of the software, and the deployment of the software.
> The development of the software will be a challenge as it will require the
> development of a web application. The testing of the software will be a
> challenge as it will require writing satisfactory test cases. The deployment
> of the software will be a challenge as it will require setting up a server to
> host the application.

### g. **References:**

> [1] Spring boot tutorial -https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm
>
> [2] Official Gradle documentation - https://docs.gradle.org/current/userguide/userguide.html
>
> [3] Spring Boot Gradle Plugin Reference
> Guide- https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/
>
> [4] Create an OCI image - https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#build-image
>
> [5] Vaadin - https://vaadin.com/docs/latest/tutorial/overview
>
> [6] Spring Data JPA - https://spring.io/projects/spring-data-jpa
>
> [7] Spring Web - https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web
>
> [8] Accessing Data with JPA - https://spring.io/guides/gs/accessing-data-jpa/
>
> [9] Storing Data persistently with JPA - https://stackoverflow.com/a/63216560

## 2. General Description

### 1. **Product perspective:**

We wanted to make this because we have to do it

### 2. **Product features:**

Allow users to register an account, log in, log out, determine their
storehouse items as well as their stock's price, sales, profit, and supply.

### 3. **User class and characteristics:**

> Our website application does not expect our users to have any prior
> knowledge of a computer, apart from using a web browser.
> The users of our application are expected to be able to understand
> the basic concepts of supply and demand. It would be helpful for the users of our
> application to have some knowledge of macroeconomics and microeconomics, but it is not required.
>
> User classes:
> - **Administrators:** The administrators are the people who have the highest level of access to the application and
    can perform all operations.
> - **Managers:** The manager is the person who has the second-highest level of access to the application and can
    perform all managerial operations.
> - **Users:** The users are the people who have the second-lowest level of access to the application and can only
    perform operations that are related to the inventory and sales of the stock.
> - **Guests:** The guest is the person who has the lowest level of access to the application and can only browse public
    pages of the application.

### 4. **Operating environment:**

> The operating environment of the software is a web browser. The software will be
> deployed on a server and will be accessed by the users through a web browser.

### 5. **Constraints:** Any limiting factors that would pose challenge to

> The design and implementation constraints of the software are the following:
> - Due to the use of a web application, the software will only be accessible over the internet
> - Some features of the software will only be available to users who have an account
> - The software will only be accessible through a web browser
> - The user data will be stored in a database and all important data will be hashed and salted (like potatoes)

### 6. **Assumptions and dependencies:**

> The assumptions that we have made regarding the software product are the following:
> - The software will run on modern web browsers
> - The software will be deployed on a server
> - The software will be accessed by the users over the internet or on a local network
> - The APIs used by the software will be stable and will not change
> - The earth is round and not flat
>
> The dependencies that we have made regarding the software product are the following:
> - Vaadin
> - Spring Boot
> - Spring Data JPA
> - H2 Database
> - Spring Web
> - Gradle
> - PlaceHolderAPI
> - GSON

## Functional Requirements

### 1. **Primary**

- FR0: When the user visits the main page, they will be asked to log in or register
  an account if they do not have one, they cannot access the website
- FR1: When the user logs in or registers successfully, they will be redirected to the dashboard page where they can
  interact according to their permission level
- FR2: When the user visits the dashboard page, they will be able to see the inventory of their stock, the sales of
  their stock, and the profit of their stock
    - FR2.1: The dashboard page will contain a search bar that will allow the user to search for a specific item in
      their inventory
    - FR2.2: The dashboard page will contain a button that will allow the user to add a new item to their inventory or
      to import items from a JSON file
- FR3: When the user clicks on one of the items on the menu bar, they will be taken to their respective pages
    - FR3.1: When the user clicks on the "Account" button, they will be taken to their account
    - FR3.2: When the user clicks on the "Home" button, they will be taken to the dashboard page
    - FR3.3: When the user clicks on the "Upload" button, they will be taken to the upload page
    - FR3.4: When the user clicks on the "Logout" button, they will be logged out and redirected to the main page and be
      logged out
    - FR3.5: If the user is an administrator, they will be able to see the "Admin" button on the menu bar
        - FR3.5.1: When the user clicks on the "Admin" button, they will be taken to the admin page where they will be
          able to
        - FR3.5.1.1: See the list of all the users
        - FR3.5.1.2: See the list of all the managers
        - FR3.5.1.3: Edit the information of a user
        - FR3.5.1.4: Delete a user
        - FR3.5.1.5: Restart the application
        - FR3.5.1.6: See an audit log of all the actions that have been performed
- FR4: When the user visits the account page, they will be able to see their account information
    - FR4.1: The account page will contain a button that will allow the user to edit their account information
    - FR4.2: The account page will contain a button that will allow the user to delete their account
- FR5: When the user visits the upload page, they will be able to upload a JSON file that contains the items that they
  want to add to their inventory

### 5. Technical Requirements

1. **Operating System & Compatibility**

    1. **Interface requirements**
   > The software will be accessible through a web browser

    2. **User Interfaces**
   > The software will have a graphical user interface

> The user interface of the software is designed on Vaadin
> The user interface of the software is designed to be simple and easy to use.
> The user interface of the software is designed to be responsive and to work on all devices. The user interface of the
> software is designed to be accessible to all users.

3. **Hardware Interfaces**

> The hardware interfaces of the software are the following:
> - The software will be deployed on a server
> - The software will be accessed by the users through a web browser on any device that supports a web browser

4. **Communication Interfaces**

> The communication interfaces of the software are as follows:
> - HTTP
> - HTTPS
> - JSON

5. **Software Interfaces**

> The software will use the following public APIs:
> 1.) [Placeholder](https://placeholder.com/)
> 2.) [PicSum](https://picsum.photos/)
>

## Non-Functional Requirements

1. **Performance requirements**

- NFR1(R): The user's computer must be capable of running a modern web browser

- NFR2(R): The system will require enough memory to run the web browser

- NFR3(R): The novice user will be able to upload their JSON data in
  less than 2 minutes.

- NFR4(R): The expert user will be able to upload their JSON data in
  less than 10 seconds.

2. **Safety requirements**

- NFR5(R): User passwords will always be obfuscated.

3. **Security requirements**

- NFR6(R): The system will only be usable by authorized users.

    1. **Software quality attributes**

        1. Availability

        2. Correctness

        3. Maintainability

        4. Reusability

        5. Portability

4. **Process Requirements**
    1. The software will be developed using the Git version control system

    2. The software will be developed using the Scrum methodology

    3. Meetings will be held every week to discuss the progress of the
       project

    4. All the meetings will be held on Discord

    5. The software will be developed using the Agile methodology
        1. The software will be developed in iterations

        2. Sprints will be held every week

        3. The software will be developed in a test-driven manner

    6. The delivery date of the software will be 2022-11-29
    7. The cost of the software will be $0.00 because we are wholesome
       people

5. **Other requirements**


- NFR7(R): The system will conform to all local, regional, and national
  laws. We will not be held responsible for any illegal actions that are performed using the system.
- NFR8(R): The system will be free to use for all users.
- NFR9(R): The system will be updated on an as-needed basis.

## Appendix

### 1. [Use Case Model](usecasemodel.md)
