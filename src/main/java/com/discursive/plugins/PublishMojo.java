package com.discursive.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nu.xom.WellformednessException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * This Mojo simply verifies that each chapter has an appropriate identifer.
 * 
 * @goal publish
 * @phase deploy
 * @requiresProject
 */
public class PublishMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * @parameter
	 * @required
	 */
	protected File pdf;

	/**
	 * @parameter
	 * @required
	 */
	protected String apiKey;
	
	/**
	 * @parameter
	 */
	protected String docId;
	
	/**
	 * @parameter
	 */
	protected String apiUrl = "http://www.scribd.com/api";
	
	/**
	 * @parameter
	 */
	protected String downloadAndDrm = "view-only";
	
	/**
	 * @parameter
	 */
	protected String access = "public";
	
	/**
	 * @parameter
	 */
	protected String docType;

	/**
	 * @parameter
	 */
	protected String paidContent;

	/**
	 * @parameter
	 */
	protected String secureDocument;
	
	/**
	 * @parameter
	 */
	protected String apiSig;

	/**
	 * @parameter
	 */
	protected String sessionKey;

	/**
	 * @parameter
	 */
	protected String myUserId;


	public void execute() throws MojoExecutionException, MojoFailureException {
		
		HttpClient client = new HttpClient( );
        
		// Create POST method
		String weblintURL = apiUrl + "?method=docs.upload&api_key=" + apiKey;
		
		if( !StringUtils.isEmpty( docId ) ) {
			weblintURL += "&rev_id=" + docId;
		}
		
		if( !StringUtils.isEmpty( downloadAndDrm ) ) {
			weblintURL += "&download_and_drm=" + downloadAndDrm;
		}
		
		if( !StringUtils.isEmpty( access ) ) {
			weblintURL += "&access=" + access;
		}
		
		if( !StringUtils.isEmpty( docType ) ) {
			weblintURL += "&doc_type=" + docType;
		}
		
		if( !StringUtils.isEmpty( paidContent ) ) {
			weblintURL += "&paid_content=" + docType;
		}
		
		if( !StringUtils.isEmpty( secureDocument ) ) {
			weblintURL += "&secure_document=" + secureDocument;
		}
		
		if( !StringUtils.isEmpty( apiSig ) ) {
			weblintURL += "&api_sig=" + apiSig;
		}
		
		if( !StringUtils.isEmpty( sessionKey ) ) {
			weblintURL += "&session_key=" + sessionKey;
		}
		
		if( !StringUtils.isEmpty( myUserId ) ) {
			weblintURL += "&my_user_id=" + myUserId;
		}
		
		MultipartPostMethod method = 
			new MultipartPostMethod( weblintURL );
		
		try {
			method.addParameter("file", pdf );
		} catch (FileNotFoundException e) {
			getLog().error( e );
		}
		// Execute and print response
			try {
				client.executeMethod( method );
			} catch (HttpException e) {
				getLog().error( e );
			} catch (IOException e) {
				getLog().error( e );
			}
		String response = null;
		try {
			response = method.getResponseBodyAsString( );
		} catch (IOException e) {
			getLog().error( e );
		}
		System.out.println( response );
		method.releaseConnection( );
		
	}


}
