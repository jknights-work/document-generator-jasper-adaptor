/*
 *   File      : JasperExportHandler.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.handler;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * The <tt>JasperExportHandler</tt> interface describes the behaviour of a class that handles the export of Jasper
 * output.
 *
 */
public interface JasperExportHandler {

  /**
   * Export the jasper print report to a byte array.
   *
   * @param print the JasperPrint object
   * @return a byte array containing the report
   */
  byte[] export(JasperPrint print);
}
