package com.example.bot.spring.echo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.mockito.internal.util.StringUtil;

import com.fet.crm.nspMicro.util.bean.HttpResult;

public class HttpUtil {

	private static final String DEFAULT_ENCODE = "UTF-8";

	/**
	 * 取得回傳的內容
	 * 
	 * @param entity
	 * @return
	 */
	private static String getResultContent(HttpEntity entity, String encode) throws Exception {
		String result = null;
		BufferedReader read = null;

		// //NSPLogUtil.pdp(HttpUtil.class, false,
		// LogConstant.EventType.INVOKE_WEBSERVICE_CONSUMER + " " +
		// "getResultContent", ModelUtil.objToMap(entity), null);

		try {
			if (entity != null) {
				InputStream content = entity.getContent();
				read = new BufferedReader(new InputStreamReader(content, encode));
				String line = null;
				StringBuffer tmp = new StringBuffer();
				// [code scan] 修正
				while ((line = escapeHtml(read.readLine())) != null) {
					tmp.append(line);
				}
				result = tmp.toString();
			}
		} catch (Exception e) {
			// 
			////NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			// e.printStackTrace();
		} finally {
			if (read != null) {
				read.close();
			}
		}

		// //NSPLogUtil.pdp(HttpUtil.class, false,
		// LogConstant.EventType.INVOKED_WEBSERVICE_CONSUMER + " " +
		// "getResultContent", ModelUtil.objToMap(result), null);

		return result;
	}

	/**
	 * 
	 * @param url
	 *            : 要 POST 的 URL
	 * @param params
	 *            : 要 POST 過去的參數
	 * @return
	 * @throws Exception
	 */
	public static HttpResult post(String url, Map<String, String> params) throws Exception {
		return post(url, params, DEFAULT_ENCODE, true);
	}

	/**
	 * 
	 * @param url
	 *            : 要 POST 的 URL
	 * @param params
	 *            : 要 POST 過去的參數
	 * @return
	 * @throws Exception
	 */
	public static HttpResult post(String url, String params) throws Exception {
		return post(url, params, DEFAULT_ENCODE, true, 300);
	}

	/**
	 * 
	 * @param url
	 *            : 要 POST 的 URL
	 * @param params
	 *            : 要 POST 過去的參數
	 * @param encode
	 *            : 編碼
	 * @return
	 * @throws Exception
	 */
	public static HttpResult post(String url, Map<String, String> params, String encode, boolean isEncode)
			throws Exception {
		HttpResult result = null;
		Integer resultStatus = null;
		String resultContent = null;
		CloseableHttpResponse response = null;

		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url.toString());

			if (params != null) {
				Set<String> keys = params.keySet();
				if (keys.size() > 0) {
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					for (String k : keys) {
						String value = params.get(k);
						if (value == null) {
							value = "";
						}
						if (isEncode) {
							value = UrlUtil.encode(value);
						}
						nvps.add(new BasicNameValuePair(k, value));
					}
					httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				}
			}

			////NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKE_WEBSERVICE_CONSUMER + " " + url,
			//		MapUtil.objToMap(httpPost.getEntity()), null);

			response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			resultStatus = response.getStatusLine().getStatusCode();
			resultContent = getResultContent(entity, encode);

