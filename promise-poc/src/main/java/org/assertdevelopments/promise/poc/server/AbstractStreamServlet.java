/*
 * Copyright 2015 Assert Developments
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.assertdevelopments.promise.poc.server;

import org.apache.log4j.Logger;
import org.assertdevelopments.promise.poc.core.protocol.StreamConstants;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An abstract base class for servlets that do promise stream processing.
 *
 * @author Stefan Bangels
 * @since 2015-01-20
 */
public abstract class AbstractStreamServlet extends HttpServlet {

    private static final String SERVER = "Promise Server/1.0";
    private static final String DOWNLOAD_FILE_NAME = "data.stream";

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    protected final void service(
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // start timer
            long time = System.currentTimeMillis();

            // fetch uri from request
            String uri = getRelativeURI(request);
            String method = request.getMethod();
            logger.info("processing stream request " + method + " " + uri + "...");

            // set request http headers
            response.setHeader("Server", SERVER);
            response.setHeader("Accepts", StreamConstants.CONTENT_TYPE);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // check content type
            String contentType = request.getContentType();
            if (contentType != null && !StreamConstants.CONTENT_TYPE.equals(contentType)) {
                logger.warn("aborting, unsupported content type: " + contentType);
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                return;
            }

            // set response http headers
            response.setHeader("Content-Type", StreamConstants.CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment; filename=" + DOWNLOAD_FILE_NAME);

            // change the http status code to accepted (status code 202)
            // beyond this point, the request has been accepted by the server
            // all exceptions will be reported in the representation body, not as an http error
            // because once we start writing to the http output stream, we can no longer change the http status code
            logger.debug("accepted stream request");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            // handle stream request
            logger.debug("handling stream request...");
            handleRequest(request, response, uri);

            logger.info("processed stream request in " + (System.currentTimeMillis() - time) + "ms.");
        } catch (Throwable t) {
            logger.error("error while processing stream request", t);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the relative URI of a request (this will remove the servlet path from the request URI, and will return
     * the URI relative to the servlet path).
     *
     * @param request the request
     * @return the relative URI
     */
    private String getRelativeURI(HttpServletRequest request) {
        String servletUri = request.getRequestURI().substring(request.getContextPath().length());
        return servletUri.substring(request.getServletPath().length());
    }

    /**
     * Handle a request with the provided stream handler. This method will instantiate a stream, that wraps the request
     * input stream and response output stream and passes it to the provided stream handler. The stream handler will
     * read the request from the stream, process it and write the response back to the stream. After handling the stream,
     * the stream will be finished, and a status code will be sent (success status when the stream is handled
     * successfully, or error status if an exception has occurred while handling the stream).
     *
     * @param request  the request
     * @param response the response
     * @see Stream
     */
    private void handleRequest(
            HttpServletRequest request, HttpServletResponse response, String uri) {
        try {
            Stream stream = createStream(
                    uri, request.getMethod(), request.getInputStream(), response.getOutputStream()
            );
            try {
                handleStreamRequest(stream);
                if (!stream.isCommitted()) {
                    stream.sendSuccess();
                }
            } catch (Throwable t) {
                logger.error("error while handling stream", t);
                if (!stream.isCommitted()) {
                    stream.sendError(StreamStatus.STATUS_ERROR, t.getMessage());
                }
            }
        } catch (Throwable t) {
            logger.error("an unexpected error has occurred", t);
        }
    }

    /**
     * Instantiate a stream for the provided uri and http method, and wrap it around the provided input- and output
     * stream.
     *
     * @param uri          the uri
     * @param method       the http method
     * @param inputStream  the input stream
     * @param outputStream the output stream
     * @return the stream
     */
    private Stream createStream(String uri, String method, InputStream inputStream, OutputStream outputStream) {
        return new StreamImpl(uri, method, inputStream, outputStream);
    }

    /**
     * Process the provided stream request (read the stream input, process it and write the stream output). All
     * exceptions thrown by this handler, will be included in the stream output status.
     *
     * @param stream the stream
     * @see Stream
     */
    protected abstract void handleStreamRequest(Stream stream) throws Throwable;

}
