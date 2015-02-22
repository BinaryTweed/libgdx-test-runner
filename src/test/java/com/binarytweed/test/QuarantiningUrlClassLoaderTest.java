package com.binarytweed.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.binarytweed.libgdx.test.scratch.ReferencedClass;
import com.binarytweed.libgdx.test.scratch.ReferencingClass;
import com.binarytweed.test.QuarantiningUrlClassLoader;

@SuppressWarnings({"resource", "unchecked"})
public class QuarantiningUrlClassLoaderTest
{
	@Test
	public void quarantinedClassIsLoadedByUs() throws Exception
	{
		QuarantiningUrlClassLoader loader = new QuarantiningUrlClassLoader("com.badlogic");
		Class<?> gdxClass = loader.loadClass("com.badlogic.gdx.Gdx");
		assertThat(gdxClass.getClassLoader(), equalTo((ClassLoader) loader));
	}
	
	
	@Test
	public void otherClassIsLoadedByParent() throws Exception
	{
		QuarantiningUrlClassLoader loader = new QuarantiningUrlClassLoader("com.badlogic");
		Class<?> otherClass = loader.loadClass("java.sql.Timestamp");
		assertThat(otherClass.getClassLoader(), nullValue());
	}
	
	
	@Test
	public void otherQuarantinedClassesAvailable() throws Exception
	{
		QuarantiningUrlClassLoader loader = new QuarantiningUrlClassLoader("com.badlogic");
		Class<?> gdxClass = loader.loadClass("com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration");
		assertThat(gdxClass.getClassLoader(), equalTo((ClassLoader) loader));
	}
	
	
	@Test
	public void settingStaticVarBeforeIsolatedLoad() throws Exception
	{
		ReferencedClass.VALUE = 100;
		ReferencingClass instance = new ReferencingClass();
		
		QuarantiningUrlClassLoader loader = new QuarantiningUrlClassLoader(ReferencingClass.class.getName(), ReferencedClass.class.getName());
		
		
		Class<ReferencingClass> isolated = (Class<ReferencingClass>) loader.loadClass(ReferencingClass.class.getName());
		
		assertThat((Class<ReferencingClass>) instance.getClass(), not(equalTo(isolated)));
		assertThat((String) isolated.getDeclaredMethod("getReferencedMember").invoke(isolated.newInstance()), is("0"));
	}
}
