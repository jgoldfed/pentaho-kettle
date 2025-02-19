/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.di.repository.filerep;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.repository.RepositoriesMeta;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by bmorrise on 4/26/16.
 */
public class KettleFileRepositoryMetaTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  public static final String NAME = "Name";
  public static final String DESCRIPTION = "Description";
  public static final String THIS_IS_THE_PATH = "/this/is/the/path";
  public static final String JSON_OUTPUT = "{\"isDefault\":true,\"displayName\":\"Name\",\"showHiddenFolders\":true,"
    + "\"description\":\"Description\",\"location\":\"\\/this\\/is\\/the\\/path\",\"id\":\"KettleFileRepository\","
    + "\"doNotModify\":true}";

  private RepositoriesMeta repositoriesMeta = mock( RepositoriesMeta.class );

  KettleFileRepositoryMeta kettleFileRepositoryMeta;

  @Before
  public void setup() {
    kettleFileRepositoryMeta = new KettleFileRepositoryMeta();
  }

  @Test
  public void testPopulate() throws Exception {
    Map<String, Object> properties = new HashMap<>();
    properties.put( "displayName", NAME );
    properties.put( "showHiddenFolders", true );
    properties.put( "description", DESCRIPTION );
    properties.put( "location", THIS_IS_THE_PATH );
    properties.put( "doNotModify", true );
    properties.put( "isDefault", true );

    kettleFileRepositoryMeta.populate( properties, repositoriesMeta );

    assertEquals( NAME, kettleFileRepositoryMeta.getName() );
    assertEquals( true, kettleFileRepositoryMeta.isHidingHiddenFiles() );
    assertEquals( DESCRIPTION, kettleFileRepositoryMeta.getDescription() );
    assertEquals( THIS_IS_THE_PATH, kettleFileRepositoryMeta.getBaseDirectory() );
    assertEquals( true, kettleFileRepositoryMeta.isReadOnly() );
    assertEquals( true, kettleFileRepositoryMeta.isDefault() );
  }

  @Test
  public void testToJSONString() {
    kettleFileRepositoryMeta.setName( NAME );
    kettleFileRepositoryMeta.setHidingHiddenFiles( true );
    kettleFileRepositoryMeta.setDescription( DESCRIPTION );
    kettleFileRepositoryMeta.setBaseDirectory( THIS_IS_THE_PATH );
    kettleFileRepositoryMeta.setReadOnly( true );
    kettleFileRepositoryMeta.setDefault( true );

    JSONObject json = kettleFileRepositoryMeta.toJSONObject();

    assertEquals( JSON_OUTPUT, json.toString() );
  }

}
