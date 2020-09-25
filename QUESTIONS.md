# Questions

Try compiling the code in the project. You should quickly run into a problem.

You should see an error message like

```
[error] -- [E008] Not Found Error: /todo/src/main/scala/todo/TodoService.scala:5:18 
[error] 5 |import org.http4s.circe._
[error]   |       ^^^^^^^^^^^^^^^^
[error]   |       value circe is not a member of org.http4s
[error] -- [E049] Reference Error: /todo/src/main/scala/todo/TodoService.scala:21:4 
[error] 21 |    HttpRoutes.of[IO]{
[error]    |    ^^^^^^^^^^
[error]    |Reference to HttpRoutes is ambiguous,
[error]    |it is both imported by name by import cats.effect.IO
[error]    |and imported subsequently by import (<error value circe is not a member of org.http4s>#org.http4s.dsl.io : 
[error]    |  <error value circe is not a member of org.http4s>
[error]    |)._
[error] two errors found
```

This is caused by missing dependencies. You need to add the following library dependencies to the project:

```
"org.http4s" %% "http4s-circe" % Http4sVersion
"org.http4s" %% "http4s-dsl" % Http4sVersion
```

You will need to add Dotty compatibility to these dependencies (`withDottyCompat`).

With these changes the code should compile. Now run the code (use the `run` command in sbt) and visit `http://localhost:3000/` in your browser. You should see the interface of a simple todo application. Play around with the interface. Try adding and completing tasks. You will notice that the application does not work. Let's fix it.


## Overview of the Code Base

The code consists of four components:

- `Main.scala`, which brings everything together and runs the webserver;
- the services in `TodoService.scala` and `AssetService.scala`, which respond to HTTP requests;
- the models in `InMemoryModel.scala` and `PersistentModel.scala`, which implement the application logic and have a common interface defined in `Model.scala`; and
- the data definitions in the `data` subpackage.

We'll focus on the models. We won't need to change other code for the most part (though if you want to take this project further on your own you may wish to do so).


## Fix the In-Memory Model

Run the tests from sbt (the `test` command). You'll see the tests are currently failing. Start first on the tests for the `InMemoryModel`. Take a look at `InMemoryModel.scala`. We have provided part of an implementation for you but it contains mistakes. Fix the implementation so that the tests pass. When you have done this you will have a working application!


## Fix the Persistent Model

The `InMemoryModel` loses any changes you make each time you restart the server. A more useful system would persist changes between runs. This is what `PersistentModel` does. Here we have provided a few useful utilities for you, but you'll have to do much more of the work required to get it working.
