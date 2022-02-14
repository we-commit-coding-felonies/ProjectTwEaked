package moze_intel.projecte.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ProjectEAPITest
{
	
	//For Sol: Uncomment this and ./gradlew test to see why these are useful
	// https://www.javatpoint.com/assertion-in-java
	//@Test
	//public void testTest() throws Exception
	//{
	//	assertNotNull(null); //This can be anything that asserts something, to check something was returning the correct value of 1024, you could `assert return1024()=1024`
	//}

	@Test
	public void testGetEMCProxy() throws Exception
	{
		assertNotNull(ProjectEAPI.getEMCProxy());
	}

	@Test
	public void testGetConversionProxy() throws Exception
	{
		assertNotNull(ProjectEAPI.getConversionProxy());
	}

	@Test
	public void testGetTransmutationProxy() throws Exception
	{
		assertNotNull(ProjectEAPI.getTransmutationProxy());
	}

	@Test
	public void testGetBlacklistProxy() throws Exception
	{
		assertNotNull(ProjectEAPI.getBlacklistProxy());
	}
}