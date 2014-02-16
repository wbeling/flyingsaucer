package org.xhtmlrenderer.service;

public interface HtmlRenderServiceConfigBuilder
{
	/**
	 * The url to render. Content-type returned by the url should
	 * be one of text/plain, text/html, image/png, image/gif or image/jpeg. 
	 * The method allowHttpObjectRead() MUST be used with this method.
	 * The use of method fileObjectUris() is RECOMMENDED.
	 * Default: None.
	 * @since 1.0
	 **/
	HtmlRenderServiceConfigBuilder url();
	
	/**
	 * The local file to render. Content type is determined by extension.
	 * Supported extensions are htm, html, xhtml, jpeg, jpg, gif, png.
	 * The method allowLocalFileRead() MUST be used with this method.
	 * The method baseUrl() is RECOMMENDED.
	 * Default: None.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder file();
	
	/**
	 * An HTML string to render.
	 * Default: None.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder html();
	
	/**
	 * Sets the charset to use if a document does not explicity specify it.
	 * Default: UTF-8.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder defaultCharset();

	/**
	 * Forces the renderer into paged mode. This means the rendered
	 * output will be split at implicit or explicit page breaks.
	 * @since 1.0  
	 */
	HtmlRenderServiceConfigBuilder paged();

	/**
	 * Sets the media type to render. Usually "print" or "screen".
	 * Default: "print" for pdf, "screen" for images.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder media();
	
	/**
	 * Sets the connection timeout for a single http object.
	 * Default: 5 seconds.
	 * @see continueOnDownloadFailure()
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder objectConnectionTimeout();

	/**
	 * Sets the read timeout for a single http object.
	 * @see continueOnDownloadFailure()
	 * Default: 30 seconds.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder objectReadTimeout();

	/**
	 * Sets the service to continue, if possible, when a http object
	 * is not available, times out or is too large.
	 * Default: An exception will be thrown if an object is not available.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder continueOnDownloadFailure();
	
	/**
	 * Sets the timeout for processing. This does not include connecting
	 * and downloading http objects. Upon reaching this timeout the service will
	 * be interrupted and throw an exception.
	 * Default: 5 seconds.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder totalProcessingTimeout();
	
	/**
	 * Sets the total timeout for connecting and downloading http objects.
	 * Default: 120 seconds.
	 * @see continueOnDownloadFailure()
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder totalDownloadTimeout();
	
	/**
	 * Sets the service to be not interruptible. Use
	 * this method with EXTREME caution. If this is used and the service
	 * enters an infinite loop the only way to stop it will be by ending the thread
	 * or process. This method MUST not be used with any of the timeouts.
	 * Default: Interruptible.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder notInterruptible();

	/**
	 * Allows reading of local files including uris starting with file://.
	 * Use this method with EXTREME caution.
	 * Default: Local files can NOT be read.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder allowLocalFileRead();

	/**
	 * Allows reading of http objects.
	 * Use of the method fileObjectUris() is RECOMMENDED.
	 * Default: Http objects can NOT be read.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder allowHttpObjectRead();

	/**
	 * Limits the URIs that can be read with a provided filter.
	 * For example this method can be used to filter uris to one site.
	 * Default: No filter.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder filterObjectUris();

	/**
	 * Sets the dots per inch.
	 * Default: 72 dpi. TODO
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder dpi();

	/**
	 * The limit, in bytes of an image download or file.
	 * Default: 5000000 (5 MiB)
     * @see continueOnDownloadFailure().
 	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder imageSizeLimit();

	/**
	 * The limit, in bytes, of an html resource download or file.
	 * Default: 1000000 (1 MiB). 
     * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder htmlSizeLimit();

	/**
	 * The limit, in bytes of a CSS resource download or file.
	 * Default: 1000000 (1 MiB).
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder cssSizeLimit();
	
	/**
	 * The limit, in bytes of a font resource download or file.
	 * Default: 1000000 (1 MiB).
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder fontSizeLimit();

	/**
	 * The limit in bytes of all resources (html, css, images, fonts).
	 * Default: 10000000 (10MiB).
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder totalSizeLimit();
	
	/**
	 * Sets the number of redirects to allow before failing.
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder allowRedirects();

	/**
	 * Sets a limit on the max image width in pixels.
	 * Default: 4000
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder imageWidthLimit();

	/**
	 * Sets a limit on the max image height in pixels.
	 * Default: 4000
	 * @see continueOnDownloadFailure().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder imageHeightLimit();

	/**
	 * Sets the base url.
	 * Default: None.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder baseUrl();	
	
	/**
	 * Sets the exact width in pixels of an output image.
	 * Use this only when rendering to image(s).
	 * Default: 1000
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder outImageExactWidth();

	/**
	 * Sets the exact height in pixels of an output image.
	 * Use this only when rendering to image(s).
	 * If not using multi image rendering the output will be cut
	 * off at the end of the image.
	 * Default: 1000
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder outImageExactHeight();

	/**
	 * If using a dynamic image size, sets the max width in pixels of an output image.
	 * Use this only when rendering to image(s).
	 * Default: 1200
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder outImageMaxWidth();

	/**
	 * If using a dynamic image size, sets the max height in pixels of an output image.
	 * Use this only when rendering to image(s).
	 * Default: 10000
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder outImageMaxHeight();

	/**
	 * The text breaking locale to use. If rendering characters with
	 * non-latin alphabets this should be set correctly.
	 * Default: Locale.US
	 * @see BreakIterator::getAvailableLocales()
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder textBreakingLocale();

	/**
	 * The locale that specifies the language to output user errors in.
	 * Currently the only translation available is Locale.US.
	 * Default: Locale.US
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder docErrorMessageLocale();

	/**
	 * Provides a String list to which document error messages will be appended.
	 * This is provided so that the application can output these to the user.
	 * Default: None.
	 * @see docErrorMessageLocale().
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder docErrorMessageList();

	/**
	 * If outputting to PDF, sets the page size.
	 * Default: A4.
	 * @since 1.0
	 */
	HtmlRenderServiceConfigBuilder pdfPageSize();
}
