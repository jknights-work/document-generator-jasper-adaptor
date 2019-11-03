/*
 *   File      : AbstractJasperDocumentGeneratorService.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018  cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.service.impl;

import com.willow.common.document.generator.service.DocumentGenerationException;
import com.willow.common.document.generator.support.DocumentGeneratorStringTool;
import com.willow.common.service.impl.AbstractService;
import com.willow.document.generator.jasper.adaptor.handler.JasperExportHandler;
import com.willow.document.generator.jasper.adaptor.service.JasperDocumentGeneratorService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.log4j.Logger;

/**
 *
 * The <tt>AbstractJasperDocumentGeneratorService</tt> class provides a default implementation of a Jasper driven
 * document generation service.
 */
public abstract class AbstractJasperDocumentGeneratorService extends AbstractService
                                                             implements JasperDocumentGeneratorService {

  /**
   * The class logger.
   */
  private static final Logger LOG = Logger.getLogger(AbstractJasperDocumentGeneratorService.class);

  /**
   * An instance of the string utility class.
   */
  private DocumentGeneratorStringTool stringUtil;
  /**
   * The handlers for exporting the JasperPrint output to a byte array.
   */
  private Map<String, JasperExportHandler> exportHandlers;
  /**
   * The base template path *
   */
  private String basePath;

  /**
   * The parameterised constructor as dictated by AbstractService.
   *
   * @param serviceName the name of the service
   */
  public AbstractJasperDocumentGeneratorService(final String serviceName) {
    super(serviceName);
    exportHandlers = new HashMap<String, JasperExportHandler>();
  }

  /**
   * The default constructor as dictated by AbstractService.
   */
  public AbstractJasperDocumentGeneratorService() {
    super("JasperDocumentGeneratorService");
    exportHandlers = new HashMap<String, JasperExportHandler>();
  }

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
   * Get the handlers registered to export the Jasper report to a byte array.
   *
   * @return a Map of key versus handler
   */
  public final Map<String, JasperExportHandler> getExportHandlers() {
    return exportHandlers;
  }

  /**
   * Set the handlers registered to export the Jasper report to a byte array.
   *
   * @param handlers a Map of key versus handler
   */
  public final void setExportHandlers(final Map<String, JasperExportHandler> handlers) {
    exportHandlers = handlers;
  }

  /**
   * Get the templates base path.
   *
   * @return a String
   */
  public String getBasePath() {
    return basePath;
  }

  /**
   * Set the templates base path.
   *
   * @param newBasePath the base path or null if not set
   */
  public void setBasePath(String newBasePath) {
    basePath = newBasePath;
  }

  /**
   * Are we able to export the Jasper report?
   *
   * @return true if handlers are configured correctly, otherwise false.
   */
  private boolean isAbleToExport() {
    return getExportHandlers() != null && getExportHandlers().size() > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean doStart() {
    boolean result = isAbleToExport();
    if (!result) {
      throwNoExportHandlersDefinedException();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean doStop() {
    return true;
  }

  
  private JasperReport createJasperReportFromTemplate(final byte[] template) {
    JasperReport result = null;
    try {
      InputStream inStream = new ByteArrayInputStream(template);
      JasperDesign design = JRXmlLoader.load(inStream);
      result = JasperCompileManager.compileReport(design);
      result.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
      result.setWhenResourceMissingType(WhenResourceMissingTypeEnum.KEY);
    } catch (JRException ex) {
      throwCannotCreateJasperReportException(ex);
    }
    return result;
  }

  /**
   * Produce a map of parameters to send to the Jasper report.
   *
   * @param data the report data to render
   * @return a Map of String vs Object
   */
  private Map<String, Object> buildParameterMap(final Map<String, Object> data) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put(JRParameter.REPORT_LOCALE, Locale.getDefault());
    result.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getDefault());
    result.putAll(data);
    return result;
  }

  /**
   * Create a JasperFillManager instance.
   *
   * @return a JasperFillManager instance
   */
  private JasperFillManager getJasperFillManager() {
    SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();
    jasperReportsContext.setExtensions(ParameterContributorFactory.class,
            Collections.singletonList(DataAdapterParameterContributorFactory.getInstance()));
    return JasperFillManager.getInstance(jasperReportsContext);
  }

  private byte[] getTemplate(final String templatePath) {
    // Consider refactoring to use DocumentStorage service instead
    byte[] result = null;
    File basePathFile = new File(basePath);
    File file = new File(basePathFile, templatePath);
    if (file.isFile()) {
      try {
        result = Files.readAllBytes(file.toPath());
      } catch (IOException ex) {
        throwReadErrorException(file.getPath(), ex);
      }
    } else {
      throwFileNotReadableException(templatePath);
    }
    return result;
  }

  /**
   * Get the named export handler or raise an exception if there is no matching handler.
   *
   * @param handlerName the name of the handler to get
   * @return a JasperExportHandler instance
   */
  private JasperExportHandler getExportHandler(final String handlerName) {
    JasperExportHandler result = getExportHandlers().get(handlerName);
    if (result == null) {
      throwNoMatchingExportHandlerException();
    }
    return result;
  }

  /**
   * Generate the report using the Jasper libraries.
   *
   * @param report the JasperReport to generate
   * @param exporter the handler responsible for exporting the content
   * @param parameters the parameters to pass to the fill process
   * @return a byte array containing the rendered report
   */
  private byte[] generateReport(final JasperReport report, final JasperExportHandler exporter,
          final Map<String, Object> parameters) {
    byte[] result = null;
    try {
      JasperPrint print = getJasperFillManager().fill(report, parameters);
      result = exporter.export(print);
    } catch (JRException ex) {
      throwDocumentGenerationException(ex);
    }
    return result;
  }
  
  
  

  /**
   * {@inheritDoc}
   */
  @Override
  public final byte[] generate(final String templatePath, final String exporter, final Map<String, Object> data) {
    if (LOG.isInfoEnabled()) {
      LOG.info("Generating template " + templatePath + " with exporter " + exporter);
    }
    byte[] result = null;
    if (isRunning()) {
      JasperReport report = createJasperReportFromTemplate(getTemplate(templatePath));
      JasperExportHandler exportHandler = getExportHandler(exporter);
      Map<String, Object> parameters = buildParameterMap(data);
      result = generateReport(report, exportHandler, parameters);
    } else {
      throwServiceNotRunningException();
    }
    return result;
  }

  /**
   * Throw an exception when the service is not running.
   */
  private void throwServiceNotRunningException() {
    throw new IllegalStateException("The service is not running");
  }

  /**
   * Throw an exception when the service has no export handlers configured.
   */
  private void throwNoExportHandlersDefinedException() {
    throw new IllegalStateException("No export handlers have been configured");
  }

  /**
   * Throw an exception when the service has no export handlers configured.
   */
  private void throwNoMatchingExportHandlerException() {
    throw new IllegalArgumentException("No matching export handler defined");
  }

  /**
   * Throw an exception when we can't create a JasperReport object.
   *
   * @param ex the original exception
   */
  private void throwCannotCreateJasperReportException(final Exception ex) {
    throw new DocumentGenerationException("Cannot create JasperReport object", ex);
  }

  /**
   * Throw an exception when there is an error generating the document content.
   *
   * @param ex the original exception
   */
  private void throwDocumentGenerationException(final Exception ex) {
    throw new DocumentGenerationException("Cannot create report output", ex);
  }

  /**
   * Throw an exception describing a failure to read the file.
   *
   * @param filename the filename that could not be read
   */
  private void throwFileNotReadableException(final String filename) {
    String msg = String.format("An error occurred when attempting to read the file %s", filename);
    throw new IllegalArgumentException(msg);
  }

  /**
   * Throw an exception describing failure to read a file.
   *
   * @param filePath the file in question
   * @param ex the exception raised by File
   */
  private void throwReadErrorException(final String filePath, final Exception ex) {
    String message = String.format("An error occurred when reading file %s", filePath);
    throw new DocumentGenerationException(message, ex);
  }
}
