/*
 *   File      : JasperPDFExportHandler.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.handler.impl.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * The <tt>JasperHTMLExportHandler</tt> class is responsible for exporting a Jasper report as a PDF.
 */
public final class JasperPDFExportHandler extends AbstractJasperExportHandler {

  /**
   * {@inheritDoc}
   */
  @Override
  protected byte[] doExport(final JasperPrint print) throws JRException {
    return JasperExportManager.exportReportToPdf(print);
  }
}
