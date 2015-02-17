package com.binarytweed.libgdx.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.binarytweed.libgdx.test.LibGdxTestRunner;

@RunWith(LibGdxTestRunner.class)
public class LibGdxTest
{

	public LibGdxTest()
	{
		// TODO Auto-generated constructor stub
	}
	
	
	@Test
	public void testTextureLoading()
	{
		System.out.println("In test method");
		assertThat(true, is(true));
	}

}
