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

import org.realitygrid.steering.*;

public class Sink implements Runnable, ReG_SteerConstants {

  /* The Thread */
  Thread t;

  /* The Steering Library */
  ReG_SteerAppside rsa;

  /* global variables */
  int nloops = 1000;
  int sleep_time = 1;
  boolean finished = false;

  /* */
  int iotype_handle;

  /* Monitored parameter */
  ReG_SteerParameter bytesRead;

  public static void main(String argv[]) {
    new Sink();
  }

  public Sink() {

    rsa = ReG_SteerAppside.getInstance();

    /* Enable steering and init the library */
    rsa.steeringEnable(true);

    int[] commands = {REG_STR_STOP};
    try {
      rsa.steeringInitialize("Java Sink v1.0", commands);
    }
    catch(ReG_SteerException e) {
      System.err.println(e.getMessage());
      System.exit(e.getErrorCode());
    }

    /* Register the input IO channel */
    try {
      iotype_handle = rsa.registerIOType("VTK_STRUCTURED_POINTS", REG_IO_IN, 1);
    }
    catch(ReG_SteerException e) {
      System.err.println(e.getMessage());
      System.exit(e.getErrorCode());      
    }

    /* Register monitored parameter */
    bytesRead = ReG_SteerParameter.create("Items_read", false, REG_INT, "", "");
    try {
      bytesRead.register();
    }
    catch(ReG_SteerException e) {
      System.err.println(e.getMessage());
      System.exit(e.getErrorCode());
    }

    /* Start the thread running */
    t = new Thread(this);
    t.start();
  }

  public void run() {

    int numParamsChanged;
    int numReceivedCommands;
    int[] recvdCmds;

    int iohandle;

    /* Enter main loop waiting for data to arrive */
    for(int i = 0; i < nloops; i++) {
      
      try {
	Thread.sleep(sleep_time * 1000);
      }
      catch(InterruptedException e) {
	System.err.println("Interrupted!");
	continue;
      }

      System.out.println("\ni = " + i);

      /* Talk to the steering client (if one is connected) */
      try {
	int[] result = rsa.steeringControl(i);
	numParamsChanged = result[0];
	numReceivedCommands = result[1];
	recvdCmds = rsa.getReceivedCommands();
      }
      catch(ReG_SteerException e) {
	System.err.println(e.getMessage());
	continue;
      }

      /* Zero count of bytes read this time around */
      bytesRead.setValue(0);

      if(numReceivedCommands > 0) {
	for(int icmd = 0; icmd < numReceivedCommands; icmd++) {
	  switch(recvdCmds[icmd]) {
	  case REG_STR_STOP:
	    finished = true;
	    break;

	  default:
	    if(recvdCmds[icmd] == iotype_handle) {
	      /* 'Open' the channel to consume data */
	      try {
		iohandle = rsa.consumeStart(iotype_handle);

		while(true) {
		  Object data = rsa.consumeDataSlice(iohandle);
		  if(data == null)
		    break;
		  if(data instanceof String) 
		    System.out.println(data);
		}

		/* Reached the end of this data set; 'close' the channel */
		iohandle = rsa.consumeStop(iohandle);

	      }
	      catch(ReG_SteerException e) {
		System.err.println(e.getMessage());
	      }
	    } // if recvd_cmds[icmd]
	    break;
	  } // switch
	  if(finished) break;
	} // for icmd
      } // if num_recvd_cmds
      if(finished) break;

    } // for i

    /* Clean-up the steering library */
    try {
      rsa.steeringFinalize();
    }
    catch(ReG_SteerException e) { }

    return;
  }
}
