# libgdx-test-runner

[![Build Status](https://travis-ci.org/BinaryTweed/libgdx-test-runner.svg)](https://travis-ci.org/BinaryTweed/libgdx-test-runner)

A JUnit test runner that runs test methods in a LibGDX `ApplicationListener`. This means all the `Gdx.*` statics are initialised and available to use.



## To use (resetting statics between each test class)

```java
@RunWith(QuarantiningRunner.class)
@Quarantine({"com.badlogic", "com.binarytweed.libgdx.test"})
@DelegateRunningTo(LibGdxTestRunner.class)
public class MyLibGdxTest
{

   @Test
   public void loadTexture()
   {
      Texture texture = new Texture("files/whatever.png");
      assertThat(texture, notNullValue());
   }
...
```

1. Annotate your test class with `@RunWith(QuarantiningRunner.class)`. This will use a `Runner` that loads classes specified below with a separate `ClassLoader` for each test.
1. Use `@Quarantine("com.badlogic", "com.binarytweed.libgdx.test")` to specify that classes from these packages will be loaded in a separate `ClassLoader`, so that the `Gdx.*` statics will be reset between tests.
1. Specify `@DelegateRunningTo(LibGdxTestRunner.class)` so that `QuarantiningRunner` knows to use `LibGdxTestRunner`, which runs your tests in an `ApplicationListener	` instance.

## Assets

If your assets are on the (test) classpath, they should just work. If you want to use separate test assets, you can go Maven-tastic and stick them in `src/test/resources/`.

## Known Issues
1. The ApplicationListener's config is hardcoded. Please raise an issue letting me know which aspects you want to be parameterisable
1. Tests won't pass on a headless system
1. Only the LWJGL backend is supported. If you need any other backend, raise an issue and we can see what's possible.