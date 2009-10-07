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
 * This abstract class provides a superclass for the internal SWIG generated
 * classes such as SWIGTYPE_p_int and so on.
 *
 * @version 1.2b
 * @author Robert Haines
 */
public abstract class ReG_SWIG {

  /**
   * Compares two objects of type ReG_SWIG to see if they are the same object
   * (their pointers are the same).
   *
   * @return true if the two objects are the same object, false otherwise.
   */
  public boolean equals(Object obj) {
    boolean equal = false;
    if (obj instanceof ReG_SWIG)
      equal = (((ReG_SWIG)obj).getPointer() == this.getPointer());
    return equal;
  }
  
  /**
   * Provides a numerical representation of the internal SWIG pointer
   * to this object. Used in comparison routines and so on.
   *
   * @return the pointer to this object.
   * @see #equals(Object)
   */
  protected abstract long getPointer();

  /**
   * Provides a C/C++ style void pointer to this object.
   *
   * @return the void pointer to this object.
   */
  public SWIGTYPE_p_void getVoidPointer() {
    return new SWIGTYPE_p_void(getPointer(), false);
  }
}
