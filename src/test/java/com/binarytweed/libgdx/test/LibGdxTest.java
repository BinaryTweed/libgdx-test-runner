package com.binarytweed.libgdx.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.binarytweed.test.QuarantiningUrlClassLoader;

@RunWith(LibGdxTestRunner.class)
@Quarantine({"com.badlogic"})
public class LibGdxTest
{	
	@Test
	public void gdxLoadedByQuarantinedClassLoader()
	{
		Class<?> gdxClassLoader = Gdx.class.getClassLoader().getClass();
		assertThat(gdxClassLoader.getName(), is(QuarantiningUrlClassLoader.class.getName()));
	}
	
	
	@Test
	public void appListenerIsLoaded()
	{
		ApplicationListener app = Gdx.app.getApplicationListener();
		assertThat(app, notNullValue());
	}
	
	
	@Test
	public void localStorageIsWritableAndReadable()
	{
		FileHandle writeHandle = Gdx.files.local("foo");
		writeHandle.writeString("bar", false);
		
		FileHandle readHandle = Gdx.files.local("foo");
		String actual = readHandle.readString();
		assertThat(actual, is("bar"));
		
		boolean deleted = readHandle.delete();
		assertThat(deleted, is(true));
	}
	
	
	@Test
	public void textureCanBeDrawn()
	{
		Texture texture = new Texture("fixtures/texture.png");
		SpriteBatch spriteBatch = new SpriteBatch();
		spriteBatch.begin();
		spriteBatch.draw(texture, 0f, 0f);
		spriteBatch.end();
	}
}
