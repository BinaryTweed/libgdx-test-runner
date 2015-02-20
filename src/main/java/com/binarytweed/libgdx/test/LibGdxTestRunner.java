package com.binarytweed.libgdx.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class LibGdxTestRunner extends Runner
{
	private final Object innerRunner;
	private final Class<?> innerRunnerClass;
	
	
	public LibGdxTestRunner(Class<?> testFileClass) throws InitializationError
	{
		try(ExcludingTestClassLoader classLoader = new ExcludingTestClassLoader(testFileClass.getName(), "com.badlogic", "com.binarytweed.libgdx.test.InnerLibGdxTestRunner"))
		{
			innerRunnerClass = classLoader.loadClass(InnerLibGdxTestRunner.class.getName());
			Class<?> testClass = classLoader.loadClass(testFileClass.getName());
			innerRunner = innerRunnerClass.cast(innerRunnerClass.getConstructor(Class.class).newInstance(testClass));
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | IOException e)
		{
			throw new InitializationError(e);
		}	
	}


	@Override
	public Description getDescription()
	{
		try
		{
			return (Description) innerRunnerClass.getMethod("getDescription").invoke(innerRunner);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e)
		{
			throw new RuntimeException("Could not get description");
		}
	}

	@Override
	public void run(RunNotifier notifier)
	{
		try
		{
			innerRunnerClass.getMethod("run", RunNotifier.class).invoke(innerRunner, notifier);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e)
		{
			throw new RuntimeException("Could not invoke inner runner");
		}
	}

}
