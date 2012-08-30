/**
 * 
 */
package com.lechucksoftware.proxy.lib.activities;

import java.net.URI;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lechucksoftware.proxy.lib.ProxyConfiguration;
import com.lechucksoftware.proxy.lib.ProxySettings;
import com.lechucksoftware.proxy.lib.ProxyUtils;

public class MainFragmentActivity extends FragmentActivity
{
	public static final String TAG = "TestActivity";
	static final int DIALOG_ID_PROXY = 0;

	
	String[] uriTypes = 
		{
    		"http://",
    		"http://www.",
    		"https://",
    		"https://www.",
    		"ftp://",
		};
		
	LinearLayout wait;
	LinearLayout contents;

	AutoCompleteTextView uriInput;
	TextView device_version;
	TextView proxy_enabled;
	TextView proxy_host;
	TextView proxy_port;
	TextView apl_tostring;
	Button get_settings;
	Button edit_settings;
	Button test_settings;
	Button test_webview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		wait = (LinearLayout) findViewById(R.id.wait);
		contents = (LinearLayout) findViewById(R.id.contents);
		
		uriInput = (AutoCompleteTextView) findViewById(R.id.uri_input);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, uriTypes);
		uriInput.setThreshold(1);
		uriInput.setAdapter(adapter);
		
		device_version = (TextView) findViewById(R.id.device_api_version_content);
		proxy_enabled = (TextView) findViewById(R.id.proxy_enabled_content);
		proxy_host = (TextView) findViewById(R.id.proxy_host_content);
		proxy_port = (TextView) findViewById(R.id.proxy_port_content);
		apl_tostring = (TextView) findViewById(R.id.apl_proxy_tostring_content);
		
		get_settings = (Button) findViewById(R.id.get_settings);
		get_settings.setOnClickListener(OnGetSettingsClick);
		
		edit_settings = (Button) findViewById(R.id.open_proxy_settings);
		edit_settings.setOnClickListener(OnEditProxySettings);
		
		test_settings = (Button) findViewById(R.id.test_proxy_settings);
		test_settings.setOnClickListener(OnTestProxySettings);
		
		test_webview = (Button) findViewById(R.id.test_proxed_webview);
		test_webview.setOnClickListener(OnTestWebView);
	}
	
	private final OnClickListener OnGetSettingsClick = new OnClickListener() {
		
		public void onClick(View v)
		{
			UpdateSettings();
		}
	};
	
	private final OnClickListener OnEditProxySettings = new OnClickListener() {
		
		public void onClick(View v)
		{
			Intent proxyIntent = ProxyUtils.getProxyIntent();
			startActivity(proxyIntent);
			UpdateSettings();
		}
	};
	
    private final OnClickListener OnTestProxySettings = new OnClickListener() {
        
        public void onClick(View v)
        {
			try
    		{	
				EnterWait();
				TestProxySettingsTask task = new TestProxySettingsTask();
				task.execute();  
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    };
    
    
	private class TestProxySettingsTask extends AsyncTask<Void, Void, String> 
	{
		@Override
		protected String doInBackground(Void... paramArrayOfParams) 
		{
			String result = null;
			
			try
			{
    			String uriString = uriInput.getText().toString().trim();
        		URI uri = URI.create(uriString);
        		ProxyConfiguration proxyConf;

				proxyConf = ProxySettings.getCurrentProxyConfiguration(getApplicationContext(), uri);
				result = ProxyUtils.getURI(uri,proxyConf.proxyHost);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return result;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			FragmentManager fm = getSupportFragmentManager();
			ResultDialogFragment resultDialog = null;
			
			if (result != null && result != "")
			{
			    resultDialog = new ResultDialogFragment(result.substring(0,500)+ " ... ");    
			}
			else
			{
			    resultDialog = new ResultDialogFragment("NOTHING RECEIVED");    
			}
			 
	        resultDialog.show(fm, "result_fragment_dialog");
	        ExitWait();
		}
	}
    	
    private final OnClickListener OnTestWebView = new OnClickListener() {
        
        public void onClick(View v)
        {
            try
            {
            	String uriString = uriInput.getText().toString().trim();
        		URI uri = URI.create(uriString);
                Intent webViewIntent = new Intent(getApplicationContext(),WebViewWithProxyActivity.class);
                webViewIntent.putExtra("URI", uri);
                startActivity(webViewIntent);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    
    public void EnterWait()
    {
    	wait.setVisibility(View.VISIBLE);
    	contents.setVisibility(View.GONE);
    }
	
    public void ExitWait()
    {
    	wait.setVisibility(View.GONE);
    	contents.setVisibility(View.VISIBLE);
    }
    
	public void UpdateSettings()
	{
		EnterWait();
		
		UpdateSettingsTask task = new UpdateSettingsTask();
		task.execute();  
	}
	
	private class UpdateSettingsTask extends AsyncTask<Void, Void, ProxyConfiguration> 
	{
		@Override
		protected ProxyConfiguration doInBackground(Void... paramArrayOfParams) 
		{
			ProxyConfiguration proxyConf = null;
			
			try
			{
				String uriString = uriInput.getText().toString();
				URI uri = URI.create(uriString);
				
				try
				{
					proxyConf = ProxySettings.getCurrentProxyConfiguration(getApplicationContext(), uri);
					ShowSettings(proxyConf);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return proxyConf;
		}

		@Override
		protected void onPostExecute(ProxyConfiguration proxyConf) 
		{
	        ExitWait();
		}

	}
	
	public void ShowSettings(ProxyConfiguration proxyConf)
	{
		device_version.setText(String.valueOf(proxyConf.deviceVersion));
		proxy_enabled.setText(String.valueOf(proxyConf.isProxyEnabled()));
		apl_tostring.setText(proxyConf.toString());
		
		switch (proxyConf.getConnectionType())
		{
			case DIRECT:
				proxy_host.setText(getApplicationContext().getResources().getString(R.id.proxy_host_content));
				proxy_port.setText(getApplicationContext().getResources().getString(R.id.proxy_port_content));
				break;
			case HTTP:
				proxy_host.setText(String.valueOf(proxyConf.getProxyHost()));
				proxy_port.setText(String.valueOf(proxyConf.getProxyPort()));
				break;
			case SOCKS:
				throw new UnsupportedOperationException("SOCKS not already supported");
		}
	}
}