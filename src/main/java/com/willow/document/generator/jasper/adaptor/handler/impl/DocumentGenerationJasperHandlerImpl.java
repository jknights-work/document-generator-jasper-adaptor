/*
 *   File      : DocumentGenerationJasperHandlerImpl.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018  cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.handler.impl;

import com.willow.common.document.generator.service.DocumentGenerationHandler;
import com.willow.common.service.ServiceException;
import com.willow.document.generator.jasper.adaptor.service.JasperDocumentGeneratorService;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * The <tt>DocumentGenerationJasperHandlerImpl</tt> class provides a handler for the DocumentGeneratorService to
 * delegate the generation process to the JasperDocumentGenerationService.
 */
public final class DocumentGenerationJasperHandlerImpl implements DocumentGenerationHandler {

  /**
   * * The class logger.
   */
  private static final Logger LOG = Logger.getLogger(DocumentGenerationJasperHandlerImpl.class);

  /**
   * The Jasper document generation service.
   */
  private JasperDocumentGeneratorService service;
  /**
   * The key used to store the template location in the Map of attributes.
   */
  private String templateLocationAttributeKey;

  /**
   * The key used to store the output format in the Map of input data.
   */
  private String ouputFormatDataKey;

  /**
   * The map of templates known by the handler.
   */
  private Map<String, String> templates;

  /**
   * Creates a new instance of DocumentGenerationJasperHandlerImpl
   */
  public DocumentGenerationJasperHandlerImpl() {
    templates = new HashMap<>();
  }

  /**
   * Get the templates map.
   *
   * @return a Map
   */
  public Map<String, String> getTemplates() {
    return templates;
  }

  /**
   * Set the templates map.
   *
   * @param templates the templates map
   */
  public void setTemplates(Map<String, String> templates) {
    this.templates = templates;
  }

  /**
   * Get the key used to store the template location in the Map of attributes.
   *
   * @return a String or null if not set
   */
  public String getTemplateLocationAttributeKey() {
    return templateLocationAttributeKey;
  }

  /**
   * Set the key used to store the template location in the Map of attributes.
   *
   * @param key string representation of the configuration parameter
   */
  public void setTemplateLocationAttributeKey(final String key) {
    this.templateLocationAttributeKey = key;
  }

  /**
   * Get the key used to store the output format in the data Map.
   *
   * @return a String or null if not set
   */
  public String getOuputFormatDataKey() {
    return ouputFormatDataKey;
  }

  /**
   * Set the key used to store the template location in the Map of attributes.
   *
   * @param key string representation of the configuration parameter
   */
  public void setOuputFormatDataKey(final String key) {
    ouputFormatDataKey = key;
  }

  /**
   * Get the Jasper generation service.
   *
   * @return an instance of JasperDocumentGeneratorService or null if not set
   */
  public JasperDocumentGeneratorService getService() {
    if (!service.isRunning()) {
      try {
        service.start();
      } catch (ServiceException ex) {
        LOG.error("Jasper Service could not be started!", ex);
      }
    }
    return service;
  }

  /**
   * Set the Jasper generation service.
   *
   * @param newService an instance of JasperDocumentGeneratorService
   */
  public void setService(final JasperDocumentGeneratorService newService) {
    service = newService;
  }

  /**
   * Get the template location from the Map of attributes.
   *
   * @param attributes the attributes to query
   *
   * @return the configuration value, or null if not set
   */
  private String getTemplateName(final Map<String, String> attributes) {
    String result = "";
    if (attributes != null) {
      String businessUnitId = attributes.get("businessUnitId");
      String documentId = attributes.get("documentId");
      String key = businessUnitId + '#' + documentId;
      result = templates.get(key);
    }
    if (result == null || result.isEmpty()) {
      throw new IllegalArgumentException(
              "There was no template location configured in the document definition attributes");
    }
    return result;
  }

  /**
   * Get the output format from the Map of input data.
   *
   * @param data the input data to query
   *
   * @return the configuration value, or null if not set
   */
  private String getOutputFormat(final Map<String, ?> data) {
    String result = "";
    if (data != null) {
      Object dataObject = data.get(getOuputFormatDataKey());
      if (dataObject instanceof String) {
        result = (String) dataObject;
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] generate(final Map<String, String> generateAttributes, final Map<String, Object> data,
                         final String sessionId) {
    String templateLocation = getTemplateName(generateAttributes);
    String outputFormat = getOutputFormat(data);
    // If the output format is not in the data map, check for the default in the attributes.
    if (outputFormat.isEmpty()) {
      outputFormat = getOutputFormat(generateAttributes);
    }
    // If no default is set, throw an exception as we cannot export the document.
    if (outputFormat.isEmpty()) {
      throw new IllegalArgumentException("There was no output format configured in the data");
    }
    return getService().generate(templateLocation, outputFormat, data);
  }
}
