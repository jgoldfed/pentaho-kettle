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


package org.pentaho.di.core.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.Const;

import java.text.MessageFormat;

/**
 * @author Tatsiana_Kasiankova
 *
 */
public class LogMessageTest {
  private LogMessage logMessage;

  private static final String LOG_MESSAGE = "Test Message";
  private static final LogLevel LOG_LEVEL = LogLevel.BASIC;
  private static String treeLogChannelId;
  private static String simpleLogChannelId;

  @Before
  public void setUp() {
    treeLogChannelId = LoggingRegistry.getInstance().registerLoggingSource( getTreeLoggingObject() );
  }

  @After
  public void tearDown() {
    LoggingRegistry.getInstance().removeIncludingChildren( treeLogChannelId );
    System.clearProperty( Const.KETTLE_LOG_MARK_MAPPINGS );
  }

  @Test
  public void testWhenLogMarkMappingTurnOn_DetailedSubjectUsed() throws Exception {
    turnOnLogMarkMapping();

    logMessage = new LogMessage( LOG_MESSAGE, treeLogChannelId, LOG_LEVEL );
    assertTrue( LOG_MESSAGE.equals( logMessage.getMessage() ) );
    assertTrue( LOG_LEVEL.equals( logMessage.getLevel() ) );
    assertTrue( treeLogChannelId.equals( logMessage.getLogChannelId() ) );
    assertTrue( "[TRANS_SUBJECT].[STEP_SUBJECT].TRANS_CHILD_SUBJECT".equals( logMessage.getSubject() ) );
  }

  @Test
  public void testWhenLogMarkMappingTurnOff_SimpleSubjectUsed() throws Exception {
    turnOffLogMarkMapping();

    logMessage = new LogMessage( LOG_MESSAGE, treeLogChannelId, LOG_LEVEL );
    assertTrue( LOG_MESSAGE.equals( logMessage.getMessage() ) );
    assertTrue( LOG_LEVEL.equals( logMessage.getLevel() ) );
    assertTrue( treeLogChannelId.equals( logMessage.getLogChannelId() ) );
    assertTrue( "TRANS_CHILD_SUBJECT".equals( logMessage.getSubject() ) );
  }

  @Test
  public void testWhenLogMarkMappingTurnOnAndNoSubMappingUsed_DetailedSubjectContainsOnlySimpleSubject() throws Exception {
    turnOnLogMarkMapping();

    simpleLogChannelId = LoggingRegistry.getInstance().registerLoggingSource( getLoggingObjectWithOneParent() );

    logMessage = new LogMessage( LOG_MESSAGE, simpleLogChannelId, LOG_LEVEL );
    assertTrue( LOG_MESSAGE.equals( logMessage.getMessage() ) );
    assertTrue( LOG_LEVEL.equals( logMessage.getLevel() ) );
    assertTrue( simpleLogChannelId.equals( logMessage.getLogChannelId() ) );
    assertTrue( "TRANS_SUBJECT".equals( logMessage.getSubject() ) );

    LoggingRegistry.getInstance().removeIncludingChildren( simpleLogChannelId );
  }

  @Test
  public void testToString() throws Exception {
    LogMessage msg = new LogMessage( "Log message",
        "Channel 01",
        LogLevel.DEBUG );
    msg.setSubject( "Simple" );

    assertEquals( "Simple - Log message", msg.toString( ) );
  }

  @Test
  public void testToString_withOneArgument() throws Exception {
    LogMessage msg = new LogMessage( "Log message for {0}",
        "Channel 01",
        new String[]{"Test"},
        LogLevel.DEBUG );
    msg.setSubject( "Subject" );

    assertEquals( "Subject - Log message for Test", msg.toString( ) );
  }

  @Test
  public void testGetMessage() {
    LogMessage msg = new LogMessage( "m {0}, {1}, {2}, {3}, {4,number,#.00}, {5} {foe}", "Channel 01",
      new Object[] { "Foo", "{abc}", "", null, 123 }, LogLevel.DEBUG );
    assertEquals( "m Foo, {abc}, , null, 123.00, {5} {foe}", msg.getMessage() );
  }

  private void turnOnLogMarkMapping() {
    System.getProperties().put( Const.KETTLE_LOG_MARK_MAPPINGS, "Y" );
  }

  private void turnOffLogMarkMapping() {
    System.getProperties().put( Const.KETTLE_LOG_MARK_MAPPINGS, "N" );
  }

  private static LoggingObjectInterface getTreeLoggingObject() {
    LoggingObjectInterface rootLogObject = new SimpleLoggingObject( "ROOT_SUBJECT", LoggingObjectType.SPOON, null );
    LoggingObjectInterface transLogObject =
        new SimpleLoggingObject( "TRANS_SUBJECT", LoggingObjectType.TRANS, rootLogObject );
    LoggingObjectInterface stepLogObject =
        new SimpleLoggingObject( "STEP_SUBJECT", LoggingObjectType.STEP, transLogObject );
    LoggingObjectInterface transChildLogObject =
        new SimpleLoggingObject( "TRANS_CHILD_SUBJECT", LoggingObjectType.TRANS, stepLogObject );
    return transChildLogObject;
  }

  private static LoggingObjectInterface getLoggingObjectWithOneParent() {
    LoggingObjectInterface rootLogObject = new SimpleLoggingObject( "ROOT_SUBJECT", LoggingObjectType.SPOON, null );
    LoggingObjectInterface transLogObject =
      new SimpleLoggingObject( "TRANS_SUBJECT", LoggingObjectType.TRANS, rootLogObject );
    return transLogObject;
  }
}
