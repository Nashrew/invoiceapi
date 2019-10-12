Instuctions to build, run, and use

Requirements

    - Java 8
    - Maven (3.5.2 used in development)

Command line:

    Primary:
        - The easiest way to run the app is to run 'mvn spring-boot:run' from the invoiceapi directory.

    Alternative:
        Build:
         - Navigate to the invoiceapi directory, run 'mvn package' to create a jar in the /target/ directory

        Run:
         - Navigate to the invoiceapi/target directory, run 'java -jar {jarname}'.

Eclipse / IntelliJ:

    If the project is correctly imported to an IDE as a maven project, you should be able to run the
    InvoiceapiApplication class to launch the spring boot app


Usage:

    API documentation can be found when the app is running at /swagger-ui.html 
    H2 Database console can be accessed at /h2 - you can log in as sa with no password
