package com.lechucksoftware.proxy.lib.activities;

import java.net.MalformedURLException;
import java.net.URI;

import com.lechucksoftware.proxy.lib.ProxyUtils;

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
	    
	    if(Build.VERSION.SDK_INT < 12)
	    	ProxyUtils.setWebViewProxy(getApplicationContext());	// Only for 1.x and 2.x devices
	    
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
