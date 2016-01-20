### Examples for using the configuration option `annotationProcessorPaths` in `maven-compiler-plugin` 3.5.

On the command-line, use `mvn clean compile` on the projects to check that they indeed compile.

Opened / imported in the IDE, the Maven projects should compile without errors as well (once the annotation processor settings are adapted properly).

Description of the projects:

* `real-world-mapstruct`: a single module project that uses the MapStruct annotation processor to generate a class. The MapStruct processor jar has no transitive dependencies.
* `synth-with-transitive-deps`: multi-module project that contains one bogus processor implementation and a module that uses this processor:
 * `sample-processor-with-transitive-deps` implements the processor and uses a transitive dependency to guava.
 * `uses-processor-with-transitive-deps` uses the processor and does not compile successfully if the processor classpath is incomplete.
 * Note: On the command-line the multi-module project builds successfully with `mvn clean compile`, meaning that the processor is picked up from the reactor (from `../sample-processor-with-transitive-deps/target/classes`). I don't know if every IDE is capable of referencing other open projects in the processor path. It may be necessary to `mvn install` the processor module first for those IDEs. To be fair, in the Maven build the reactor build order is currently not aware that the processor must be built before the using project - so it's working only out of luck and by using the declaration order of the modules in the reactor poms. It'll break on parallel maven builds.