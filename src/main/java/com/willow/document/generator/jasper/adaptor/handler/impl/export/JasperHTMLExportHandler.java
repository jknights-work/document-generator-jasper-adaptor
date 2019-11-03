/*
 *   File      : JasperHTMLExportHandler.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.handler.impl.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.apache.log4j.Logger;

/**
 *
 * The <tt>JasperHTMLExportHandler</tt> class is responsible for exporting a Jasper report as HTML.
 */
public final class JasperHTMLExportHandler extends AbstractJasperExportHandler {

  /**
   * The class logger.
   */
  private static final Logger LOG = Logger.getLogger(JasperHTMLExportHandler.class);

  /**
   * {@inheritDoc}
   */
  @Override
  protected byte[] doExport(final JasperPrint print) throws JRException {
    byte[] result = null;
    HtmlExporter exporter = new HtmlExporter();
    StringBuffer stringBuffer = new StringBuffer();
    ExporterInput exporterInput = new SimpleExporterInput(print);
    HtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(stringBuffer);
    exporter.setExporterInput(exporterInput);
    exporter.setExporterOutput(exporterOutput);
    exporter.exportReport();
    String outString = stringBuffer.toString();
    result = getStringUtil().convertStringToBytes(outString);
    return result;
  }
}
