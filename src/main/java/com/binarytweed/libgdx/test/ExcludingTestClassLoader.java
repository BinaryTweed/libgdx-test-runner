package com.binarytweed.libgdx.test;

import java.net.URL;
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
		boolean quarantine = false;
		
		for(String quarantinedPattern : quarantinedClassNames)
		{
			if(name.startsWith(quarantinedPattern))
			{
				quarantine = true;
				break;
			}
		}
		
		if(quarantine)
		{
			System.out.println("Detected quarantined class: "+name);
			try
			{
				return findClass(name);
			}
			catch (ClassNotFoundException e)
			{
				System.out.println("Could not load "+name);
				throw e;
			}
		}

		System.out.println("Loaded by parent: "+name+", calling super.loadClass");
		return super.loadClass(name);
	}
}