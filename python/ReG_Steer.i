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

%module ReG_Steer
%{
#include "ReG_Steer_Appside.h"
%}

%include "ReG_Steer_types.h"

/* Set up pointers */
%include "cpointer.i"

%pointer_class(int, intp);
%pointer_class(float, floatp);
%pointer_class(double, doublep);

/* Set up typemaps */
%include "typemaps.i"

/* A set of typemaps to convert a python list of ints and a length
 * variable to an array of ints */
%typemap(in) (int length, int *array) {
  int i;
  if(!PyList_Check($input)) {
    PyErr_SetString(PyExc_ValueError, "Expected a list");
    return NULL;
  }
  $1 = PyList_Size($input);
  int* temp = (int*) malloc($1 * sizeof(int));
  for(i = 0; i < $1; i++) {
    PyObject *o = PyList_GetItem($input, i);
    if(PyInt_Check(o)) {
      temp[i] = (int) PyInt_AsLong(o);
    }
    else {
      PyErr_SetString(PyExc_ValueError, "List elements must be integers");
      return NULL;
    }
  }
  $2 = temp;
}
%typemap(freearg) (int length, int *array) {
  if($2) free($2);
}

/* A set of typemaps to map a returned string array and length
 * variable into two python lists of strings and ints */
%typemap(in, numinputs=0) (int *length, char **outstrs) {
  int i;
  $1 = (int*) malloc(sizeof(int));
  $2 = (char**) malloc(REG_MAX_NUM_STR_PARAMS * sizeof(char*));
  for(i = 0; i < REG_MAX_NUM_STR_PARAMS; i++)
    $2[i] = (char*) malloc(REG_MAX_STRING_LENGTH * sizeof(char));
}
%typemap(argout) (int *length, char **outstrs) {
  int i;
  PyObject* outliststrs = PyList_New(*$1);
  for(i = 0; i < *$1; i++) {
    PyList_SetItem(outliststrs, i, PyString_FromString($2[i]));
  }
  $result = SWIG_Python_AppendOutput($result, outliststrs);
}
%typemap(freearg) (int *length, char **outstrs) {
  int i;
  if($1) free($1);
  for(i = 0; i < REG_MAX_NUM_STR_PARAMS; i++)
    if($2[i]) free($2[i]);
  if($2) free($2);
}

/* A set of typemaps to map a returned int array, string array and
 * length variable into two python lists of strings and ints */
%typemap(in, numinputs=0) (int *length, int *outints, char **outstrs) {
  int i;
  $1 = (int*) malloc(sizeof(int));
  $2 = (int*) malloc(REG_MAX_NUM_STR_CMDS * sizeof(int));
  $3 = (char**) malloc(REG_MAX_NUM_STR_CMDS * sizeof(char*));
  for(i = 0; i < REG_MAX_NUM_STR_CMDS; i++)
    $3[i] = (char*) malloc(REG_MAX_STRING_LENGTH * sizeof(char));
}
%typemap(argout) (int *length, int *outints, char **outstrs) {
  int i;
  PyObject* outlistints = PyList_New(*$1);
  PyObject* outliststrs = PyList_New(*$1);
  for(i = 0; i < *$1; i++) {
    PyList_SetItem(outlistints, i, PyInt_FromLong($2[i]));
    PyList_SetItem(outliststrs, i, PyString_FromString($3[i]));
  }
  $result = SWIG_Python_AppendOutput($result, outlistints);
  $result = SWIG_Python_AppendOutput($result, outliststrs);
}
%typemap(freearg) (int *length, int *outints, char **outstrs) {
  int i;
  if($1) free($1);
  if($2) free($2);
  for(i = 0; i < REG_MAX_NUM_STR_CMDS; i++) 
    if($3[i]) free($3[i]);
  if($3) free($3);
}

/* A set of typemaps to map a returned block of data into a
 * python list of the correct type */
%typemap(in, numinputs=0) void *outdata {
  /* Just throw outdata away from the inputs! */
}
%typemap(check) (int type, int count, void *outdata) {
  if($1 < 0 || $1 > 3) {
    printf("Type out of valid range!\n");
  }
  if($2 <= 0) {
    printf("Data count is zero!\n");
  }
  if(!$3) {
    switch($1) {
    case REG_INT:
      $3 = (void*) malloc($2 * sizeof(int));
      break;
    case REG_CHAR:
      $3 = (void*) malloc($2 * sizeof(char));
      break;
    case REG_FLOAT:
      $3 = (void*) malloc($2 * sizeof(float));
      break;
    case REG_DBL:
      $3 = (void*) malloc($2 * sizeof(double));
      break;
    }
  }
}
%typemap(argout) (int type, int count, void *outdata) {
  int i;

  PyObject* outlist;

  switch($1) {
  case REG_CHAR:
    outlist = PyList_New(1);
    PyList_SetItem(outlist, 0, PyString_FromString((char*) $3));
    break;
  case REG_INT:
    outlist = PyList_New($2);
    for(i = 0; i < $2; i++)
      PyList_SetItem(outlist, i, PyInt_FromLong(((int*) $3)[i]));
    break;
  case REG_FLOAT:
    outlist = PyList_New($2);
    for(i = 0; i < $2; i++)
      PyList_SetItem(outlist, i, PyFloat_FromDouble(((double*) $3)[i]));
    break;
  case REG_DBL:
    outlist = PyList_New($2);
    for(i = 0; i < $2; i++)
      PyList_SetItem(outlist, i, PyFloat_FromDouble(((double*) $3)[i]));
    break;
  }
  $result = SWIG_Python_AppendOutput($result, outlist);
}
%typemap(freearg) void *outdata {
  if($1) free($1);
}

/* Apply typemaps to the required variables */
%apply (int length, int *array) { (int NumSupportedCmds, int *SupportedCmds) }

%apply (int *length, char **outstrs) {
  (int *NumSteerParams, char **SteerParamLabels)
}

%apply (int *length, int *outints, char **outstrs) {
  (int *NumSteerCommands, int *SteerCommands, char **SteerCmdParams)
}

%apply void *outdata { void *pDataOUT }
%apply (int type, int count, void *outdata) {
  (int DataType, int Count, void *pDataOUT)
}

%apply int *OUTPUT {
  int *iotypehandle,
  int *ChkType,
  int *IOTypeIndex,
  int *IOHandle,
  int *DataType,
  int *DataCount
}

%apply int *INOUT {
  int *IOTypeIndexINOUT,
  int *IOHandleINOUT
}

%include "ReG_Steer_API.i"
