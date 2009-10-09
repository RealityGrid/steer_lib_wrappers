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

package org.realitygrid.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * A collection of useful input/output methods. These methods fall into the
 * following groups:
 * <ul>
 * <li>Reading from the terminal (stdin).
 * </ul>
 *
 * @version 1.0
 * @author Robert Haines
 * @see <a href="http://www.realitygrid.org/">The RealityGrid Website</a>
 * @see <a href="http://www.sve.man.ac.uk/Research/AtoZ/RealityGrid">the RealityGrid pages at Manchester Computing</a>
 */
public final class IO {

  private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

  /**
   * Hidden contructor as all methods are static - no state
   */
  private IO() {
  }

  /**
   * Read a single line from the terminal (stdin). Input is gathered until
   * terminated by carriage return. If there is no input, <code>null</code> is
   * returned.
   *
   * @return the line that has been read in, or <code>null</code> if no input.
   * @see #readText()
   */
  public static String readLine() {
    String input = null;

    try {
      input = stdin.readLine();
    }
    catch(IOException e) {
      System.err.println("IOException: " + e);
    }

    return input;
  }

  /**
   * Read a single character from the terminal (stdin). If more than one
   * character is input before a carriage return is entered, then just the
   * first  character is returned. If no characters are input,
   * <code>'\0'</code> is returned.
   *
   * @return the first character input on stdin, or <code>'\0'</code> if no
   * input.
   * @see #readLine()
   */
  public static char readChar() {
    char result = '\0';

    try {
      result = readLine().charAt(0);
    }
    catch(IndexOutOfBoundsException e) {
      result = '\0';
    }

    return result;
  }

  /**
   * Read an <code>int</code> from the terminal (stdin). If no number is input
   * or the number that is input is invalid <code>Integer.MAX_VALUE</code> is
   * returned.
   *
   * @return the <code>int</code> input on stdin or
   * <code>Integer.MAX_VALUE</code>.
   * @see #readLine()
   * @see Integer#MAX_VALUE
   */
  public static int readInt() {
    int result = Integer.MAX_VALUE;
    String input = null;

    try {
      input = readLine();
      result = Integer.parseInt(input);
    }
    catch(NumberFormatException e) {
      System.err.println("Not a valid integer: " + input);
    }

    return result;
  }

  /**
   * Read a <code>float</code> from the terminal (stdin). If no number is input
   * or the number that is input is invalid <code>Float.NaN</code> is
   * returned.
   *
   * @return the <code>float</code> input on stdin or <code>Float.NaN</code>. 
   * @see #readLine()
   * @see #readDouble()
   * @see Float#NaN
   */
  public static float readFloat() {
    float result = Float.NaN;
    String input = null;

    try {
      input = readLine();
      result = Float.parseFloat(input);
    }
    catch(NumberFormatException e) {
      System.err.println("Not a valid float: " + input);
    }

    return result;
  }

  /**
   * Read a <code>double</code> from the terminal (stdin). If no number is
   * input or the number that is input is invalid <code>Double.NaN</code> is
   * returned.
   *
   * @return the <code>double</code> input on stdin or <code>Double.NaN</code>.
   * @see #readLine()
   * @see #readFloat()
   * @see Double#NaN
   */
  public static double readDouble() {
    double result = Double.NaN;
    String input = null;

    try {
      input = readLine();
      result = Double.parseDouble(input);
    }
    catch(NumberFormatException e) {
      System.err.println("Not a valid double: " + input);
    }

    return result;
  }

  /**
   * Read a (possibly) multiline block of text from the terminal (stdin). If
   * there is no input, <code>null</code> is returned.
   *
   * @return a String containing the block of text entered on stdin.
   * @see #readLine()
   */
  public static String readText() {
    String result = "";
    String input = "";

    try {
      while(input != null) {
	input = stdin.readLine();
	if(input != null) result += (input + "\n");
      }
    }
    catch(IOException e) {
      System.err.println("IOException: " + e);
    }

    return result;
  }

  /**
   * This is a stub function at the moment and <strong>does not</strong>
   * obscure the input password in anyway!
   *
   * @see #readLine()
   */
  public static String readPassword() {

    return IO.readLine();
  }
}
