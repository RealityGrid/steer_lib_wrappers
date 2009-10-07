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
 * This class provides exceptions for the RealityGrid Steering API. It extends
 * the standard Exception class with an error code that corresponds to the
 * values returned by RealityGrid methods, eg: REG_FAILURE.
 *
 * @version 1.2b
 * @author Robert Haines
 */
public class ReG_SteerException extends RuntimeException {

  private int errorCode;
  private boolean fatal;

  /**
   * Constructs a RealityGrid exception with no error code or
   * text description.
   */
  public ReG_SteerException() {
    super();
    fatal = false;
  }

  public ReG_SteerException(boolean f) {
    super();
    fatal = f;
  }

  /**
   * Constructs a RealityGrid exception with a text description but no
   * error code.
   *
   * @param s The text description.
   */
  public ReG_SteerException(String s) {
    super(s);
    fatal = false;
  }

  public ReG_SteerException(String s, boolean f) {
    super(s);
    fatal = f;
  }

  /**
   * Constructs a RealityGrid exception with an error code but no
   * text description.
   *
   * @param ec The error code.
   */
  public ReG_SteerException(int ec) {
    super();
    errorCode = ec;
    fatal = false;
  }

  public ReG_SteerException(int ec, boolean f) {
    super();
    errorCode = ec;
    fatal = f;
  }

  /**
   * Constructs a RealityGrid exception with an error code and a
   * text description.
   *
   * @param s The text description.
   * @param ec The error code.
   */
  public ReG_SteerException(String s, int ec) {
    super(s);
    errorCode = ec;
    fatal = false;
  }

  public ReG_SteerException(String s, int ec, boolean f) {
    super(s);
    errorCode = ec;
    fatal = f;
  }

  /**
   * Return the value of the error code of this exception.
   *
   * @return the error code contained in this exception.
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Return the fatal status of this exception.
   *
   * @return true if this exception should be treated as a fatal error,
   * false otherwise.
   */
  public boolean isFatal() {
    return fatal;
  }

  /**
   *
   */
  public String toString() {
    String errorMessage = super.toString();
    errorMessage += " ReG Error code: ";
    errorMessage += ReG_SteerUtilities.lookupReturn(errorCode);
    return errorMessage;
  }

}
