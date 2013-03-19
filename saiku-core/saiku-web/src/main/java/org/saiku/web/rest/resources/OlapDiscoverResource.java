/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.saiku.web.rest.resources;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.saiku.olap.dto.SaikuConnection;
import org.saiku.olap.dto.SaikuCube;
import org.saiku.olap.dto.SaikuDimension;
import org.saiku.olap.dto.SaikuHierarchy;
import org.saiku.olap.dto.SaikuLevel;
import org.saiku.olap.dto.SaikuMember;
import org.saiku.service.olap.OlapDiscoverService;
import org.saiku.service.util.exception.SaikuServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Path("/saiku/{username}/discover")
public class OlapDiscoverResource implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OlapDiscoverService olapDiscoverService;
    
    private static final Logger log = LoggerFactory.getLogger(OlapDiscoverResource.class);
    
    public void setOlapDiscoverService(OlapDiscoverService olapds) {
        olapDiscoverService = olapds;
    }
    
    /**
     * Returns the datasources available.
     */
    @GET
    @Produces({"application/json" })
    @Path("/{agency}")
     public List<SaikuConnection> getConnections( @PathParam("agency") Integer agency ) {
    	try {
      log.warn("Entrer");
			return olapDiscoverService.getAllConnections();
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
			return new ArrayList<SaikuConnection>();
		}
    }
    
    
    /**
     * Returns the datasources available.
     */
    @GET
    @Produces({"application/json" })
    @Path("/{connection}/{agency}")
     public List<SaikuConnection> getConnections(
         @PathParam("connection") String connectionName,
         @PathParam("agency") Integer agency ) {
    	try {
			return olapDiscoverService.getConnection(connectionName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
			return new ArrayList<SaikuConnection>();
		}
    }


    @GET
    @Produces({"application/json" })
  	@Path("/refresh/{agency}")
     public List<SaikuConnection> refreshConnections( @PathParam("agency") Integer agency ) {
    	try {
    		olapDiscoverService.refreshAllConnections();
			return olapDiscoverService.getAllConnections();
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
			return new ArrayList<SaikuConnection>();
		}
    }
    
    @GET
    @Produces({"application/json" })
    @Path("/{connection}/refresh/{agency}")
     public List<SaikuConnection> refreshConnection(
         @PathParam("connection") String connectionName,
         @PathParam("agency") Integer agency ) {
    	try {
			olapDiscoverService.refreshConnection(connectionName);
			return olapDiscoverService.getConnection(connectionName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
			return new ArrayList<SaikuConnection>();
		}
    }
    
	@GET
    @Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/dimensions/{agency}")
     public List<SaikuDimension> getDimensions(
    		 @PathParam("connection") String connectionName, 
    		 @PathParam("catalog") String catalogName, 
    		 @PathParam("schema") String schemaName, 
    		 @PathParam("cube") String cubeName,
         @PathParam("agency") Integer agency ) 
    {
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getAllDimensions(cube);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuDimension>();
	}
	
	@GET
    @Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/dimensions/{dimension}/{agency}")
     public SaikuDimension getDimension(
    		 @PathParam("connection") String connectionName, 
    		 @PathParam("catalog") String catalogName, 
    		 @PathParam("schema") String schemaName, 
    		 @PathParam("cube") String cubeName,
    		 @PathParam("dimension") String dimensionName,
         @PathParam("agency") Integer agency) 
    {
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getDimension(cube, dimensionName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return null;
	}
	
	@GET
    @Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/dimensions/{dimension}/hierarchies/{agency}")
     public List<SaikuHierarchy> getDimensionHierarchies(@PathParam("connection") String connectionName, 
    		 									@PathParam("catalog") String catalogName, 
    		 									@PathParam("schema") String schemaName, 
    		 									@PathParam("cube") String cubeName, 
    		 									@PathParam("dimension") String dimensionName,
                          @PathParam("agency") Integer agency) {
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getAllDimensionHierarchies(cube, dimensionName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuHierarchy>();
	}
	
	@GET
	@Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/dimensions/{dimension}/hierarchies/{hierarchy}/levels/{agency}")
	public List<SaikuLevel> getHierarchy(@PathParam("connection") String connectionName, 
				@PathParam("catalog") String catalogName, 
				@PathParam("schema") String schemaName, 
				@PathParam("cube") String cubeName, 
				@PathParam("dimension") String dimensionName, 
				@PathParam("hierarchy") String hierarchyName,
        @PathParam("agency") Integer agency)
	{
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getAllHierarchyLevels(cube, dimensionName, hierarchyName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuLevel>();
	}

	/**
	 * Get level information.
	 * @return 
	 */
	@GET
	@Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/dimensions/{dimension}/hierarchies/{hierarchy}/levels/{level}/{agency}")
	public List<SaikuMember> getLevelMembers(
			@PathParam("connection") String connectionName, 
			@PathParam("catalog") String catalogName, 
			@PathParam("schema") String schemaName, 
			@PathParam("cube") String cubeName, 
			@PathParam("dimension") String dimensionName, 
			@PathParam("hierarchy") String hierarchyName,
			@PathParam("level") String levelName,
      @PathParam("agency") Integer agency)
	{
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getLevelMembers(cube, dimensionName, hierarchyName, levelName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuMember>();
	}
   
	/**
	 * Get root member of that hierarchy.
	 * @return 
	 */
	@GET
	@Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/hierarchies/{hierarchy}/rootmembers/{agency}")
	public List<SaikuMember> getRootMembers(
			@PathParam("connection") String connectionName, 
			@PathParam("catalog") String catalogName, 
			@PathParam("schema") String schemaName, 
			@PathParam("cube") String cubeName, 
			@PathParam("hierarchy") String hierarchyName,
      @PathParam("agency") Integer agency)
		{
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getHierarchyRootMembers(cube, hierarchyName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return null;
	}

/* There was as double // here after hierarchies */	
	@GET
	@Path("/{connection}/{catalog}/{schema}/{cube}/hierarchies/{agency}")
    @Produces({"application/json" })
     public List<SaikuHierarchy> getCubeHierarchies(@PathParam("connection") String connectionName, 
    		 									@PathParam("catalog") String catalogName, 
    		 									@PathParam("schema") String schemaName, 
    		 									@PathParam("cube") String cubeName,
                          @PathParam("agency") Integer agency) {
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getAllHierarchies(cube);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuHierarchy>();
	}
	
/* There was as double // here after measures */	
	@GET
	@Path("/{connection}/{catalog}/{schema}/{cube}/measures/{agency}")
    @Produces({"application/json" })
     public List<SaikuMember> getCubeMeasures(@PathParam("connection") String connectionName, 
    		 									@PathParam("catalog") String catalogName, 
    		 									@PathParam("schema") String schemaName, 
    		 									@PathParam("cube") String cubeName,
                          @PathParam("agency") Integer agency) {

		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getMeasures(cube);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuMember>();
	}
	
	/**
	 * Get all info for given member
	 * @return 
	 */
	@GET
	@Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/member/{member}/{agency}")
	public SaikuMember getMember(
			@PathParam("connection") String connectionName, 
			@PathParam("catalog") String catalogName, 
			@PathParam("schema") String schemaName, 
			@PathParam("cube") String cubeName, 
			@PathParam("member") String memberName,
      @PathParam("agency") Integer agency)

	{
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getMember(cube, memberName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return null;
	}
	
	/**
	 * Get child members of given member
	 * @return 
	 */
	@GET
	@Produces({"application/json" })
	@Path("/{connection}/{catalog}/{schema}/{cube}/member/{member}/children/{agency}")
	public List<SaikuMember> getMemberChildren(
			@PathParam("connection") String connectionName, 
			@PathParam("catalog") String catalogName, 
			@PathParam("schema") String schemaName, 
			@PathParam("cube") String cubeName, 
			@PathParam("member") String memberName,
      @PathParam("agency") Integer agency )
	{
		if ("null".equals(schemaName)) {
			schemaName = "";
		}
		SaikuCube cube = new SaikuCube(connectionName, cubeName,cubeName,cubeName, catalogName, schemaName);
		try {
			return olapDiscoverService.getMemberChildren(cube, memberName);
		} catch (Exception e) {
			log.error(this.getClass().getName(),e);
		}
		return new ArrayList<SaikuMember>();
	}

}
