package test.java.quartzshard.projecttweaked.api;

import static org.junit.Assert.*;
import main.java.quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import org.junit.Test;

public class ProjectTwEakedAPITest
{

	@Test
	public void testGetEMCProxy() throws Exception
	{
		assertNotNull(ProjectTwEakedAPI.getEMCProxy());
	}

	@Test
	public void testGetConversionProxy() throws Exception
	{
		assertNotNull(ProjectTwEakedAPI.getConversionProxy());
	}

	@Test
	public void testGetTransmutationProxy() throws Exception
	{
		assertNotNull(ProjectTwEakedAPI.getTransmutationProxy());
	}

	@Test
	public void testGetBlacklistProxy() throws Exception
	{
		assertNotNull(ProjectTwEakedAPI.getBlacklistProxy());
	}
}