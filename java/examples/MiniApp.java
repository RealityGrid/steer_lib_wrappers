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

public class MiniApp implements Runnable, ReG_SteerConstants {

  /* The Thread */
  Thread t;

  /* The Steering Library */
  ReG_SteerAppside rsa;

  /* global variables */
  int nloops = 1000;
  boolean finished = false;
  int chunkDim = 4;

  /* IO Types */
  int numIOTypes;
  int[] ioTypeHandle = new int[REG_INITIAL_NUM_IOTYPES];


  /* Monitored and Steered parameters */
  ReG_SteerParameter<Integer> sleepTime;
  ReG_SteerParameter<Integer> opacityStepStop;
  ReG_SteerParameter<Float> temp;
  ReG_SteerParameter<Integer> nx;
  ReG_SteerParameter<Integer> ny;
  ReG_SteerParameter<Integer> nz;
  ReG_SteerParameter<Double> aAxis;
  ReG_SteerParameter<Double> bAxis;
  ReG_SteerParameter<Double> cAxis;
  ReG_SteerParameter<Integer> bool;

  public static void main(String[] argv) {
    new MiniApp();
  }

  public MiniApp() {

    rsa = ReG_SteerAppside.getInstance();

    /* Enable steering and init the library */
    rsa.steeringEnable(true);

    int[] commands = {REG_STR_STOP, REG_STR_PAUSE_INTERNAL};
    try {
      rsa.steeringInitialize("Java MiniApp v1.0", commands);
    }
    catch(ReG_SteerException e) {
      System.err.println(e.getMessage());
      System.exit(e.getErrorCode());
    }

    /* Register the input and output IO channels */
    try {
      ioTypeHandle[0] = rsa.registerIOType("SOME_INPUT_DATA", REG_IO_IN, 0);
      ioTypeHandle[1] = rsa.registerIOType("VTK_STRUCTURED_POINTS", REG_IO_OUT, 1);
    }
    catch(ReG_SteerException e) {
      System.err.println(e.getMessage());
      System.exit(e.getErrorCode());      
    }
    numIOTypes = 2;

    /* Register the parameters */
    sleepTime = new ReG_SteerParameter<Integer>("time_to_sleep", true, 1, "0", "100");
    opacityStepStop = new ReG_SteerParameter<Integer>("OPACITY_STEP_STOP", true, 130, "0", "256");
    temp = new ReG_SteerParameter<Float>("TEMP", false, 55.6f, "", "");
    aAxis = new ReG_SteerParameter<Double>("a_axis", true, 1.5, "0.01", "10.0");
    bAxis = new ReG_SteerParameter<Double>("b_axis", true, 1.5, "0.01", "10.0");
    cAxis = new ReG_SteerParameter<Double>("c_axis", true, 1.5, "0.01", "10.0");
    nx = new ReG_SteerParameter<Integer>("nx", true, 16, "1", "");
    ny = new ReG_SteerParameter<Integer>("ny", true, 16, "1", "");
    nz = new ReG_SteerParameter<Integer>("nz", true, 16, "1", "");
    bool = new ReG_SteerParameter<Integer>("boolean", true, 0, "0", "1");

    try {
      sleepTime.register();
      opacityStepStop.register();
      temp.register();
      aAxis.register();
      bAxis.register();
      cAxis.register();
      nx.register();
      ny.register();
      nz.register();
      bool.register();
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

    int chunkDim = 4;
    //int dataSize = 4096;;

    /* Enter main loop waiting for data to arrive */
    for(int i = 0; i < nloops; i++) {

      try {
	Thread.sleep(sleepTime.getValue().intValue() * 1000);
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

      System.out.println("opacityStepStop = " + opacityStepStop.getValue());
      System.out.println("temp            = " + temp.getValue());

      if(numReceivedCommands > 0) {
	for(int icmd = 0; icmd < numReceivedCommands; icmd++) {
	  switch(recvdCmds[icmd]) {
	  case REG_STR_STOP:
	    finished = true;
	    break;

	  default:
	    for(int j = 0; j < numIOTypes; j++) {
	      if(recvdCmds[icmd] == ioTypeHandle[1]) {
		// emit data
		try {
		  iohandle = rsa.emitStart(ioTypeHandle[1], i);
		  String header = "DATA HEADER";
		  rsa.emitDataSlice(iohandle, header);

		  /* nx, ny and nz are steerable so we need to alloc every time */
		  int bufferSize = nx.getValue().intValue() * ny.getValue().intValue() * nz.getValue().intValue();

		  float[] buffer = new float[bufferSize];
		  for(int l = 0; l < bufferSize; l++) 
		    buffer[l] = l;

		  /* emit data in chunks to mimic a parallel program */
		  if(nx.getValue().intValue() % chunkDim != 0) {
		    System.out.println("nx not a multiple of " + chunkDim);
		    rsa.emitStop(iohandle);
		    continue;
		  }

		  int numChunks = nx.getValue().intValue() / chunkDim;
		  System.out.println("nx = " + nx.getValue().intValue() + ", chunkDim = " + chunkDim + " so have " + numChunks + " chunks");

		  for(int iChunk = 0; iChunk < numChunks; iChunk++) {
		    System.out.println("chunk " + iChunk + "...");
		    String chunkHeader = "CHUNK HEADER";
		    rsa.emitDataSlice(iohandle, chunkHeader);

		    int dataCount = chunkDim * ny.getValue().intValue() * nz.getValue().intValue();
		    float[] sendBuffer = new float[dataCount];
		    for(int b = 0; b < dataCount; b++) {
		      int item = (iChunk * chunkDim * ny.getValue().intValue() * nz.getValue().intValue());
		      sendBuffer[b] = buffer[item + b];
		    }
		    rsa.emitDataSlice(iohandle, sendBuffer);
		  } // iChunk
		  rsa.emitStop(iohandle);
		}
		catch(ReG_SteerException e) {
		  System.err.println(e.getMessage());
		}
	      } // if ioTypeHandle
	    } // j (numIOTypes)
	    break;
	  } // switch recvdCmds
	  if(finished) break;
	} // icmd
	if(finished) break;
      } // numReceivedCommands

      temp.setValue(temp.getValue().floatValue() + 0.5347672f);

    } // nloops

    /* Clean-up the steering library */
    try {
      rsa.steeringFinalize();
    }
    catch(ReG_SteerException e) { }

    return;
  }

}
