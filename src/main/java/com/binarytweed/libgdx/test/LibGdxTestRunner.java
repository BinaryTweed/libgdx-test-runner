package com.binarytweed.libgdx.test;

import java.lang.reflect.InvocationTargetException;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.binarytweed.test.QuarantiningUrlClassLoader;

public class LibGdxTestRunner extends Runner
{
	private final Object innerRunner;
	private final Class<?> innerRunnerClass;
	
	
	public LibGdxTestRunner(Class<?> testFileClass) throws InitializationError
	{
		String testFileClassName = testFileClass.getName();
		String innerRunnerClassName = InnerLibGdxTestRunner.class.getName();
		
		QuarantiningUrlClassLoader classLoader = new QuarantiningUrlClassLoader(testFileClassName, innerRunnerClassName, "com.badlogic");
		
		try
		{
			innerRunnerClass = classLoader.loadClass(innerRunnerClassName);
			Class<?> testClass = classLoader.loadClass(testFileClassName);
			innerRunner = innerRunnerClass.cast(innerRunnerClass.getConstructor(Class.class).newInstance(testClass));
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException  e)
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
			throw new RuntimeException("Could not get description", e);
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
			notifier.fireTestFailure(new Failure(getDescription(), e));
		}
	}

}
