Instuctions to build, run, and use

Command line:

    Primary:
        - The easiest way to run the app is to run 'mvn spring-boot:run' from the invoiceapi directory.

    Alrenative:
        Build:
         - Navigate to the invoiceapi directory, run 'mvn package' to create a jar in the /target/ directory

        Run:
         - Navigate to the invoiceapi/target directory, run 'java -jar {jarname}'.

Eclipse / IntelliJ:

    If the project is correctly imported to an IDE as a maven project, you should be able to run the
    InvoiceapiApplication class to launch the spring boot app


Usage:

    API documentation can be found when the app is running at /swagger-ui.html 