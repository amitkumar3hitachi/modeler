/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2014 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */


package org.pentaho.agilebi.modeler.models.annotations;

import org.junit.Test;
import org.pentaho.agilebi.modeler.ModelerException;
import org.w3c.dom.Document;

import static org.junit.Assert.*;
import static org.pentaho.agilebi.modeler.models.annotations.AnnotationUtil.*;

public class HideMeasureTest {
  private static final String MONDRIAN_TEST_FILE_PATH = "test-res/hideshow.mondrian.xml";

  @Test
  public void testSummary() throws Exception {
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "Price" );
    hideMeasure.setCube( "Sales" );
    assertEquals( "Hide measure Price in cube Sales", hideMeasure.getSummary() );
  }

  @Test
  public void testHidesMeasureWithNoVisibilitySpecified() throws Exception {
    Document mondrianDoc = getMondrianDoc( MONDRIAN_TEST_FILE_PATH );
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "Price" );
    hideMeasure.setCube( "products" );
    assertTrue( hideMeasure.apply( mondrianDoc ) );
    assertTrue( validateNodeAttribute( mondrianDoc, MEASURE_ELEMENT_NAME, "Price", "visible", "false" ) );
  }

  @Test
  public void testHidesMeasureWithExplicitVisible() throws Exception {
    Document mondrianDoc = getMondrianDoc( MONDRIAN_TEST_FILE_PATH );
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "visibleMeasure" );
    hideMeasure.setCube( "products" );
    assertTrue( hideMeasure.apply( mondrianDoc ) );
    assertTrue( validateNodeAttribute( mondrianDoc, MEASURE_ELEMENT_NAME, "visibleMeasure", "visible", "false" ) );
  }

  @Test
  public void testHiddenMeasureStaysHidden() throws Exception {
    Document mondrianDoc = getMondrianDoc( MONDRIAN_TEST_FILE_PATH );
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "hiddenMeasure" );
    hideMeasure.setCube( "products" );
    assertTrue( hideMeasure.apply( mondrianDoc ) );
    assertTrue( validateNodeAttribute( mondrianDoc, MEASURE_ELEMENT_NAME, "hiddenMeasure", "visible", "false" ) );
  }

  @Test
  public void testHideMeasureNotFoundIsFalse() throws Exception {
    Document mondrianDoc = getMondrianDoc( MONDRIAN_TEST_FILE_PATH );
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "measureNotFound" );
    hideMeasure.setCube( "products" );
    assertFalse( hideMeasure.apply( mondrianDoc ) );
    assertFalse( validateNodeAttribute( mondrianDoc, MEASURE_ELEMENT_NAME, "measureNotFound", "visible", "false" ) );
  }

  @Test
  public void testCubeNameIsRequired() throws Exception {
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setName( "aMeasure" );
    try {
      hideMeasure.validate();
      fail( "should have got exception" );
    } catch ( ModelerException e ) {
      assertEquals( "Cube Name is required", e.getMessage() );
    }
  }

  @Test
  public void testMeasureNameIsRequired() throws Exception {
    HideMeasure hideMeasure = new HideMeasure();
    hideMeasure.setCube( "aCube" );
    try {
      hideMeasure.validate();
      fail( "should have got exception" );
    } catch ( ModelerException e ) {
      assertEquals( "Measure Name is required", e.getMessage() );
    }
  }
}