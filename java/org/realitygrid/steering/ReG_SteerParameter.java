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
 * This class provides a steerable parameter. It can be of type REG_INT,
 * REG_FLOAT or REG_DBL. The REG_CHAR data type is not supported in
 * Java at present due to the way in which SWIG treats pointers to char.
 *
 * @version 3.0
 * @author Robert Haines
 */
public class ReG_SteerParameter<T extends Number> implements ReG_SteerConstants {

  private int handle;
  private String label;
  private int type;
  private boolean steerable;
  private String minLabel;
  private String maxLabel;

  private Intp intParam = null;
  private Floatp floatParam = null;
  private Doublep doubleParam = null;

  private boolean registered;

  /*
   * Hide the default constructor
   */
  private ReG_SteerParameter() {}

  /*
   * Private constructor:
   * Creates a parameter but does not set its value or type.
   * Purely used by the public constructors.
   */
  private ReG_SteerParameter(String label, boolean steer, String min, String max, int handle) {
    this.handle = handle;
    this.label = label;
    steerable = steer;
    minLabel = min;
    maxLabel = max;

    if(handle == REG_PARAM_HANDLE_NOTSET)
      registered = false;
    else
      registered = true;
  }

  /**
   * Create a parameter with upper and lower constraints and an
   * internal handle.
   *
   * @param label a descriptive label for the parameter.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param value the value of the parameter.
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   * @param handle the internal handle of the parameter as set by the Steering Linrary.   */
  public ReG_SteerParameter(String label, boolean steer, T value, String min, String max, int handle) {
    this(label, steer, min, max, handle);

    if(value.getClass().getName().equals("java.lang.Integer")) {
      type = REG_INT;
      intParam = new Intp();
    }
    else if(value.getClass().getName().equals("java.lang.Float")) {
      type = REG_FLOAT;
      floatParam = new Floatp();
    }
    else if(value.getClass().getName().equals("java.lang.Double")) {
      type = REG_DOUBLE;
      doubleParam = new Doublep();
    }

    setValue(value);
  }

  /**
   * Create a parameter with upper and lower constraints but no internal handle.
   *
   * @param label a descriptive label for the parameter.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param value the value of the parameter.
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   */
  public ReG_SteerParameter(String label, boolean steer, T value, String min, String max) {
    this(label, steer, value, min, max, REG_PARAM_HANDLE_NOTSET);
  }

  /**
   * Create a parameter with no minimum or maximum bounds
   * and no internal handle.
   *
   * @param label a descriptive label for the parameter.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param value the value of the parameter.
   */
  public ReG_SteerParameter(String label, boolean steer, T value) {
    this(label, steer, value, "", "", REG_PARAM_HANDLE_NOTSET);
  }

  /**
   * Register this parameter with the steering library.
   *
   * @throws ReG_SteerException If registering the parameter with the
   * steering library fails, or if the parameter is already registered with the
   * steering library, the error code will be <code>REG_FAILURE</code>.
   * @see ReG_SteerAppside#registerParam(ReG_SteerParameter)
   */
  public void register() throws ReG_SteerException {
    if(registered) {
      throw new ReG_SteerException("This parameter (" + label + ") is already registered.", REG_FAILURE);
    }

    int steered = REG_FALSE;
    if(steerable)
      steered = REG_TRUE;
    
    int status = ReG_Steer.Register_param(label, steered, getVoidPointer(),
					  type, minLabel, maxLabel);

    if(status != REG_SUCCESS) {
      throw new ReG_SteerException("Failed to register parameter.", status);
    }

    registered = true;
  }

  /**
   * Set the value of the steered parameter.
   *
   * @param value the new value of the parameter.
   */
  public void setValue(T value) {
    switch(type) {
    case REG_INT:
      intParam.assign(value.intValue());
      break;
    case REG_FLOAT:
      floatParam.assign(value.floatValue());
      break;
    case REG_DBL:
      doubleParam.assign(value.doubleValue());
      break;
    }
  }

  /**
   * Get the current value of the parameter.
   *
   * @return the value of the parameter.
   */
  @SuppressWarnings("unchecked")
  public T getValue() {
    T result;

    switch(type) {
    case REG_INT:
      result = (T) new Integer(intParam.value());
      break;
    case REG_FLOAT:
      result = (T) new Float(floatParam.value());
      break;
    case REG_DBL:
      result = (T) new Double(doubleParam.value());
      break;
    default:
      result = null;
    }

    return result;
  }

  /**
   * Provides a C/C++ style void pointer to this parameter. This is actually
   * a pointer to the internal SWIG type used by this class rather than a
   * pointer to itself.
   *
   * @return the void pointer to this object.
   */
  public SWIGTYPE_p_void getVoidPointer() {
    SWIGTYPE_p_void result = null;
    
    switch(type) {
    case REG_INT:
      result = intParam.cast().getVoidPointer();
      break;
    case REG_FLOAT:
      result = floatParam.cast().getVoidPointer();
      break;
    case REG_DBL:
      result = doubleParam.cast().getVoidPointer();
      break;
    }

    return result;
  }

  /**
   * Get the handle of this parameter. This will only have been set if the
   * parameter has been returned by the steering library.
   *
   * @return handle;
   */
  public int getHandle() {
    return handle;
  }

  /**
   * Get the text label of this parameter.
   *
   * @return the text label of this parameter.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Determine whether this parameter is steerable or not.
   *
   * @return true if steerable, false otherwise.
   */
  public boolean isSteerable() {
    return steerable;
  }

