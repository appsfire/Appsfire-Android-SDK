using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

public class AppsfireSDKDemo : MonoBehaviour
{
    bool m_bDisplayingAd = false;

    void Start() {
        // initialize ad sdk
        Debug.Log("Appsfire Ad SDK - Start");
        AppsfireSDK.ConnectWithAPIKey("YOUR_API_KEY_HERE", AFSDKFeature.AFSDKFeatureMonetization);		
		AppsfireAdSDK.SetDebugModeEnabled(true);		
		AppsfireAdSDK.Prepare();
    }

    /*
     *	Register / Unregister to Events
     */

    void OnEnable()
    {
        // attach to ad sdk events
        AppsfireSDKEvents.afsdkIsInitialized += this.afsdkIsInitialized;
        AppsfireSDKEvents.afsdkadModalAdsRefreshedAndAvailable += this.afsdkadModalAdsRefreshedAndAvailable;
        AppsfireSDKEvents.afsdkadModalAdRequestDidFailWithErrorCode += this.afsdkadModalAdRequestDidFailWithErrorCode;
        AppsfireSDKEvents.afsdkadModalAdWillAppear += this.afsdkadModalAdWillAppear;
        AppsfireSDKEvents.afsdkadModalAdDidAppear += this.afsdkadModalAdDidAppear;
        AppsfireSDKEvents.afsdkadModalAdWillWisappear += this.afsdkadModalAdWillWisappear;
        AppsfireSDKEvents.afsdkadModalAdDidDisappear += this.afsdkadModalAdDidDisappear;
    }

    void OnDisable()
    {
        // detach from ad sdk events
        AppsfireSDKEvents.afsdkIsInitialized -= this.afsdkIsInitialized;
        AppsfireSDKEvents.afsdkadModalAdsRefreshedAndAvailable -= this.afsdkadModalAdsRefreshedAndAvailable;
        AppsfireSDKEvents.afsdkadModalAdRequestDidFailWithErrorCode -= this.afsdkadModalAdRequestDidFailWithErrorCode;
        AppsfireSDKEvents.afsdkadModalAdWillAppear -= this.afsdkadModalAdWillAppear;
        AppsfireSDKEvents.afsdkadModalAdDidAppear -= this.afsdkadModalAdDidAppear;
        AppsfireSDKEvents.afsdkadModalAdWillWisappear -= this.afsdkadModalAdWillWisappear;
        AppsfireSDKEvents.afsdkadModalAdDidDisappear -= this.afsdkadModalAdDidDisappear;
    }

    void Update()
    {

        if (Input.GetButtonDown("Fire1") && !m_bDisplayingAd)
        {
            //tap
            if (AppsfireAdSDK.IsThereAModalAdAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi) == AFAdSDKAdAvailability.AFAdSDKAdAvailabilityYes)
            {
                Debug.Log("Appsfire - request modal ad");
                m_bDisplayingAd = true;
                AppsfireAdSDK.RequestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi);
            }
        }
    }

	/*
	 *	Events
	 */

	// ad sdk did initialize
	public void afsdkIsInitialized()
	{
		Debug.Log("Appsfire Ad SDK - Did Initialize");
	}

	// modal ad is ready for request
	public void afsdkadModalAdsRefreshedAndAvailable()
	{
		Debug.Log("Appsfire Ad SDK - Modal Ad Is Ready For Request");
	}

	// modal ad request did fail
	public void afsdkadModalAdRequestDidFailWithErrorCode(AFSDKErrorCode errorCode)
	{
        m_bDisplayingAd = false;
        Debug.Log("Appsfire Ad SDK - Modal Ad Request Did Fail With Error Code");
	}

	// modal ad will appear
	public void afsdkadModalAdWillAppear()
	{
		Debug.Log("Appsfire Ad SDK - Modal Ad Will Appear");
	}
	
	// modal ad did appear
	public void afsdkadModalAdDidAppear()
	{
		Debug.Log("Appsfire Ad SDK - Modal Ad Did Appear");
	}

	// modal ad will disappear
	public void afsdkadModalAdWillWisappear()
	{
		Debug.Log("Appsfire Ad SDK - Modal Ad Will Disappear");
	}
	
	// modal ad did disappear
	public void afsdkadModalAdDidDisappear()
	{
        m_bDisplayingAd = false;
		Debug.Log("Appsfire Ad SDK - Modal Ad Did Disappear");
	}
}
