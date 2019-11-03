/*
 *   File      : LocalJasperDocumentGeneratorService.java
 *   Author    : cmartin
 *   Copyright : Martin Technical Consulting Limited Ltd (2018)
 *   Created   : 21-Jan-2018
 *
 *   History
 *     21-Jan-2018 cmartin The initial version.
 */
package com.willow.document.generator.jasper.adaptor.service.impl;

/**
 *
 * The <tt>LocalJasperDocumentGeneratorService</tt> class provides a local concrete implementation of the
 * JasperDocumentGeneratorService.
 */
public class LocalJasperDocumentGeneratorService extends AbstractJasperDocumentGeneratorService {

  /**
   * Default constructor as dictated by AbstractService.
   */
  public LocalJasperDocumentGeneratorService() {
    super("LocalJasperDocumentGeneratorService");
  }

  /**
   * Default constructor as dictated by AbstractService.
   *
   * @param name the name of the service
   */
  public LocalJasperDocumentGeneratorService(final String name) {
    super(name);
  }
}
