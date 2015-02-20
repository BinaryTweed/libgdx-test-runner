package com.binarytweed.libgdx.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.Gdx;

public class ClassLoaderContaminationTest
{
	@Test
	public void gdxWithoutQuarantinedClassLoaderIsUninitialised()
	{
		assertThat(Gdx.app, nullValue());
	}
}
