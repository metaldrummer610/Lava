# Lava (Query-able Collections API for Java)

This library is a more or less port of Microsoft's [LINQ](http://msdn.microsoft.com/en-us/library/vstudio/bb397926.aspx) library to Java.

## Building the project
The project uses [Gradle](http://www.gradle.org/) for it's build process. All one must do is a simple `gradle jar` command in the root directory and Gradle will take care of the rest.

### Dependencies
So far, the only dependency for the project is the [Guava](https://code.google.com/p/guava-libraries/) library. I am also including [jUnit](http://junit.sourceforge.net/) for testing, but that is not needed in a production release.

## Contributing to the project
To contribute to the project, please follow the [Git Flow](https://github.com/nvie/gitflow) workflow. I have checked the develop branch into github so that any cutting-edge development can take place there, while leaving the master for tagged releases. Please try to adhere to these guidelines.

Steps to add a method to the project:

1. run `git flow feature start {name of method}`
2. Add the desired method to LavaBase.java
3. Add the static version of the method to Lava.java
4. Add the instance version of the method to Enumerable.java (but make sure to remove the 'source' collection from the parameters, as the source will become the current Enumerable's `collection` member)
5. Add a unit test for the new function (make sure it works!)
6. Make sure it's all committed and run `git flow feature finish`
7. Push your changes to the develop branch
