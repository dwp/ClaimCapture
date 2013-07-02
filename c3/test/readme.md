To only run only integration specs, run the following command from the command line:

```scala
test-only -- include integration
```

Note the use of the Tags trait and the section("integration")

To exclude the integration test, run the tests with the following command:

```scala
test-only -- exclude integration
```