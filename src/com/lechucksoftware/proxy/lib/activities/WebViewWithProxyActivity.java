package com.lechucksoftware.proxy.lib.activities;

import java.net.MalformedURLException;
import java.net.URI;

import com.shouldit.proxy.lib.ProxyConfiguration;
import com.shouldit.proxy.lib.ProxySettings;
import com.shouldit.proxy.lib.ProxyUtils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewWithProxyActivity extends Activity
{
	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	    setContentView(R.layout.webview);
	    
	    ProxyConfiguration proxyConf = null;
	    
	    try
		{
			proxyConf = ProxySettings.getCurrentHttpProxyConfiguration(getApplicationContext());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    if(Build.VERSION.SDK_INT < 12)
	    	ProxyUtils.setWebViewProxy(getApplicationContext(), proxyConf);	// Only for 1.x and 2.x devices
	    
	    Bundle extras = getIntent().getExtras();
	    URI uri = (URI) extras.getSerializable("URI");

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    
	    try
		{
			mWebView.loadUrl(uri.toURL().toString());
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
