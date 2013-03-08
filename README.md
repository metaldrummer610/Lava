# Lava (Query-able Collections API for Java)

This library is a more or less port of Microsoft's [LINQ](http://msdn.microsoft.com/en-us/library/vstudio/bb397926.aspx) library to Java.

## Building the project
The project uses [Gradle](http://www.gradle.org/) for it's build process. All one must do is a simple `gradle jar` command in the root directory and Gradle will take care of the rest.

### Dependencies
So far, the only dependency for the project is the [Guava](https://code.google.com/p/guava-libraries/) library. I am also including [jUnit](http://junit.sourceforge.net/) for testing, but that is not needed in a production release.

## Contributing to the project
To contribute to the project, please follow the [Git Flow](https://github.com/nvie/gitflow) workflow. I have checked the develop branch into github so that any cutting-edge development can take place there, while leaving the master for tagged releases. Please try to adhere to these guidelines.

If you do add some functionality to the project, or modify something, please make sure there is a unit test for it. I know I haven't fully implemented all the unit tests for the existing functionality yet, but that will come with time.
<<<<<<< HEAD
=======

>>>>>>> release/initial_release