  /**
   * Get the type of this parameter. This will be REG_INT, REG_FLOAT or
   * REG_DBL.
   *
   * @return the type of this parameter. (REG_INT, REG_FLOAT or
   * REG_DBL.)
   */
  public int getType() {
    return type;
  }

  /**
   * Get the minimum label for this parameter.
   *
   * @return the minimum text label for this parameter.
   */
  public String getMinLabel() {
    return minLabel;
  }

  /**
   * Get the maximum label for this parameter.
   *
   * @return the maximum text label for this parameter.
   */
  public String getMaxLabel() {
    return maxLabel;
  }

  /**
   * Determine whether this parameter has been registered with the steering
   * library or not.
   *
   * @return true if the parameter has been registered with the steering
   * library, false otherwise.
   */
  public boolean isRegistered() {
    return registered;
  }

  /**
   * Set the registered state of this parameter. This method is
   * <code>protected</code> as it should be used with care and it should only
   * be subclasses or objects in the same package that need to do this.
   *
   * @param toggle whether this parameter is to be treated as having been
   * registered with the steering library or not.
   */
  protected void setRegistered(boolean toggle) {
    registered = toggle;
  }

  /**
   * Format this instance of ReG_SteerParameter in a manner suitable for
   * printing to a terminal window.
   *
   * @return a string representation of this object.
   */
  public String toString() {
    String result = "";

    if(handle != REG_PARAM_HANDLE_NOTSET) {
      result += "Handle: " + handle + ", ";
    }
    result += "Label: " + label;
    result += ", Type: " + ReG_SteerUtilities.lookupType(type);
    result += ", Bounds: (" + minLabel + ", " + maxLabel + ")";
    result += ", Value: " + getValue();

    return result;
  }

  /**
   * A factory method to create an integer ReG_SteerParameter. The value of the
   * parameter is set but it is not registered with the steering library.
   *
   * @param label a descriptive label for the parameter.
   * @param value the value to set the parameter to be.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   * @return the created ReG_SteerParameter.
   * @see ReG_SteerParameter#create(java.lang.String,float,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,double,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,java.lang.String,boolean,int,java.lang.String,java.lang.String,int)
   */
  public static ReG_SteerParameter create(String label, int value, boolean steer, String min, String max) {
    return new ReG_SteerParameter<Integer>(label, steer, value, min, max);
  }

  /**
   * A factory method to create a float ReG_SteerParameter. The value of the
   * parameter is set but it is not registered with the steering library.
   *
   * @param label a descriptive label for the parameter.
   * @param value the value to set the parameter to be.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   * @return the created ReG_SteerParameter.
   * @see ReG_SteerParameter#create(java.lang.String,int,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,double,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,java.lang.String,boolean,int,java.lang.String,java.lang.String,int)
   */
  public static ReG_SteerParameter create(String label, float value, boolean steer, String min, String max) {
    return new ReG_SteerParameter<Float>(label, steer, value, min, max);    
  }

  /**
   * A factory method to create a double ReG_SteerParameter. The value of the
   * parameter is set but it is not registered with the steering library.
   *
   * @param label a descriptive label for the parameter.
   * @param value the value to set the parameter to be.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   * @return the created ReG_SteerParameter.
   * @see ReG_SteerParameter#create(java.lang.String,int,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,float,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,java.lang.String,boolean,int,java.lang.String,java.lang.String,int)
   */
  public static ReG_SteerParameter create(String label, double value, boolean steer, String min, String max) {
    return new ReG_SteerParameter<Double>(label, steer, value, min, max);    
  }

  /**
   * A factory method to create a ReG_SteerParameter. This form of
   * <code>create</code> is mainly used internally by the Java wrappers to
   * create <code>ReG_SteerParameter</code>s when returning them from the
   * steering library.
   *
   * @param label a descriptive label for the parameter.
   * @param steer set the parameter to be steerable (if true) or just
   * monitored (if false).
   * @param value the value of the parameter as a <code>String</code>. This is
   * parsed into a numeric value internally.
   * @param type the type of the parameter. (<code>REG_INT</code>,
   * <code>REG_FLOAT</code> or <code>REG_DBL</code>).
   * @param min the minimum value that a parameter can be steered to (can
   * be empty but not null).
   * @param max the maximum value that a parameter can be steered to (can
   * be empty but not null).
   * @param handle the internal handle of the parameter as set by the Steering Linrary.
   * @return the created ReG_SteerParameter.
   * @see ReG_SteerParameter#create(java.lang.String,int,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,float,boolean,java.lang.String,java.lang.String)
   * @see ReG_SteerParameter#create(java.lang.String,double,boolean,java.lang.String,java.lang.String)
   */
  public static ReG_SteerParameter create(String label, String value, boolean steer, int type, String min, String max, int handle) {

    ReG_SteerParameter result = null;

    switch(type) {
    case REG_INT:
      int ival = 0;
      if(value.length() > 0)
	ival = Integer.parseInt(value);
      result = new ReG_SteerParameter<Integer>(label, steer, ival, min, max, handle);
      break;
    case REG_FLOAT:
      float fval = 0.0f;
      if(value.length() > 0)
	fval = Float.parseFloat(value);
      result = new ReG_SteerParameter<Float>(label, steer, fval, min, max, handle);
      break;
    case REG_DBL:
      double dval = 0.0;
      if(value.length() > 0)
	dval = Double.parseDouble(value);
      result = new ReG_SteerParameter<Double>(label, steer, dval, min, max, handle);
      break;
    }

    return result;
  }
}
