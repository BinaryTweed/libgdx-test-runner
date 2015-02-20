package com.binarytweed.libgdx.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ExcludingTestClassLoaderTest
{
	@Test
	public void quarantinedClassIsFromDifferentClassLoader() throws Exception
	{
		ExcludingTestClassLoader loader = new ExcludingTestClassLoader();
		
		Class<?> objectClass = loader.loadClass("java.lang.Object");
		Class<?> gdxClass = loader.loadClass("com.badlogic.gdx.Gdx");
		
		assertThat(gdxClass.getClassLoader(), notNullValue());
		assertThat(gdxClass.getClassLoader(), not(objectClass.getClassLoader()));
	}
}
