package com.ensoft.imgurviewer;

import android.app.Application;

import com.ensoft.restafari.network.service.NetworkPreferencesService;
import com.ensoft.restafari.network.service.RequestService;
import com.ensoft.restafari.network.service.RequestServiceOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
	formUri = "http://imgurviewer.ensoft-dev.com/"
)
public class App extends Application
{
	protected static App instance;
	protected int activityCount = 0;
	protected NetworkPreferencesService preferencesService;

	public static App getInstance()
	{
		return instance;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		instance = this;

		preferencesService = new NetworkPreferencesService( this );

		ACRA.init( this );

		RequestServiceOptions requestServiceOptions = new RequestServiceOptions.Builder().
			setProxyHost( getPreferencesService().getProxyHost() ).
			setProxyPort( getPreferencesService().getProxyPort() ).
			build();

		RequestService.init( this, requestServiceOptions );
	}

	protected void onDestroy()
	{
		Fresco.shutDown();
	}

	public void addActivity()
	{
		if ( 0 == activityCount )
		{
			ImagePipelineConfig config = ImagePipelineConfig.newBuilder( this )
				.setDownsampleEnabled( true )
				.build();

			Fresco.initialize( this, config );
		}

		activityCount++;
	}

	public void destroyActivity()
	{
		activityCount--;

		if ( 0 == activityCount )
		{
			onDestroy();
		}
	}

	public NetworkPreferencesService getPreferencesService()
	{
		return preferencesService;
	}
}