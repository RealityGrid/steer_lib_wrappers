/*
  The RealityGrid Steering Library Wrappers

  Copyright (c) 2002-2009, University of Manchester, United Kingdom.
  All rights reserved.

  This software is produced by Research Computing Services, University
  of Manchester as part of the RealityGrid project and associated
  follow on projects, funded by the EPSRC under grants GR/R67699/01,
  GR/R67699/02, GR/T27488/01, EP/C536452/1, EP/D500028/1,
  EP/F00561X/1.

  LICENCE TERMS

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:

    * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.

    * Neither the name of The University of Manchester nor the names
      of its contributors may be used to endorse or promote products
      derived from this software without specific prior written
      permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  POSSIBILITY OF SUCH DAMAGE.

  Author: Robert Haines
 */

package org.realitygrid.steering;

/**
 * This class provides security information for connecting to
 * registries and simulations.
 *
 * @version 2.0
 * @author Robert Haines
 */
public class ReG_SteerSecurity implements ReG_SteerConstants {

    private boolean usingSSL;
    private String  caCertsPath;
    private String  myKeyCertFile;
    private String  userDN;
    private String  passphrase;

    public ReG_SteerSecurity() {
	clear();
    }

    public ReG_SteerSecurity(boolean ssl, String caPath, String keyfile, String dn, String pass) {
	usingSSL = ssl;
	caCertsPath = caPath;
	myKeyCertFile = keyfile;
	userDN = dn;
	passphrase = pass;
    }

    public ReG_SteerSecurity(String filename) {
	ReG_SteerSecurity temp;
	temp = (ReG_SteerSecurity) ReG_Steer.Get_security_config_j(filename);

	usingSSL = temp.isUsingSSL();
	caCertsPath = temp.getCaCertsPath();
	myKeyCertFile = temp.getMyKeyCertFile();
	userDN = temp.getUserDN();
	passphrase = temp.getPassphrase();
    }

    public boolean isUsingSSL() {
	return usingSSL;
    }

    public void setUsingSSL(boolean ssl) {
	usingSSL = ssl;
    }

    public String getCaCertsPath() {
	return caCertsPath;
    }

    public void setCaCertsPath(String caPath) {
	caCertsPath = caPath;	
    }

    public String getMyKeyCertFile() {
	return myKeyCertFile;
    }

    public void setMyKeyCertFile(String keyfile) {
	myKeyCertFile = keyfile;
    }

    public String getUserDN() {
	return userDN;
    }

    public void setUserDN(String dn) {
	userDN = dn;
    }

    public String getPassphrase() {
	return passphrase;
    }

    public void setPassphrase(String pass) {
	passphrase = pass;
    }

    public void clear() {
	usingSSL = false;
	caCertsPath = "";
	myKeyCertFile = "";
	userDN = "";
	passphrase = "";	
    }

    public String toString() {
	String result = "Using SSL: " + usingSSL + "\n";
	result += "CA Certs Path: " + caCertsPath + "\n";
	result += "User Cert and Key File: " + myKeyCertFile + "\n";
	result += "User DN: " + userDN + "\nPassphrase: ";

	if(passphrase.length() > 0)
	    result += "**************";
	else
	    result += "<not set>";

	return result;
    }
}
