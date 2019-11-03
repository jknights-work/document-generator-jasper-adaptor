/*
 *   File      : AbstractJasperExportHandler.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.handler.impl.export;

import com.willow.common.document.generator.service.DocumentGenerationException;
import com.willow.common.document.generator.support.DocumentGeneratorStringTool;
import com.willow.document.generator.jasper.adaptor.handler.JasperExportHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * The <tt>AbstractJasperExportHandler</tt> class is responsible for default behaviour and exception handling common to
 * all the JasperExportHandler concrete implementations.
 */
public abstract class AbstractJasperExportHandler implements JasperExportHandler {

  /** An instance of the string utility class. */
  private DocumentGeneratorStringTool stringUtil;

  /**
   * Get the string utility.
   *
   * @return an instance of DocumentGeneratorStringUtil
   */
  public final DocumentGeneratorStringTool getStringUtil() {
    return stringUtil;
  }

  /**
   * Set the string utility.
   *
   * @param util an instance of DocumentGeneratorStringUtil
   */
  public final void setStringUtil(final DocumentGeneratorStringTool util) {
    this.stringUtil = util;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final byte[] export(final JasperPrint print) {
    byte[] result = null;
    try {
      result = doExport(print);
    } catch (JRException ex) {
      throwDocumentGenerationException(ex);
    }
    return result;
  }

  /**
   * Throw an exception denoting failure to generate the document.
   *
   * @param throwable the throwable containing the exception details
   */
  protected final void throwDocumentGenerationException(final Throwable throwable) {
    String message = String.format("There was a problem generating the Jasper report: %s", throwable.getMessage());
    throw new DocumentGenerationException(message, throwable);
  }

  /**
   * Execute the export process.
   *
   * @param print the JasperPrint object
   * @return a byte array containing the exported document
   * @throws JRException thrown by Jasper when error is encountered
   */
  abstract byte[] doExport(final JasperPrint print) throws JRException;
}
