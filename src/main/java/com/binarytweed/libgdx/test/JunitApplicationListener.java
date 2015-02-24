package com.binarytweed.libgdx.test;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationListener;

class JunitApplicationListener implements ApplicationListener
{
	private static final Logger logger = LoggerFactory.getLogger(JunitApplicationListener.class);
	
	
	private final RunNotifier notifier;
	private final LibGdxTestRunner runner;


	JunitApplicationListener(RunNotifier notifier, LibGdxTestRunner runner)
	{
		this.notifier = notifier;
		this.runner = runner;
	}


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
		if (!runner.isFinished())
		{
			try
			{
				runner.invokeParentRun(notifier);
			}
			catch (Throwable t)
			{
				notifier.fireTestFailure(new Failure(runner.getDescription(), t));
			}
			finally
			{
				runner.setFinished();
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
}