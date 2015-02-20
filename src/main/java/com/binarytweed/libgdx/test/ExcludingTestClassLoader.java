package com.binarytweed.libgdx.test;

import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;


public class ExcludingTestClassLoader extends URLClassLoader
{
	private final Set<String> quarantinedClassNames;
	
	
	public ExcludingTestClassLoader(String... quarantinedClassNames)
	{
		super(((URLClassLoader) getSystemClassLoader()).getURLs());
		System.out.println("ExcludingTestClassLoader ("+this+") was loaded by "+getClass().getClassLoader());
		
		this.quarantinedClassNames = new HashSet<>();
		for(String className : quarantinedClassNames)
		{
			this.quarantinedClassNames.add(className);
		}
	}


	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		if(! quarantinedClassNames.contains(name))
		//if (! name.startsWith("com.badlogic") 
		//	&& ! name.startsWith("com.binarytweed.libgdx.test.InnerLibGdxTestRunner")
		//	&& ! name.startsWith("com.binarytweed.libgdx.test.LibGdxTest"))
		{
			System.out.println("Loaded by parent: "+name+", calling super.findClass");
			return super.loadClass(name);
		}
		Class<?> type = findClass(name);
		System.out.println("Loaded by us: "+name+", loaded by "+type.getClassLoader());
		return type;
	}
}