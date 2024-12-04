package com.mayab.quality.doubles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class DependencyTest {

	private Dependency dependency;
	private SubDependency subdependency;
	//public static final int 
	
	@BeforeEach
	public void setupMock() {
		subdependency = mock(SubDependency.class);
		dependency = mock (Dependency.class);
	}
	
	@Test
	void getClassNameTest() {
		when(dependency.getSubDependencyClassName()).thenReturn("SubDependency.class");
		//exercise
		String name = dependency.getSubDependencyClassName();
		//assert
		assertThat(name, is("SubDependency.class"));
	}
	
	/*@Test
	void addTwoTest() {
		int i = 4;
		int expResult = 4;
		int result = dependency.addTwo(i);
		assertThat(result, is(expResult));		
	}*/
	
	/*@Test
	//mock
	void addTwoTestMock() {
		int i = 4;
		int expResult = 6;
		when(dependency.addTwo(anyInt())).thenReturn(expResult);
		int result = dependency.addTwo(i);
		assertThat(result, is(expResult));		
	}*/
	
	/*@Test
	public void testAnswer() {
		when(dependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				int arg = (Integer) invocation.getArguments()[0];
				return arg + 20;
			}
		});
	assertEquals(26, dependency.addTwo(6));
	}*/
	
	@Test
	public void testCallMethod() {
		when(dependency.addTwo(anyInt())).thenCallRealMethod();
		assertThat(8, is(dependency.addTwo(6)));
	}


}
