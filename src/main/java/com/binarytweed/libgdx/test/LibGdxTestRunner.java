package com.binarytweed.libgdx.test;

import java.lang.Thread.UncaughtExceptionHandler;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class LibGdxTestRunner extends BlockJUnit4ClassRunner
{
	private static final Logger logger = LoggerFactory.getLogger(LibGdxTestRunner.class);
	
	
	public LibGdxTestRunner(Class<?> klass) throws InitializationError
	{
		super(klass);
	}

	volatile boolean finished = false;


	@Override
	public void run(final RunNotifier notifier)
	{
		logger.debug("Starting run of [{}]", getDescription());
		logger.trace("Test class [{}] loaded by class loader [{}]", getDescription(), this.getClass().getClassLoader());
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		config.title = "Test";
		config.forceExit = false;

		final LibGdxTestRunner runner = this;
		LwjglApplication app = null;

		try
		{
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
			{
				@Override
				public void uncaughtException(Thread t, Throwable e)
				{
					runner.finished = true;
					EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
					testNotifier.addFailure(e);
				}
			});

			/*
			 * Gdx.gl was not getting nulled out between tests. It may not be
			 * the direct cause, but there was no OpenGL context available after
			 * a few runs; null out Gdx.gl and it works again.
			 * 
			 * The 'correct' solution here is to use a new classloader for every
			 * test class, but this works for now.
			 */

			Gdx.gl = null;
			app = new LwjglApplication(new ApplicationListener()
			{
				@Override
				public void resume()
				{
				}


				@Override
				public void resize(int width, int height)
				{
				}


				@Override
				public void render()
				{
					if (!finished)
					{
						try
						{
							runner.actualRun(notifier);
						}
						catch (Throwable t)
						{
							notifier.fireTestFailure(new Failure(getDescription(), t));
						}
						finally
						{
							runner.finished = true;
						}
					}
				}


				@Override
				public void pause()
				{
				}


				@Override
				public void dispose()
				{
					logger.trace("Disposing");
				}


				@Override
				public void create()
				{
					logger.trace("Creating");
				}
			}, config);

			while (!finished)
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					logger.error("TestRunner thread interrupted whilst sleeping");
					notifier.fireTestFailure(new Failure(getDescription(), e));
				}
			}
		}
		catch (Throwable t)
		{
			notifier.fireTestFailure(new Failure(getDescription(), t));
		}
		finally
		{
			if (app != null)
			{
				app.stop();
			}
		}

		logger.trace("Ending run of [{}]", getDescription());
	}


	public void actualRun(RunNotifier notifier)
	{
		super.run(notifier);
	}

}
