/*
 *   File      : JasperXMLExportHandler.java
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
import org.apache.log4j.Logger;

/**
 *
 * The <tt>JasperHTMLExportHandler</tt> class is responsible for exporting a Jasper report as XML.
 */
public final class JasperXMLExportHandler extends AbstractJasperExportHandler {

  /**
   * The class logger.
   */
  private static final Logger LOG = Logger.getLogger(JasperXMLExportHandler.class);

  /**
   * {@inheritDoc}
   */
  @Override
  protected byte[] doExport(final JasperPrint print) throws JRException {
    byte[] result = null;
    String outString = JasperExportManager.exportReportToXml(print);
    result = getStringUtil().convertStringToBytes(outString);
    return result;
  }
}
