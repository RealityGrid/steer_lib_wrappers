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
 * This class provides access to the RealityGrid steering side API.
 *
 * @version 3.0
 * @author Robert Haines
 * @see <a href="http://www.realitygrid.org/">The RealityGrid Website</a>
 * @see <a href="http://code.google.com/p/computational-steering/">
   the Computational Steering pages at Google Code</a>
 * @see <a href="http://computational-steering.googlecode.com/files/steering_api-1.2.pdf">RealityGrid API Documentation v1.2 (PDF file)</a>
 */
public class ReG_SteerSteerside implements ReG_SteerConstants {
  
  private static ReG_SteerSteerside instance = new ReG_SteerSteerside();

  private int status;

  /**
   * Private constructor. Use <code>getInstance</code> to create an instance
   * of this class.
   */
  private ReG_SteerSteerside() {
  }

  /**
   * This method is used to obtain an instance of this class. Only one
   * instance of this class is allowed, so there are only private
   * constructors and all users of this instance will be using the
   * same instance of the steering library.
   *
   * @return the single instance of ReG_SteerSteerside.
   */
  public static ReG_SteerSteerside getInstance() {
    return instance;
  }

  /**
   * Initialise the internal tables <em>etc.</em> used by the steering library
   * on the steering client side. This must be called before all other steering
   * library methods.
   *
   * @throws ReG_SteerException If initialisation fails,
   * <code>REG_FAILURE</code> is returned as the error code.
   *
   * @see #steererFinalize()
   */
  public void steererInitialize() throws ReG_SteerException {
    status = ReG_Steer.Steerer_initialize();

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not initialise the steerer", status);
    }
  }

  /**
   * Cleans up the internal tables, <em>etc.</em> used by the client-side
   * steering library. This should be called after all steering activity is
   * complete.
   *
   * @see #steererInitialize()
   */
  public void steererFinalize() {
    status = ReG_Steer.Steerer_finalize();
  }

  /**
   * This method queries available middleware for a list of steerable
   * applications. The results of this method are used as input to
   * <code>simAttach</code>.
   *
   * @return a two dimensional array of Strings. The first entry in this array
   * is an array of application names and the second is an array of application
   * Grid Service Handles (GSHs). If no steerable applications are found this
   * method returns <code>null</code>.
   *
   * @throws ReG_SteerException If no steerable applications are found the
   * errorCode will be <code>REG_FAILURE</code>.
   *
   * @see #simAttach
   */
  public String[][] getSimList() throws ReG_SteerException {
    Intp nSims = new Intp();
    String[] simNames = new String[REG_MAX_NUM_STEERED_SIM];
    String[] simGSHs = new String[REG_MAX_NUM_STEERED_SIM];
    String[][] simList = null;

    status = ReG_Steer.Get_sim_list(nSims.cast(), simNames, simGSHs);

    if((status != REG_SUCCESS) || (nSims.value() <= 0)) {
      throw new ReG_SteerException("Could not find a list of steerable applications.", status);
    }
    else {
      simList = new String[2][nSims.value()];
      for(int i = 0; i < nSims.value(); i++) {
	simList[0][i] = simNames[i];
	simList[1][i] = simGSHs[i];
      }
    }

    return simList;
  }

  /**
   * Query the registry for a list of its entries.
   *
   * @param regAddress The registry address.
   * @param sec The security details to use.
   * 
   * @return The list of entries in the registry.
   *
   * @see #getRegistryEntriesFilteredSecure
   * @see ReG_SteerSecurity
   */
  public ReG_SteerRegistryEntry[] getRegistryEntriesSecure(String regAddress,
						      ReG_SteerSecurity sec) {
    return (ReG_SteerRegistryEntry[]) ReG_Steer.Get_registry_entries_secure_j(regAddress, sec);
  }

  /**
   * Query the registry for a list of its entries filtered by the specified
   * pattern.
   *
   * @param regAddress The registry address.
   * @param sec The security details to use.
   * @param pattern The pattern to filter the registry entries against.
   * 
   * @return The filtered list of entries in the registry.
   * 
   * @see #getRegistryEntriesSecure
   * @see ReG_SteerSecurity
   */
  public ReG_SteerRegistryEntry[] getRegistryEntriesFilteredSecure(String regAddress,
						      ReG_SteerSecurity sec,
						      String pattern) {
    return (ReG_SteerRegistryEntry[]) ReG_Steer.Get_registry_entries_filtered_secure_j(regAddress, sec, pattern);
  }

  /**
   * Attach to a simulation.
   *
   * @param simID The ID of the simulation to attach to. If using WSRF steering
   * this will be SWS EPR. If using sockets-based steering it can be a string
   * in the form <code>host:port</code> or empty, in which case the simulation
   * address will be determined by the contents of
   * <code>REG_APP_ADDRESS</code>. If using file-based steering it should be
   * empty.
   *
   * @return The internal handle ID of this simulation.
   *
   * @throws ReG_SteerException If unable to attach.
   *
   * @see #simAttachSecure
   * @see #simDetach
   */
  public int simAttach(String simID) throws ReG_SteerException {
    Intp simHandle = new Intp();
    String where = "remote";

    if(simID == "") {
      where = "local";
    }

    status = ReG_Steer.Sim_attach(simID, simHandle.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not attach to " + where + " simulation.", status);
    }

    return simHandle.value();
  }

  /**
   * Attach to a simulation.
   *
   * @param simID The ID of the simulation to attach to. If using WSRF steering
   * this will be SWS EPR. If using sockets-based steering it can be a string
   * in the form <code>host:port</code> or empty, in which case the simulation
   * address will be determined by the contents of
   * <code>REG_APP_ADDRESS</code>. If using file-based steering it should be
   * empty.
   * @param sec The security details to use.
   *
   * @return The internal handle ID of this simulation.
   *
   * @throws ReG_SteerException If unable to attach.
   *
   * @see #simAttach
   * @see #simDetach
   */
  public int simAttachSecure(String simID, ReG_SteerSecurity sec) throws ReG_SteerException {
    Intp simHandle = new Intp();
    String where = "remote";

    if(simID == "") {
      where = "local";
    }

    status = ReG_Steer.Sim_attach_secure(simID, sec, simHandle.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not attach to " + where + " simulation.", status);
    }

    return simHandle.value();
  }

  /**
   * Detach from a connected simulation.
   *
   * @param simHandle The internal ID of the simulation to detach from.
   *
   * @see #simAttach
   * @see #simAttachSecure
   */
  public int simDetach(int simHandle) {
    Intp sh = new Intp();
    sh.assign(simHandle);

    status = ReG_Steer.Sim_detach(sh.cast());

    return sh.value();
  }

  /**
   * Deletes all data associated with the simulation with handle
   * <code>simHandle</code>. Used when a simulation detaches. Supplied as a
   * separate interface because also required when the simulation initiates the
   * detach (e.g. when it has completed its run).
   *
   * @param simHandle The internal ID of the simulation for which to delete the
   * internal tables.
   */
  public int deleteSimTableEntry(int simHandle) {
    Intp sh = new Intp();
    sh.assign(simHandle);

    status = ReG_Steer.Delete_sim_table_entry(sh.cast());

    return sh.value();
  }

  /**
   * Return the number of parameters (read-only or read-write) that have been
   * registered in the connected simulation.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param steerable Whether to enumerate read-write (<code>REG_TRUE</code>)
   * or read-only (<code>REG_FALSE</code>) parameters.
   *
   * @return The number of parameters.
   *
   * @throws ReG_SteerException If there was any sort of problem.
   */
  public int getParamNumber(int simHandle, boolean steerable) 
    throws ReG_SteerException {
    
    int steer = REG_TRUE;
    Intp numParams = new Intp();
    String which = "steerable";

    if(!steerable) {
      steer = REG_FALSE;
      which = "monitored";
    }

    status = ReG_Steer.Get_param_number(simHandle, steer, numParams.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Failed to get number of " + which + " parameters.", status);
    }

    return numParams.value();
  }

  /**
   * Get the parameter values from the connected application. This method acts
   * in a rather c-like way in that you need to supply the number of parameters
   * to be returned. This will be improved upon in a later version.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param steerable Whether to return read-write (<code>REG_TRUE</code>) or
   * read-only (<code>REG_FALSE</code>) parameter values.
   * @param numParams The number of parameters to be returned.
   *
   * @return An array of ReG_SteerParameter representing the set of parameters.
   *
   * @see #getParamNumber
   * @see ReG_SteerParameter
   */
  public ReG_SteerParameter[] getParamValues(int simHandle, boolean steerable, int numParams) {

    int steer = REG_TRUE;
    String which = "steerable";
    ReG_SteerParameter[] values; // = new ReG_SteerParameter[numParams];

    if(!steerable) {
      steer = REG_FALSE;
      which = "monitored";
    }

    values = (ReG_SteerParameter[]) ReG_Steer.Get_param_values_j(simHandle, steer, numParams);

    return values;
  }

  /**
   * Sets the values of the parameters with the specified handles for the
   * specified simulation. Causes internal flags to be set to indicate that
   * these parameter values have changed. The new values are sent to the
   * simulation on the next call of emitControl().
   *
   * @param simHandle The internal ID of the simulation of whose parameters to
   * change.
   * @param handles The parameter handles of the parameters to change.
   * @param values The new values for the parameters.
   *
   * @throws ReG_SteerException If there is an internal library problem.
   */
  public void setParamValues(int simHandle, int[] handles, String[] values) throws ReG_SteerException {
    status = ReG_Steer.Set_param_values(simHandle, handles.length, handles, values);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Failed to set new param values.", status);
    }
  }

  /**
   * Get the number of supported commands from the application.
   *
   * @param simHandle The internal ID of the simulation to query.
   *
   * @return The number of supported commands.
   */
  public int getSuppCmdNumber(int simHandle) throws ReG_SteerException {
    Intp numCmds = new Intp();

    status = ReG_Steer.Get_supp_cmd_number(simHandle, numCmds.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not get the number of supported commands from the simulation.", status);
    }

    return numCmds.value();
  }

  /**
   * Get the supported commands from the connected application. This method acts
   * in a rather c-like way in that you need to supply the number of parameters
   * to be returned. This will be improved upon in a later version.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param numCmds The number of commands to be returned.
   *
   * @return An array of the supported commands.
   */
  public int[] getSuppCmds(int simHandle, int numCmds) {
    int[] cmdIDs = new int[numCmds];    

    status = ReG_Steer.Get_supp_cmds(simHandle, numCmds, cmdIDs);

    return cmdIDs;
  }

  /**
   * A wrapper for generating a Stop command and sending it to the application.
   *
   * @param simHandle the handle of the simulation to send the command to.
   *
   * @throws ReG_SteerException If Stop command could not be issued.
   */
  public void emitStopCmd(int simHandle) throws ReG_SteerException {
    status = ReG_Steer.Emit_stop_cmd(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not issue Stop command.", status);
    }
  }

  /**
   * A wrapper for generating a Pause command and sending it to the
   * application.
   *
   * @param simHandle the handle of the simulation to send the command to.
   *
   * @throws ReG_SteerException If Pause command could not be issued.
   */
  public void emitPauseCmd(int simHandle) throws ReG_SteerException {
    status = ReG_Steer.Emit_pause_cmd(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not issue Pause command.", status);
    }
  }

  /**
   * A wrapper for generating a Resume command and sending it to the
   * application.
   *
   * @param simHandle the handle of the simulation to send the command to.
   *
   * @throws ReG_SteerException If Resume command could not be issued.
   */
  public void emitResumeCmd(int simHandle) throws ReG_SteerException {
    status = ReG_Steer.Emit_resume_cmd(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not issue Resume command.", status);
    }
  }

  /**
   * Look for the next message from any attached simulations. If one is found
   * then the handle of the originating simulation along with the type of the
   * message is returned.
   *
   * @return an integer array of length two holding the handle of the
   * originating simulation and the type of message, in that order.
   *
   * @throws ReG_SteerException If no messages are found,
   * <code>REG_FAILURE</code> is returned as the error code.
   */
  public int[] getNextMessage() throws ReG_SteerException {
    Intp simHandle = new Intp();
    Intp msgType = new Intp();
    int[] result = new int[2];

    status = ReG_Steer.Get_next_message(simHandle.cast(), msgType.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("No messages found.", status);
    }

    result[0] = simHandle.value();
    result[1] = msgType.value();

    return result;
  }

  /**
   * Consume the parameter definitions emitted by the steered application.
   * The steering library's internal table of parameters is updated ready for
   * querying.
   *
   * @param simHandle the handle of the simulation to query for parameter
   * definitions.
   *
   * @throws ReG_SteerException If consumption of the parameter definitions
   * fails the error code will be <code>REG_FAILURE</code>.
   */
  public void consumeParamDefs(int simHandle) throws ReG_SteerException {
    
    status = ReG_Steer.Consume_param_defs(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not consume parameter definitions.", status);
    }
  }

  /**
   * Consume the IOType descriptions emitted by the steered application.
   * These descriptions provide information to be displayed in steering clients
   * in order to allow the user to request sample data to be emitted and
   * consumed.
   *
   * @param simHandle the handle of the simulation to query for IOType
   * descriptions.
   *
   * @throws ReG_SteerException If consumption of the IOType descriptions
   * fails the error code will be <code>REG_FAILURE</code>.
   */
  public void consumeIOTypeDefs(int simHandle) throws ReG_SteerException {
    
    status = ReG_Steer.Consume_IOType_defs(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not consume IOType descriptions.", status);
    }
  }

  /**
   * Consume the Checkpoint Type descriptions emitted by the steered
   * application. These descriptions provide information to be displayed in
   * steering clients in order to allow the user to request a checkpoint to be
   * taken.
   *
   * @param simHandle the handle of the simulation to query for Checkpoint Type
   * descriptions.
   *
   * @throws ReG_SteerException If consumption of the Checkpoint Type
   * descriptions fails the error code will be <code>REG_FAILURE</code>.
   */
  public void consumeChkTypeDefs(int simHandle) throws ReG_SteerException {
    
    status = ReG_Steer.Consume_ChkType_defs(simHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not consume checkpoint type definitions.", status);
    }
  }

  /**
   * Emit a steering-control message to a simulation. Emits the specified
   * commands (if any) and automatically sends any (steerable) parameter
   * values that have been edited since the last call to this routine.
   *
   * @param simHandle the handle of the simulation to emit to.
   * @param sysCmds List of commands to send.
   * @param sysCmdParams Parameters (if any) to go with each command.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public void emitControl(int simHandle, int[] sysCmds, String[] sysCmdParams) throws ReG_SteerException {
    int numCmds = 0;
    if(sysCmds != null) {
      numCmds = sysCmds.length;
    }

    status = ReG_Steer.Emit_control(simHandle, numCmds, sysCmds, sysCmdParams);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Emit control failed.", status);
    }
  }

  /**
   * Get the number of IOTypes from the application.
   *
   * @param simHandle The internal ID of the simulation to query.
   *
   * @return The number of IOTypes.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public int getIOTypeNumber(int simHandle) throws ReG_SteerException {
    Intp numIOTypes = new Intp();

    status = ReG_Steer.Get_iotype_number(simHandle, numIOTypes.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not get number of IO types.", status);
    }

    return numIOTypes.value();
  }

  /**
   * Get the IOTypes from the connected application.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param ioHandles An array to hold the returned io handles of the IOTypes.
   * @param ioLabels An array to hold the returned labels of the IOTypes.
   * @param ioDirs An array to hold the returned directions of the IOTypes.
   * @param ioFreqs An array to hold the returned in/out frequencies of the
   * IOTypes.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public void getIOTypes(int simHandle, int[] ioHandles, String[] ioLabels, int[] ioDirs, int[] ioFreqs) throws ReG_SteerException {
    int numIOs = ioHandles.length;

    status = ReG_Steer.Get_iotypes(simHandle, numIOs, ioHandles, ioLabels,
				   ioDirs, ioFreqs);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("NO!", status);
    }
  }

  /**
   * Get the number of Checkpoint Types from the application.
   *
   * @param simHandle The internal ID of the simulation to query.
   *
   * @return The number of Checkpoint Types.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public int getChkTypeNumber(int simHandle) throws ReG_SteerException {
    Intp numChkTypes = new Intp();

    status = ReG_Steer.Get_chktype_number(simHandle, numChkTypes.cast());

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not get number of checkpoint types.", status);
    }

    return numChkTypes.value();
  }

  /**
   * Get the Checkpoint Types from the connected application.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param chkHandles An array to hold the returned io handles of the
   * Checkpoint Types.
   * @param chkLabels An array to hold the returned labels of the Checkpoint
   * Types.
   * @param chkDirs An array to hold the returned directions of the Checkpoint
   * Types.
   * @param chkFreqs An array to hold the returned in/out frequencies of the
   * Checkpoint Types.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public void getChkTypes(int simHandle, int[] chkHandles, String[] chkLabels, int[] chkDirs, int[] chkFreqs) throws ReG_SteerException {
    int numChks = chkHandles.length;

    status = ReG_Steer.Get_chktypes(simHandle, numChks, chkHandles, chkLabels,
				    chkDirs, chkFreqs);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("NO!", status);
    }
  }

  /**
   * Consume a status message emitted by the simulation associated with
   * <code>simHandle</code>. Returns that simulation's current sequence no.
   * and a list of any commands received (e.g. notification that it has
   * finished). Any parameter values received by this routine are automatically
   * used to update the internal library table of parameters.
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param commands List of commands received from simulation.
   */
  public int consumeStatus(int simHandle, int[] commands) throws ReG_SteerException {
    Intp seqNum = new Intp();
    Intp numCmds = new Intp();

    status = ReG_Steer.Consume_status(simHandle, seqNum.cast(), numCmds.cast(), commands);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("NO, no, no!", status);
    }

    return seqNum.value();
  }

  /**
   * Emit a command to instruct the steered application to emit all of the
   * logged values of the specified parameter. The log itself is stored
   * internally and is accessed via Get_param_log().
   *
   * @param simHandle The internal ID of the simulation to query.
   * @param paramHandle The internal ID of the parameter for which to retrieve
   * log.
   *
   * @throws ReG_SteerException If there is an internal Steering Library error.
   */
  public void emitRetrieveParamLogCmd(int simHandle, int paramHandle) throws ReG_SteerException {
    
    status = ReG_Steer.Emit_retrieve_param_log_cmd(simHandle, paramHandle);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Could not emit param log command.", status);
    }
  }

}
