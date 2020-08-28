# Questions

Try compiling the code in the project. You should quickly run into a problem.




You should see an error message like

```
[error] -- [E008] Not Found Error: /Users/noel/dev/inner-product/scala-center/todo/src/main/scala/todo/TodoService.scala:5:18 
[error] 5 |import org.http4s.circe._
[error]   |       ^^^^^^^^^^^^^^^^
[error]   |       value circe is not a member of org.http4s
[error] -- [E049] Reference Error: /Users/noel/dev/inner-product/scala-center/todo/src/main/scala/todo/TodoService.scala:14:4 
[error] 14 |    HttpRoutes.of[IO]{
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

With these changes the code should compile. Now run the code (use the `run` command in sbt) and visit `http://localhost:3000/` in your browser. You should see the interface of a simple todo application. Unfortunately it doesn't work! Let's fix it.


## Fix the Model

Run the tests from sbt (the `test` command). You'll see the tests are currently failing. You'll need to fix the tests as a first step to get the code working. Go to the file `Model.scala` and fix it until the tests work.


## Create Working Actions

Look at the files `Actions.scala` and `DefaultActions.scala`. `Actions` specifies an interface that connects HTTP endpoints to the model. The current implementation is given by `DefaultActions`, and it is used in `Main.scala` where the `app` is constructed. This implementation doesn't do very much. Create a *new* implementation of `Actions` that correctly implements the interface and fix the app.


Once you fix the above you should have a working todo list app!
