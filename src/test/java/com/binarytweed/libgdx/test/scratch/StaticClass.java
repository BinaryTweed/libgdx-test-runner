package com.binarytweed.libgdx.test.scratch;

public class StaticClass
{	
	public static int MEMBER = 0;
	public static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	public static ClassLoader LOADER_FROM_CONSTRUCTOR;
	
	
	public StaticClass()
	{
		LOADER_FROM_CONSTRUCTOR = Thread.currentThread().getContextClassLoader();
	}
}
