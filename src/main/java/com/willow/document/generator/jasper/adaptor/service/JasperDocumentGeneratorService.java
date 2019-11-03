/*
 *   File      : JasperDocumentGeneratorService.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.service;

import com.willow.common.service.Service;
import java.util.Map;

/**
 *
 * The <tt>JasperDocumentGeneratorService</tt> class describes the behaviour of a document generator service that uses
 * Jasper.
 */
public interface JasperDocumentGeneratorService extends Service {

  /**
   * Generate a document.
   *
   * @param templatePath the path to the document template
   * @param exporter the name of the export handler
   * @param data the template data
   * @return the generated content
   */
  byte[] generate(String templatePath, String exporter, Map<String, Object> data);
}
