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
 * This class provides various useful methods to aid developers that don't
 * really fall either on the Application or Steering Client side of the
 * steering system.<p>All methods are defined as <code>static</code> as no
 * state is required.
 *
 * @version 1.2b
 * @author Robert Haines
 */
public class ReG_SteerUtilities implements ReG_SteerConstants {

  /**
   * Convert a return code to a string representation, <em>e.g.</em>
   * <code>REG_FAILURE</code>.
   *
   * @param returnCode the error code to lookup.
   *
   * @throws ReG_SteerException If an invalid error code is given.
   *
   * @see ReG_SteerConstants
   */
  public static String lookupReturn(int returnCode) throws ReG_SteerException {
    String result;

    switch(returnCode) {
    case REG_SUCCESS:
      result = "REG_SUCCESS";
      break;
    case REG_FAILURE:
      result = "REG_FAILURE";
      break;
    case REG_EOD:
      result = "REG_EOD";
      break;
    case REG_MEM_FAIL:
      result = "REG_MEM_FAIL";
      break;
    case REG_TIMED_OUT:
      result = "REG_TIMED_OUT";
      break;
    case REG_NOT_READY:
      result = "REG_NOT_READY";
      break;
    case REG_UNFINISHED:
      result = "REG_UNFINISHED";
      break;
    default:
      throw new ReG_SteerException("Unknown error code.", REG_FAILURE);
    }

    return result;
  }

  /**
   * Convert a command number to a string representation, <em>e.g.</em>
   * <code>REG_STR_PAUSE</code>.
   *
   * @param command the command number to lookup.
   *
   * @throws ReG_SteerException If an invalid command number is given.
   *
   * @see ReG_SteerConstants
   */
  public static String lookupCommand(int command) throws ReG_SteerException {
    String result;

    switch(command) {
    case REG_STR_STOP:
      result = "REG_STR_STOP";
      break;
    case REG_STR_PAUSE:
      result = "REG_STR_PAUSE";
      break;
    case REG_STR_RESUME:
      result = "REG_STR_RESUME";
      break;
    case REG_STR_DETACH:
      result = "REG_STR_DETACH";
      break;
    case REG_STR_EMIT_PARAM_LOG:
      result = "REG_STR_EMIT_PARAM_LOG";
      break;
    case REG_STR_PAUSE_INTERNAL:
      result = "REG_STR_PAUSE_INTERNAL";
      break;
    default:
      throw new ReG_SteerException("Unknown command.", REG_FAILURE);
    }

    return result;
  }

  /**
   * Convert a type code to a string representation, <em>e.g.</em>
   * <code>REG_CHAR</code>.
   *
   * @param type the type code to lookup.
   *
   * @throws ReG_SteerException If an invalid type code is given.
   *
   * @see ReG_SteerConstants
   */
  public static String lookupType(int type) throws ReG_SteerException {
    String result;

    switch(type) {
    case REG_INT:
      result = "REG_INT";
      break;
    case REG_FLOAT:
      result = "REG_FLOAT";
      break;
    case REG_DBL:
      result = "REG_DBL";
      break;
    case REG_CHAR:
      result = "REG_CHAR";
      break;
    case REG_XDR_INT:
      result = "REG_XDR_INT";
      break;
     case REG_XDR_FLOAT:
      result = "REG_XDR_FLOAT";
      break;
    case REG_XDR_DOUBLE:
      result = "REG_XDR_DOUBLE";
      break;
    case REG_BIN:
      result = "REG_BIN";
      break;
   default:
      throw new ReG_SteerException("Unknown type.", REG_FAILURE);
    }

    return result;
  }

  /**
   * Convert a direction code to a string representation, <em>e.g.</em>
   * <code>REG_IO_IN</code>.
   *
   * @param dir the direction code to lookup.
   *
   * @throws ReG_SteerException If an invalid direction code is given.
   *
   * @see ReG_SteerConstants
   */
  public static String lookupDirection(int dir) throws ReG_SteerException {
    String result;

    switch(dir) {
    case REG_IO_IN:
      result = "REG_IO_IN";
      break;
    case REG_IO_OUT:
      result = "REG_IO_OUT";
      break;
    case REG_IO_INOUT:
      result = "REG_IO_INOUT";
      break;
    default:
      throw new ReG_SteerException("Unknown direction.", REG_FAILURE);
    }

    return result;
  }

  /**
   * A method to return the sizes of common datatypes used by
   * the RealityGrid Steering library. Datatypes should be passed in by their
   * RealityGrid names, for example: <code>REG_INT</code>,
   * <code>REG_CHAR</code> and <code>REG_XDR_INT</code>. Note that the sizes
   * that are returned correspond to those of the underlying C implementation
   * of the steering library, and not those of the Java virtual machine in
   * which this method is run.
   *
   * @param type the datatype whose size is required.
   *
   * @throws ReG_SteerException If an invalid type is supplied the error code
   * will be <code>REG_FAILURE</code>.
   *
   * @see ReG_SteerConstants
   */
  public static int sizeof(int type) throws ReG_SteerException {
    int size = ReG_Steer.Sizeof(type);
    if(size == 0) {
      throw new ReG_SteerException("Invalid type supplied to sizeof.", REG_FAILURE);
    }

    return size;
  }

}
