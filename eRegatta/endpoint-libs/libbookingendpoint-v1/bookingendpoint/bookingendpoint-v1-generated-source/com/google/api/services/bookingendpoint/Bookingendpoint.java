/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-06-05 16:09:48 UTC)
 * on 2013-06-18 at 20:48:48 UTC 
 * Modify at your own risk.
 */

package com.google.api.services.bookingendpoint;

/**
 * Service definition for Bookingendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link BookingendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Bookingendpoint extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION == 14,
        "You are currently running with version %s of google-api-client. " +
        "You need version 1.14 of google-api-client to run version " +
        "1.14.2-beta of the  library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://eregatta.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "bookingendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Bookingendpoint(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Bookingendpoint(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the BookingEndpoint collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Bookingendpoint bookingendpoint = new Bookingendpoint(...);}
   *   {@code Bookingendpoint.BookingEndpoint.List request = bookingendpoint.bookingEndpoint().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public BookingEndpoint bookingEndpoint() {
    return new BookingEndpoint();
  }

  /**
   * The "bookingEndpoint" collection of methods.
   */
  public class BookingEndpoint {

    /**
     * Create a request for the method "bookingEndpoint.downloadBookings".
     *
     * This request holds the parameters needed by the the bookingendpoint server.  After setting any
     * optional parameters, call the {@link DownloadBookings#execute()} method to invoke the remote
     * operation.
     *
     * @return the request
     */
    public DownloadBookings downloadBookings() throws java.io.IOException {
      DownloadBookings result = new DownloadBookings();
      initialize(result);
      return result;
    }

    public class DownloadBookings extends BookingendpointRequest<com.google.api.services.bookingendpoint.model.BookingCollection> {

      private static final String REST_PATH = "downloadBookings";

      /**
       * Create a request for the method "bookingEndpoint.downloadBookings".
       *
       * This request holds the parameters needed by the the bookingendpoint server.  After setting any
       * optional parameters, call the {@link DownloadBookings#execute()} method to invoke the remote
       * operation. <p> {@link DownloadBookings#initialize(com.google.api.client.googleapis.services.Abs
       * tractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @since 1.13
       */
      protected DownloadBookings() {
        super(Bookingendpoint.this, "GET", REST_PATH, null, com.google.api.services.bookingendpoint.model.BookingCollection.class);
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public DownloadBookings setAlt(java.lang.String alt) {
        return (DownloadBookings) super.setAlt(alt);
      }

      @Override
      public DownloadBookings setFields(java.lang.String fields) {
        return (DownloadBookings) super.setFields(fields);
      }

      @Override
      public DownloadBookings setKey(java.lang.String key) {
        return (DownloadBookings) super.setKey(key);
      }

      @Override
      public DownloadBookings setOauthToken(java.lang.String oauthToken) {
        return (DownloadBookings) super.setOauthToken(oauthToken);
      }

      @Override
      public DownloadBookings setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (DownloadBookings) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public DownloadBookings setQuotaUser(java.lang.String quotaUser) {
        return (DownloadBookings) super.setQuotaUser(quotaUser);
      }

      @Override
      public DownloadBookings setUserIp(java.lang.String userIp) {
        return (DownloadBookings) super.setUserIp(userIp);
      }

      @Override
      public DownloadBookings set(String parameterName, Object value) {
        return (DownloadBookings) super.set(parameterName, value);
      }
    }

  }

  /**
   * Create a request for the method "getBooking".
   *
   * This request holds the parameters needed by the the bookingendpoint server.  After setting any
   * optional parameters, call the {@link GetBooking#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetBooking getBooking(java.lang.Long id) throws java.io.IOException {
    GetBooking result = new GetBooking(id);
    initialize(result);
    return result;
  }

  public class GetBooking extends BookingendpointRequest<com.google.api.services.bookingendpoint.model.Booking> {

    private static final String REST_PATH = "booking/{id}";

    /**
     * Create a request for the method "getBooking".
     *
     * This request holds the parameters needed by the the bookingendpoint server.  After setting any
     * optional parameters, call the {@link GetBooking#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetBooking#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetBooking(java.lang.Long id) {
      super(Bookingendpoint.this, "GET", REST_PATH, null, com.google.api.services.bookingendpoint.model.Booking.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetBooking setAlt(java.lang.String alt) {
      return (GetBooking) super.setAlt(alt);
    }

    @Override
    public GetBooking setFields(java.lang.String fields) {
      return (GetBooking) super.setFields(fields);
    }

    @Override
    public GetBooking setKey(java.lang.String key) {
      return (GetBooking) super.setKey(key);
    }

    @Override
    public GetBooking setOauthToken(java.lang.String oauthToken) {
      return (GetBooking) super.setOauthToken(oauthToken);
    }

    @Override
    public GetBooking setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetBooking) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetBooking setQuotaUser(java.lang.String quotaUser) {
      return (GetBooking) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetBooking setUserIp(java.lang.String userIp) {
      return (GetBooking) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetBooking setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetBooking set(String parameterName, Object value) {
      return (GetBooking) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertBooking".
   *
   * This request holds the parameters needed by the the bookingendpoint server.  After setting any
   * optional parameters, call the {@link InsertBooking#execute()} method to invoke the remote
   * operation.
   *
   * @param content the {@link com.google.api.services.bookingendpoint.model.Booking}
   * @return the request
   */
  public InsertBooking insertBooking(com.google.api.services.bookingendpoint.model.Booking content) throws java.io.IOException {
    InsertBooking result = new InsertBooking(content);
    initialize(result);
    return result;
  }

  public class InsertBooking extends BookingendpointRequest<com.google.api.services.bookingendpoint.model.Booking> {

    private static final String REST_PATH = "booking";

    /**
     * Create a request for the method "insertBooking".
     *
     * This request holds the parameters needed by the the bookingendpoint server.  After setting any
     * optional parameters, call the {@link InsertBooking#execute()} method to invoke the remote
     * operation. <p> {@link InsertBooking#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link com.google.api.services.bookingendpoint.model.Booking}
     * @since 1.13
     */
    protected InsertBooking(com.google.api.services.bookingendpoint.model.Booking content) {
      super(Bookingendpoint.this, "POST", REST_PATH, content, com.google.api.services.bookingendpoint.model.Booking.class);
    }

    @Override
    public InsertBooking setAlt(java.lang.String alt) {
      return (InsertBooking) super.setAlt(alt);
    }

    @Override
    public InsertBooking setFields(java.lang.String fields) {
      return (InsertBooking) super.setFields(fields);
    }

    @Override
    public InsertBooking setKey(java.lang.String key) {
      return (InsertBooking) super.setKey(key);
    }

    @Override
    public InsertBooking setOauthToken(java.lang.String oauthToken) {
      return (InsertBooking) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertBooking setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertBooking) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertBooking setQuotaUser(java.lang.String quotaUser) {
      return (InsertBooking) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertBooking setUserIp(java.lang.String userIp) {
      return (InsertBooking) super.setUserIp(userIp);
    }

    @Override
    public InsertBooking set(String parameterName, Object value) {
      return (InsertBooking) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeBooking".
   *
   * This request holds the parameters needed by the the bookingendpoint server.  After setting any
   * optional parameters, call the {@link RemoveBooking#execute()} method to invoke the remote
   * operation.
   *
   * @param id
   * @return the request
   */
  public RemoveBooking removeBooking(java.lang.Long id) throws java.io.IOException {
    RemoveBooking result = new RemoveBooking(id);
    initialize(result);
    return result;
  }

  public class RemoveBooking extends BookingendpointRequest<com.google.api.services.bookingendpoint.model.Booking> {

    private static final String REST_PATH = "booking/{id}";

    /**
     * Create a request for the method "removeBooking".
     *
     * This request holds the parameters needed by the the bookingendpoint server.  After setting any
     * optional parameters, call the {@link RemoveBooking#execute()} method to invoke the remote
     * operation. <p> {@link RemoveBooking#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveBooking(java.lang.Long id) {
      super(Bookingendpoint.this, "DELETE", REST_PATH, null, com.google.api.services.bookingendpoint.model.Booking.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveBooking setAlt(java.lang.String alt) {
      return (RemoveBooking) super.setAlt(alt);
    }

    @Override
    public RemoveBooking setFields(java.lang.String fields) {
      return (RemoveBooking) super.setFields(fields);
    }

    @Override
    public RemoveBooking setKey(java.lang.String key) {
      return (RemoveBooking) super.setKey(key);
    }

    @Override
    public RemoveBooking setOauthToken(java.lang.String oauthToken) {
      return (RemoveBooking) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveBooking setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveBooking) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveBooking setQuotaUser(java.lang.String quotaUser) {
      return (RemoveBooking) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveBooking setUserIp(java.lang.String userIp) {
      return (RemoveBooking) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public RemoveBooking setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public RemoveBooking set(String parameterName, Object value) {
      return (RemoveBooking) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateBooking".
   *
   * This request holds the parameters needed by the the bookingendpoint server.  After setting any
   * optional parameters, call the {@link UpdateBooking#execute()} method to invoke the remote
   * operation.
   *
   * @param content the {@link com.google.api.services.bookingendpoint.model.Booking}
   * @return the request
   */
  public UpdateBooking updateBooking(com.google.api.services.bookingendpoint.model.Booking content) throws java.io.IOException {
    UpdateBooking result = new UpdateBooking(content);
    initialize(result);
    return result;
  }

  public class UpdateBooking extends BookingendpointRequest<com.google.api.services.bookingendpoint.model.Booking> {

    private static final String REST_PATH = "booking";

    /**
     * Create a request for the method "updateBooking".
     *
     * This request holds the parameters needed by the the bookingendpoint server.  After setting any
     * optional parameters, call the {@link UpdateBooking#execute()} method to invoke the remote
     * operation. <p> {@link UpdateBooking#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link com.google.api.services.bookingendpoint.model.Booking}
     * @since 1.13
     */
    protected UpdateBooking(com.google.api.services.bookingendpoint.model.Booking content) {
      super(Bookingendpoint.this, "PUT", REST_PATH, content, com.google.api.services.bookingendpoint.model.Booking.class);
    }

    @Override
    public UpdateBooking setAlt(java.lang.String alt) {
      return (UpdateBooking) super.setAlt(alt);
    }

    @Override
    public UpdateBooking setFields(java.lang.String fields) {
      return (UpdateBooking) super.setFields(fields);
    }

    @Override
    public UpdateBooking setKey(java.lang.String key) {
      return (UpdateBooking) super.setKey(key);
    }

    @Override
    public UpdateBooking setOauthToken(java.lang.String oauthToken) {
      return (UpdateBooking) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateBooking setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateBooking) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateBooking setQuotaUser(java.lang.String quotaUser) {
      return (UpdateBooking) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateBooking setUserIp(java.lang.String userIp) {
      return (UpdateBooking) super.setUserIp(userIp);
    }

    @Override
    public UpdateBooking set(String parameterName, Object value) {
      return (UpdateBooking) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Bookingendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Bookingendpoint}. */
    @Override
    public Bookingendpoint build() {
      return new Bookingendpoint(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link BookingendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setBookingendpointRequestInitializer(
        BookingendpointRequestInitializer bookingendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(bookingendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
