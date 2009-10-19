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
 * This class provides information on a registry entry as
 * returned by various internal Steering Library query routines.
 *
 * @version 2.0
 * @author Robert Haines
 */
public class ReG_SteerRegistryEntry implements ReG_SteerConstants {
  
  private String serviceType;
  private String GSH;
  private String entryGSH;
  private String application;
  private String startDateTime;
  private String user;
  private String group;
  private String jobDescription;
  
  public ReG_SteerRegistryEntry() {}
  
  public ReG_SteerRegistryEntry(String type,
				String gsh,
				String egsh,
				String app,
				String start,
				String user,
				String group,
				String description) {
    serviceType = type;
    GSH = gsh;
    entryGSH = egsh;
    application = app;
    startDateTime = start;
    this.user = user;
    this.group = group;
    jobDescription = description;
  }
  
  public String getServiceType() {
    return serviceType;
  }
  
  public String getGSH() {
    return GSH;
  }
  
  public String getEntryGSH() {
    return entryGSH;
  }
  
  public String getApplication() {
    return application;
  }
  
  public String getStartDateTime() {
    return startDateTime;
  }
  
  public String getUser() {
    return user;
  }
  
  public String getGroup() {
    return group;
  }
  
  public String getJobDescription() {
    return jobDescription;
  }
  
  public String toString() {
    return application + " (" + jobDescription + ") - " + getCNFromUser(); 
  }

  private String getCNFromUser() {
    if(user.startsWith("/")) {
      int index = user.indexOf("/CN=");
      return user.substring(index + 4);
    }
    else return user;
  }
}
