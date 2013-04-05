# Lava [![Build Status](https://travis-ci.org/metaldrummer610/Lava.png)](https://travis-ci.org/metaldrummer610/Lava)

## What is Lava?
**Lava** is a Java library that is built to do one thing, make collections easier to work with. Lava is a more or less port of Microsoft's [LINQ](http://msdn.microsoft.com/en-us/library/vstudio/bb397926.aspx) library.

## Why would I want to use Lava?
Have you ever needed to search through an `ArrayList` of People to find everyone who's name begins with the letter 'R'? Lava has a convience method for that. Ever need to transform a `Collection` from one type to another? Lava has a method for that as well. Lava has a plethora of methods that allow you to modify collections to your hearts content.

## So how do I get started?
Well, first you need to clone the master branch of Lava and build the project (sorry, no Maven artifact yet...). Once it's built and placed in your classpath, you'll want to turn your attention to the `Lava` class. This class exposes all of the methods of the library statically, so there's no need for any setup. 

(Almost) all of the `Lava` methods return an object of type `Enumerable`, which allow you to chain method calls. For example, you could filter a list using `where`, transform the result using `select`, then sort the results using `orderBy` all in a single method chain.

For more information on the classes and methods available in **Lava**, take a look at the [JavaDocs](http://metaldrummer610.github.com/Lava/javadoc/). More documentation can be found at the project's [homepage](http://metaldrummer610.github.com/Lava)

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
