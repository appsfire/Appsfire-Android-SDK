package com.appsfire.mediation;

import java.util.Map;

import com.google.ads.mediation.MediationServerParameters;


public class AppsfireServerParameters extends MediationServerParameters {
	/*
	 * This class can either override load(Map<String, String>) or can provide
	 * String fields with an @Parameter annotation. Optional parameters can be
	 * specified in the annotation with required = false. If any required
	 * parameters are missing from the server, this adapter will be skipped.
	 */
	
	@Override
	public void load(Map<String, String> parameters) throws MappingException {
		super.load(parameters);
		
	}
		
	/**
	 * sdk key
	 */
	@Parameter(name="sdkKey")
	public String sdkKey;
	
	/**
	 * debug mode
	 */
	@Parameter(name="isDebug")
	public String isDebug;
}