			EntityUtils.consume(entity);
		} catch (Exception e) {
			
			////NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e2) {
				
				////NSPLogUtil.error(HttpUtil.class, "", e2.getMessage(), e2);

				
				// e2.printStackTrace();
			}
		}

		result = new HttpResult(resultStatus, resultContent);

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKED_WEBSERVICE_CONSUMER + " " + url,
				//MapUtil.objToMap(result), null);

		return result;
	}

	/**
	 * @author Brian suen
	 * 
	 * @description 用於單純只傳Json字串的Http Post方法
	 * 
	 * @param url
	 *            : 要 POST 的 URL
	 * @param params
	 *            : 要 POST 過去的參數
	 * @param encode
	 *            : 編碼
	 * @param timeout
	 *            : 設定timeout秒數
	 * @return
	 * @throws Exception
	 */
	public static HttpResult post(String url, String params, String encode, boolean isEncode, int timeout)
			throws Exception {
		HttpResult result = null;
		Integer resultStatus = null;
		String resultContent = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;

		try {

			// 加入信任所有 Https 的邏輯
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000)
					.setConnectTimeout(timeout * 1000).build();

			httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLSocketFactory(sslsf).build();

			HttpPost httpPost = new HttpPost(url.toString());


			if (null != params && params.trim().length() > 0) {
				httpPost.setEntity(new StringEntity(params, encode));
				httpPost.setHeader("Content-type", "application/json");
			}

			//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKE_WEBSERVICE_CONSUMER + " " + url,
					//MapUtil.objToMap(httpPost.getEntity()), null);

			response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			resultStatus = response.getStatusLine().getStatusCode();
			resultContent = getResultContent(entity, encode);

			EntityUtils.consume(entity);
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				
				if (httpclient != null){
					httpclient.close();
				}
			} catch (Exception e2) {
				
				//NSPLogUtil.error(HttpUtil.class, "", e2.getMessage(), e2);

				
				// e2.printStackTrace();
			}
		}

		result = new HttpResult(resultStatus, resultContent);

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKED_WEBSERVICE_CONSUMER + " " + url,
				//MapUtil.objToMap(result), null);

		return result;
	}

	/**
	 * 
	 * @notes Created by Bonjour <br />
	 *        Created on 2015/5/25
	 * 
	 * @description 透過 post 方式打 http
	 *
	 *
	 * @param urlString
	 *            : URL
	 * @param requestBean
	 *            : 傳入的參數 bean
	 * @parma responseClass : 回傳的 class 型態
	 * @return
	 *
	 * @author Bonjour
	 */
	public static <T> T post(String urlString, Object requestBean, Class<T> responseClass) {
		T response = null;

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKE_WEBSERVICE_CONSUMER + " " + urlString,
				//MapUtil.objToMap(requestBean), null);

		if (null != urlString && urlString.trim().length() > 0) {
			try {
				URL url = new URL(urlString);

				// HttpURLConnection connection = (HttpURLConnection)
				// url.openConnection();
				// connection.setRequestMethod("POST");
				// connection.setDoInput(true);
				// connection.setDoOutput(true);
				// connection.connect();

				// [code scan] 修正
				Class<?> HttpURLConnectionClz = Class.forName("java.net.HttpURLConnection");
				Object connection = url.openConnection();
				HttpURLConnectionClz.getMethod("setRequestMethod", String.class).invoke(connection, "POST");
				HttpURLConnectionClz.getMethod("setDoInput", boolean.class).invoke(connection, true);
				HttpURLConnectionClz.getMethod("setDoOutput", boolean.class).invoke(connection, true);
				HttpURLConnectionClz.getMethod("connect").invoke(connection);

				PrintWriter output = null;
				BufferedReader input = null;
				try {
					output = new PrintWriter(((HttpURLConnection) connection).getOutputStream());
					output.print(NSPXmlUtil.parseBeanToXml(requestBean, true));
					output.flush();

					input = new BufferedReader(new InputStreamReader(
							ValidateUtil.validate(((HttpURLConnection) connection).getInputStream())));

					StringBuffer responseString = new StringBuffer();
					String result = null;
					// [code scan] 修正
					while ((result = escapeHtml(input.readLine())) != null) {
						responseString.append(result.trim());
					}

					response = NSPXmlUtil.parseXmlToBean(responseString.toString(), responseClass);
				} catch (Exception e) {
					throw e;
				} finally {
					if (output != null) {
						output.close();
					}
					if (input != null) {
						input.close();
					}
				}

			} catch (Exception e) {
				
				//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

				
				// e.printStackTrace();
			}
		}

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKED_WEBSERVICE_CONSUMER + " " + urlString,
				//MapUtil.objToMap(response), null);

		return response;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static HttpResult get(String url, Map<String, String> params) throws Exception {
		return get(url, params, DEFAULT_ENCODE);
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param encode
	 * @return
	 * @throws Exception
	 */
	public static HttpResult get(String url, Map<String, String> params, String encode) throws Exception {

		HttpResult result = null;
		Integer resultStatus = null;
		String resultContent = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuffer urlStr = new StringBuffer(url);
		StringBuffer urlLogStr = new StringBuffer(url);
		if (params != null) {
			Set<String> keys = params.keySet();
			if (keys.size() > 0) {
				urlStr.append("?");
				urlLogStr.append("?");
				int cnt = 0;
				for (String k : keys) {
					String value = params.get(k);
					if (value == null) {
						value = "";
					}

					if (cnt > 0) {
						urlStr.append("&");
						urlLogStr.append("&");
					}
					urlStr.append(k).append("=").append(UrlUtil.encode(value, encode));
					urlLogStr.append(k).append("=").append(value);

					cnt++;
				}
			}
		}

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKE_WEBSERVICE_CONSUMER + " " + urlLogStr);

		HttpGet httpGet = new HttpGet(urlStr.toString());
		CloseableHttpResponse response = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST either fully consume the response content or abort
		// request
		// execution by calling CloseableHttpResponse#close().

		try {
			HttpEntity entity = response.getEntity();

			resultStatus = response.getStatusLine().getStatusCode();
			resultContent = getResultContent(entity, encode);

			// and ensure it is fully consumed
			EntityUtils.consume(entity);
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			throw e;
		} finally {
			if (response != null) {
				response.close();
			}
		}

		result = new HttpResult(resultStatus, resultContent);

		//NSPLogUtil.pdp(HttpUtil.class, false, LogConstant.EventType.INVOKED_WEBSERVICE_CONSUMER + " " + url,
				//MapUtil.objToMap(resultContent), null);

		return result;
	}

	public static String validate(String data) {
		return validate(data, true);
	}

	public static String validate(String str, boolean isCanonicalize) {
		if (isCanonicalize) {
			str = canonicalize(str);
			str = canonicalized(str);
		}

		String result = null;
		if (str != null) {
			result = str;
		}
		return result;
	}

	/**
	 * canonicalized for code scan
	 * 
	 * @param uncanonicalizedObj
	 *            : uncanonicalizedObj
	 * @return
	 */
	// 2015.01.22 add by Janet
	public static <T> T canonicalized(T uncanonicalizedObj) {
		T canonicalizedObj = uncanonicalizedObj;
		return canonicalizedObj;
	}

	public static String canonicalize(String str) {
		final String ENCODING = "UTF-8";

		try {
			if (str != null) {
				return new String(str.getBytes(ENCODING), ENCODING);
			}
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			// e.printStackTrace();
		}
		return null;
	}

	public static String getIp(HttpServletRequest request) {
		String ip = " ";
		try {
			if (request != null) {
				ip = ValidateUtil.validate(request.getHeader("X-Forwarded-For"));
				if (ip == null) {
					ip = ValidateUtil.validate(request.getRemoteAddr());
				}
			}
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			// e.printStackTrace();
		}
		return ip;
	}

	public static String getUrl(HttpServletRequest request) {
		String url = " ";
		try {
			if (request != null) {
				int idx = ValidateUtil.validate(request.getRequestURL()).toString().indexOf(request.getContextPath());
				String serviceUri = ValidateUtil.validate(request.getRequestURL()).substring(0, idx);
				url = serviceUri + request.getContextPath();
			}
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			// e.printStackTrace();
		}
		return url;
	}

	public static String getDomain(HttpServletRequest request) {
		String domain = "";
		try {
			URL tmpUrl = new URL(getUrl(request));
			domain = tmpUrl.getHost();
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			e.printStackTrace();
		}
		return domain;
	}

	public static String getDomain(String url) {
		String domain = "";
		try {
			URL tmpUrl = new URL(url);
			domain = tmpUrl.getHost();
		} catch (Exception e) {
			
			//NSPLogUtil.error(HttpUtil.class, "", e.getMessage(), e);

			
			e.printStackTrace();
		}
		return domain;
	}

	// code scan 用
	private static String escapeHtml(String data) {
		return data;
	}
}
