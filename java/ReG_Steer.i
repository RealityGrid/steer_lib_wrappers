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
#include "ReG_Steer_Steerside.h"
%}

/* Ensure that all #defined constants are wrapped in a sensible
 * mannner and then pull in the ReG_Steer_types header.  */
%javaconst(1);
%include "ReG_Steer_types.h"

/* Ensure that the initial module wrapping has package level
 * access so that the general public use the ReG_SteerAppside
 * wrapper class for access to the steering library instead. */
%pragma(java) moduleclassmodifiers="class"

/* Let the JNI bit of the wrapper code load in the native library
 * seeing as we'll need it every time anyway. */
%pragma(java) jniclasscode=%{
  static {
    try {
      System.loadLibrary("ReG_SteerJava");
    } catch(UnsatisfiedLinkError e) {
      System.err.println("Failed to load native code library:\n" + e);
      System.exit(1);
    }
  }
%}

/* Set up pointers */
%include "cpointer.i"
%pointer_class(int, Intp);
%pointer_class(char, Charp);
%pointer_class(float, Floatp);
%pointer_class(double, Doublep);
//%pointer_class(byte, Bytep);

/*
 * The following two typemaps add the ReG_SWIG baseclass and
 * getPointer to the SWIGTYPE classes that are auto-generated
 */
%typemap(javabase) SWIGTYPE, SWIGTYPE *, SWIGTYPE &, SWIGTYPE [], 
                                                    SWIGTYPE (CLASS::*) "ReG_SWIG"

%typemap(javacode) SWIGTYPE, SWIGTYPE *, SWIGTYPE &, SWIGTYPE [], 
                                                    SWIGTYPE (CLASS::*) %{
  protected long getPointer() {
    return swigCPtr;
  }
%}

/* 
 * A set of typemaps to convert a java array of ints into a
 * C array of ints and a length variable
 */
%typemap(in) (int length, int *array) {
  $1 = (int) (*jenv)->GetArrayLength(jenv, $input);
  $2 = (int*) (*jenv)->GetIntArrayElements(jenv, $input, 0);
}
%typemap(freearg) (int length, int *array) {
  if($2) (*jenv)->ReleaseIntArrayElements(jenv, $input, $2, JNI_ABORT);
}
%typemap(jni) (int length, int *array) "jintArray"
%typemap(jtype) (int length, int *array) "int[]"
%typemap(jstype) (int length, int *array) "int[]"
%typemap(javain) (int length, int *array) "$javainput"

/*
 * A set of typemaps to update the contents of arrays of strings
 */
%typemap(in) char **update (jint len) {
  int i;
  if(!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "array null");
    return $null;
  }
  
  len = (*jenv)->GetArrayLength(jenv, $input);
  $1 = (char**) malloc(len * sizeof(char*));
  for(i = 0; i < len; i++) {
    $1[i] = (char*) malloc(REG_MAX_STRING_LENGTH * sizeof(char));
  }
}
%typemap(argout) char **update {
  int i;
  jstring temp_string;
  for(i = 0; i < len$argnum; i++) {
    temp_string = (*jenv)->NewStringUTF(jenv, $1[i]);
    (*jenv)->SetObjectArrayElement(jenv, $input, i, temp_string);
    (*jenv)->DeleteLocalRef(jenv, temp_string);
  }
}
%typemap(freearg) char **update {
  int i;
  for(i = 0; i < len$argnum; i++) 
    if($1[i]) free($1[i]);
  if($1) free($1);
}
%typemap(jni) char **update "jobjectArray"
%typemap(jtype) char **update "String[]"
%typemap(jstype) char **update "String[]"
%typemap(javain) char **update "$javainput"

/*
 * A set of typemaps for the data emitter method
 */
%typemap(in, numinputs=0) (int type, int count) {
  /* Just throw these away as we can work them out! */
}
%typemap(check) (int type, int count, void* indata) {
  if((*jenv)->IsInstanceOf(jenv, jarg4, (*jenv)->FindClass(jenv, "[I")) == JNI_TRUE) {
    $1 = REG_INT;
    $2 = (*jenv)->GetArrayLength(jenv, jarg4);
    $3 = (int*) malloc($2 * sizeof(int));
    (*jenv)->GetIntArrayRegion(jenv, jarg4, 0, $2, $3);
  }
  else if((*jenv)->IsInstanceOf(jenv, jarg4, (*jenv)->FindClass(jenv, "[F")) == JNI_TRUE) {
    $1 = REG_FLOAT;
    $2 = (*jenv)->GetArrayLength(jenv, jarg4);
    $3 = (float*) malloc($2 * sizeof(float));
    (*jenv)->GetFloatArrayRegion(jenv, jarg4, 0, $2, $3);
  }
  else if((*jenv)->IsInstanceOf(jenv, jarg4, (*jenv)->FindClass(jenv, "[D")) == JNI_TRUE) {
    $1 = REG_DBL;
    $2 = (*jenv)->GetArrayLength(jenv, jarg4);
    $3 = (double*) malloc($2 * sizeof(double));
    (*jenv)->GetDoubleArrayRegion(jenv, jarg4, 0, $2, $3);
  }
  else if((*jenv)->IsInstanceOf(jenv, jarg4, (*jenv)->FindClass(jenv, "[B")) == JNI_TRUE) {
    $1 = REG_CHAR;
    $2 = (*jenv)->GetArrayLength(jenv, jarg4);
    $3 = (char*) malloc($2 * sizeof(char));
    (*jenv)->GetByteArrayRegion(jenv, jarg4, 0, $2, $3);
  }
  else if((*jenv)->IsInstanceOf(jenv, jarg4, (*jenv)->FindClass(jenv, "java/lang/String")) == JNI_TRUE) {
    jboolean isCopy;

    $1 = REG_CHAR;
    $2 = (*jenv)->GetStringUTFLength(jenv, jarg4) + 1;
    $3 = (char*) malloc($2 * sizeof(char));
    const char* cArray = (*jenv)->GetStringUTFChars(jenv, jarg4, &isCopy);
    strncpy($3, cArray, $2);
    if(isCopy == JNI_TRUE)
      (*jenv)->ReleaseStringUTFChars(jenv, jarg4, cArray);
  }
}
%typemap(freearg) void* indata {
  if($1) free($1);
}
%typemap(jni) void* indata "jobject"
%typemap(jtype) void* indata "Object"
%typemap(jstype) void* indata "Object"
%typemap(javain) void* indata "$javainput"

/*
 * A set of typemaps for the data consumer method
 */
%typemap(in, numinputs=0) void* outdata {
  /* Just throw outdata away from the inputs! */
}
%typemap(check) (int type, int count, void *outdata) {
  switch($1) {
  case REG_INT:
    $3 = (int*) malloc($2 * sizeof(int));
    break;
  case REG_CHAR:
    $3 = (char*) malloc($2 * sizeof(char));
    break;
  case REG_FLOAT:
    $3 = (float*) malloc($2 * sizeof(float));
    break;
  case REG_DBL:
    $3 = (double*) malloc($2 * sizeof(double));
    break;
  }
}
%typemap(argout) (int type, int count, void* outdata) {
  jintArray iArray;
  jbyteArray bArray;
  jstring cString;
  jfloatArray fArray;
  jdoubleArray dArray;

  switch($1) {
  case REG_INT:
    iArray = (*jenv)->NewIntArray(jenv, $2);
    (*jenv)->SetIntArrayRegion(jenv, iArray, 0, $2, $3);
    $result = iArray;
    break;
  case REG_CHAR:
    if(((char*) $3)[$2 - 1] == '\0') {
      cString = (*jenv)->NewStringUTF(jenv, $3);
      $result = cString;
    }
    else {
      bArray = (*jenv)->NewByteArray(jenv, $2);
      (*jenv)->SetByteArrayRegion(jenv, bArray, 0, $2, $3);
      $result = bArray;
    }
    break;
  case REG_FLOAT:
    fArray = (*jenv)->NewFloatArray(jenv, $2);
    (*jenv)->SetFloatArrayRegion(jenv, fArray, 0, $2, $3);
    $result = fArray;
    break;
  case REG_DBL:
    dArray = (*jenv)->NewDoubleArray(jenv, $2);
    (*jenv)->SetDoubleArrayRegion(jenv, dArray, 0, $2, $3);
    $result = dArray;
    break;
  default:
    $result = $null;
    break;
  }
  
}
%typemap(freearg) void* outdata {
  if($1) free($1);
}
%typemap(jni) void* outdata "jobject"
%typemap(jtype) void* outdata "Object"
%typemap(jstype) void* outdata "Object"
%typemap(javain) void* outdata "$javainput"
%typemap(javaout) void* outdata {
  return $jnicall;
}

/*
 * A set of typemaps for the get_param_details method
 */
%typemap(in, numinputs=0) Param_details_struct* outdetails {
  /* Just throw outdata away from the inputs! */
}
%typemap(check) (int count, Param_details_struct* outdetails) {
  $2 = (Param_details_struct*) malloc($1 * sizeof(Param_details_struct));
}
%typemap(argout) (int steer, int count, Param_details_struct* outdetails) {
  jclass regParam;
  jobjectArray paramArray;

  regParam = (*jenv)->FindClass(jenv, "org/realitygrid/steering/ReG_SteerParameter");
  paramArray = (*jenv)->NewObjectArray(jenv, $2, regParam, NULL);

  if($2 > 0) {
    int i;
    jmethodID regParamCreate;
    jvalue args[7];
    jobject newObject;

    /* Set up access to the java classes and methods */
    regParamCreate = (*jenv)->GetStaticMethodID(jenv, regParam, "create", "(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/String;Ljava/lang/String;I)Lorg/realitygrid/steering/ReG_SteerParameter;");

    for(i = 0; i < $2; i++) {
      args[0].l = (*jenv)->NewStringUTF(jenv, $3[i].label);
      args[1].l = (*jenv)->NewStringUTF(jenv, $3[i].value);
      args[2].z = ($1 == REG_TRUE) ? JNI_TRUE : JNI_FALSE;
      args[3].i = $3[i].type;
      args[4].l = (*jenv)->NewStringUTF(jenv, $3[i].min_val);
      args[5].l = (*jenv)->NewStringUTF(jenv, $3[i].max_val);
      args[6].i = $3[i].handle;

      newObject = (*jenv)->CallStaticObjectMethodA(jenv, regParam, regParamCreate, args);
      (*jenv)->SetObjectArrayElement(jenv, paramArray, i, newObject);
    }
  }
  $result = paramArray;
}
%typemap(freearg) Param_details_struct* outdetails {
  if($1) free($1);
}
%typemap(jni) Param_details_struct* outdetails "jobjectArray"
%typemap(jtype) Param_details_struct* outdetails "Object[]"
%typemap(jstype) Param_details_struct* outdetails "Object[]"
%typemap(javain) Param_details_struct* outdetails "$javainput"
%typemap(javaout) Param_details_struct* outdetails {
  return $jnicall;
}

/*
 * A set of typemaps to cope with reg_security_info passed in
 */
%typemap(in) const struct reg_security_info* sec_in {
  jclass regSec;
  jmethodID getMethod;
  jboolean usingSSL;
  jboolean isStringCopy;
  jobject secString;
  const char* cString;

  // malloc C security struct
  $1 = 0;
  $1 = (struct reg_security_info*) malloc(sizeof(struct reg_security_info));
  if(!$1) return 0;

  // get Java security class
  regSec = (*jenv)->FindClass(jenv, "org/realitygrid/steering/ReG_SteerSecurity");

  // use_ssl?
  getMethod = (*jenv)->GetMethodID(jenv, regSec, "isUsingSSL", "()Z");
  usingSSL = (*jenv)->CallBooleanMethod(jenv, $input, getMethod);
  $1->use_ssl = (usingSSL == JNI_TRUE) ? 1 : 0;

  // caCertsPath
  getMethod = (*jenv)->GetMethodID(jenv, regSec, "getCaCertsPath", "()Ljava/lang/String;");
  secString = (*jenv)->CallObjectMethod(jenv, $input, getMethod);
  cString = (*jenv)->GetStringUTFChars(jenv, (jstring) secString, &isStringCopy);
  strncpy($1->caCertsPath, cString, REG_MAX_STRING_LENGTH);
  if(isStringCopy == JNI_TRUE) {
    (*jenv)->ReleaseStringUTFChars(jenv, secString, cString);
  }

  // myKeyCertFile
  getMethod = (*jenv)->GetMethodID(jenv, regSec, "getMyKeyCertFile", "()Ljava/lang/String;");
  secString = (*jenv)->CallObjectMethod(jenv, $input, getMethod);
  cString = (*jenv)->GetStringUTFChars(jenv, (jstring) secString, &isStringCopy);
  strncpy($1->myKeyCertFile, cString, REG_MAX_STRING_LENGTH);
  if(isStringCopy == JNI_TRUE) {
    (*jenv)->ReleaseStringUTFChars(jenv, secString, cString);
  }

  // userDN
  getMethod = (*jenv)->GetMethodID(jenv, regSec, "getUserDN", "()Ljava/lang/String;");
  secString = (*jenv)->CallObjectMethod(jenv, $input, getMethod);
  cString = (*jenv)->GetStringUTFChars(jenv, (jstring) secString, &isStringCopy);
  strncpy($1->userDN, cString, REG_MAX_STRING_LENGTH);
  if(isStringCopy == JNI_TRUE) {
    (*jenv)->ReleaseStringUTFChars(jenv, secString, cString);
  }

  // passphrase
  getMethod = (*jenv)->GetMethodID(jenv, regSec, "getPassphrase", "()Ljava/lang/String;");
  secString = (*jenv)->CallObjectMethod(jenv, $input, getMethod);
  cString = (*jenv)->GetStringUTFChars(jenv, (jstring) secString, &isStringCopy);
  strncpy($1->passphrase, cString, REG_MAX_STRING_LENGTH);
  if(isStringCopy == JNI_TRUE) {
    (*jenv)->ReleaseStringUTFChars(jenv, secString, cString);
  }
}
%typemap(freearg) const struct reg_security_info* sec_in {
  if($1) free($1);
}
%typemap(jni) const struct reg_security_info* sec_in "jobject"
%typemap(jtype) const struct reg_security_info* sec_in "Object"
%typemap(jstype) const struct reg_security_info* sec_in "Object"
%typemap(javain) const struct reg_security_info* sec_in "$javainput"

/*
 * A set of typemaps to cope with reg_security_info passed out
 */
%typemap(in, numinputs=0) struct reg_security_info* sec_out {
  /* Just throw $1 away from the inputs! */
}
%typemap(check) struct reg_security_info* sec_out {
  $1 = 0;
  $1 = (struct reg_security_info*) malloc(sizeof(struct reg_security_info));
  if(!$1) {
    return 0;
  }
}
%typemap(argout) struct reg_security_info* sec_out {
  jclass regSec;
  jmethodID regSecCons;
  jvalue args[5];
  jobject newObject;

  regSec = (*jenv)->FindClass(jenv, "org/realitygrid/steering/ReG_SteerSecurity");
  regSecCons = (*jenv)->GetMethodID(jenv, regSec, "<init>", "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

  args[0].z = ($1->use_ssl == 1) ? JNI_TRUE : JNI_FALSE;
  args[1].l = (*jenv)->NewStringUTF(jenv, $1->caCertsPath);
  args[2].l = (*jenv)->NewStringUTF(jenv, $1->myKeyCertFile);
  args[3].l = (*jenv)->NewStringUTF(jenv, $1->userDN);
  args[4].l = (*jenv)->NewStringUTF(jenv, $1->passphrase);

  newObject = (*jenv)->NewObjectA(jenv, regSec, regSecCons, args);

  $result = newObject;
}
%typemap(freearg) struct reg_security_info* sec_out {
  if($1) free($1);
}
%typemap(jni) struct reg_security_info* sec_out "jobject"
%typemap(jtype) struct reg_security_info* sec_out "Object"
%typemap(jstype) struct reg_security_info* sec_out "Object"
%typemap(javain) struct reg_security_info* sec_out "$javainput"
%typemap(javaout) struct reg_security_info* sec_out {
  return $jnicall;
}

/*
 * A set of typemaps for the registry_contents struct passed out
 */
%typemap(in, numinputs=0) struct registry_contents* cont_out {
  /* Just throw $1 away from the inputs! */
}
%typemap(check) struct registry_contents* cont_out {
  $1 = 0;
  $1 = (struct registry_contents*) malloc(sizeof(struct registry_contents));
  if(!$1) {
    return 0;
  }
}
%typemap(argout) struct registry_contents* cont_out {
  int num = $1->numEntries;
  jclass regEntry;
  jobjectArray entryArray;

  regEntry = (*jenv)->FindClass(jenv, "org/realitygrid/steering/ReG_SteerRegistryEntry");
  entryArray = (*jenv)->NewObjectArray(jenv, num, regEntry, NULL);

  if(num > 0) {
    int i;
    jmethodID regEntryCons;
    jvalue args[8];
    jobject newObject;
    
    regEntryCons = (*jenv)->GetMethodID(jenv, regEntry, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    for(i = 0; i < num; i++) {
      args[0].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].service_type);
      args[1].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].gsh);
      args[2].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].entry_gsh);
      args[3].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].application);
      args[4].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].start_date_time);
      args[5].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].user);
      args[6].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].group);
      args[7].l = (*jenv)->NewStringUTF(jenv, $1->entries[i].job_description);
      
      newObject = (*jenv)->NewObjectA(jenv, regEntry, regEntryCons, args);
      (*jenv)->SetObjectArrayElement(jenv, entryArray, i, newObject);
    }
  }

  $result = entryArray;
}
%typemap(freearg) struct registry_contents* cont_out {
  if($1) {
    Delete_registry_table($1);
    free($1);
  }
}
%typemap(jni) struct registry_contents* cont_out "jobjectArray"
%typemap(jtype) struct registry_contents* cont_out "Object[]"
%typemap(jstype) struct registry_contents* cont_out "Object[]"
%typemap(javain) struct registry_contents* cont_out "$javainput"
%typemap(javaout) struct registry_contents* cont_out {
  return $jnicall;
}


/*
 * A set of typemaps to accept arrays of Strings as arrays of char*
 */
%typemap(in) char **inStrings(jint len) {
  int i;
  if(!$input) {
    $1 = NULL;
    len = 0;
  }
  else {
    len = (*jenv)->GetArrayLength(jenv, $input);
    $1 = (char**) malloc((len + 1) * sizeof(char*));
    /* make a copy of each string */
    for(i = 0; i < len; i++) {
      jstring jString = (jstring)(*jenv)->GetObjectArrayElement(jenv, $input, i);
      const char* cString = (*jenv)->GetStringUTFChars(jenv, jString, 0);
      $1[i] = malloc(strlen((cString) + 1) * sizeof(const char*));
      strcpy($1[i], cString);
      (*jenv)->ReleaseStringUTFChars(jenv, jString, cString);
      (*jenv)->DeleteLocalRef(jenv, jString);
    }
    $1[i] = 0;
  }
}
%typemap(freearg) char **inStrings {
  int i;
  for(i = 0; i < len$argnum-1; i++)
    if($1[i]) free($1[i]);
  if($1) free($1);
}
%typemap(jni) char **inStrings "jobjectArray"
%typemap(jtype) char **inStrings "String[]"
%typemap(jstype) char **inStrings "String[]"
%typemap(javain) char **inStrings "$javainput"

/*
 * A set of typemaps to accept arrays of ints as int* (and handle NULL!)
 */
%typemap(in) int *inInts(jint len) {
  if(!$input) {
    $1 = NULL;
    len = 0;
  }
  else {
    len = (*jenv)->GetArrayLength(jenv, $input);
    $1 = (int*) malloc(len * sizeof(int));
    (*jenv)->GetIntArrayRegion(jenv, $input, 0, (len - 1), $1);
  }
}
%typemap(freearg) int *inInts {
  if($1) free($1);
}
%typemap(jni) int *inInts "jobjectArray"
%typemap(jtype) int *inInts "int[]"
%typemap(jstype) int *inInts "int[]"
%typemap(javain) int *inInts "$javainput"



/* Strings and arrays stuff */
%include "arrays_java.i"



/* Apply typemaps to the required variables */
%apply (int length, int *array) { (int NumSupportedCmds, int *SupportedCmds) }

%apply char **update {
  char** SteerParamLabels,
  char** SteerCmdParams,
  char** simName,
  char** simGSH,
  char** chk_labels,
  char** io_labels
}

%apply int[] {
  int *SteerCommands,
  int *cmd_ids,
  int *handles,
  int *chk_handles,
  int *chk_types,
  int *chk_freqs,
  int *io_handles,
  int *io_types,
  int *io_freqs,
  int *Commands
};

%apply (int type, int count, void* outdata) {
  (int DataType, int Count, void* pDataOUT)
}
%apply void* outdata { void* pDataOUT }

%apply (int type, int count, void* indata) {
  (int DataTypeIN, int CountIN, void* pDataIN)
}
%apply (int type, int count) { (int DataTypeIN, int CountIN) }
%apply void* indata { void* pDataIN }

%apply (int steer, int count, Param_details_struct* outdetails) {
  (int steerable, int num_params, Param_details_struct* param_details)
}
%apply (int count, Param_details_struct* outdetails) {
  (int num_params, Param_details_struct* param_details)
}
%apply Param_details_struct* outdetails { Param_details_struct* param_details }

%apply const struct reg_security_info* sec_in { const struct reg_security_info* secIn }

%apply struct reg_security_info* sec_out { struct reg_security_info* secOut }

%apply struct registry_contents* cont_out { struct registry_contents* regContsOut }

%apply char **inStrings {
  char **SysCmdParams,
  char* *vals
}
%apply int *inInts { int *SysCommands }



/* Pull in the common API definition file */
%include "ReG_Steer_API.i"



/* Re-define the Consume_data_slice method to return an object
 * this is done by creating a new version of the method and
 * calling that instead.: Consume_data_slice_j */
%inline %{
  jobject Consume_data_slice_j(int IOTypeIndex, int DataType, int Count, void* pDataOUT) {
    Consume_data_slice(IOTypeIndex, DataType, Count, pDataOUT);
    return 0;
  }
%}
jobject Consume_data_slice_j(int IOTypeIndex, int DataType, int Count, void* pDataOUT);

/* Re-define the Get_param_values method to return an objectArray
 * this is done by creating a new version of the method and
 * calling that instead.: Get_param_values_j */
%inline %{
  jobjectArray Get_param_values_j(int sim_handle, int steerable, int num_params, Param_details_struct *param_details) {
    Get_param_values(sim_handle, steerable, num_params, param_details);
    return 0;
  }
%}
jobjectArray Get_param_values_j(int sim_handle, int steerable, int num_params, Param_details_struct *param_details);

/* Re-define the Get_security_config method to return an object
 * this is done by creating a new version of the method and
 * calling that instead.: Get_security_config_j */
%inline %{
  jobject Get_security_config_j(const char* configFile,
				struct reg_security_info* secOut) {
    Get_security_config(configFile, secOut);
    return 0;
  }
%}
jobject Get_security_config_j(const char* configFile,
			      struct reg_security_info* secOut);

/* Re-define the Get_registry_entries_secure method to return an
 * objectArray. This is done by creating a new version of the method
 * and calling that instead.: Get_registry_entries_secure_j */
%inline %{
  jobjectArray Get_registry_entries_secure_j(const char* registryGSH,
				       const struct reg_security_info* secIn,
				       struct registry_contents* regContsOut) {
    Get_registry_entries_secure(registryGSH, secIn, regContsOut);
  }
%}
jobjectArray Get_registry_entries_secure_j(const char* registryGSH,
					const struct reg_security_info* secIn,
					struct registry_contents* regContsOut);

/* Re-define the Get_registry_entries_filtered_secure method to return an
 * objectArray. This is done by creating a new version of the method
 * and calling that instead.: Get_registry_entries_filtered_secure_j */
%inline %{
  jobjectArray Get_registry_entries_filtered_secure_j(const char* registryGSH,
				       const struct reg_security_info* secIn,
				       struct registry_contents* regContsOut,
				       char* pattern) {
    Get_registry_entries_filtered_secure(registryGSH, secIn, regContsOut, pattern);
  }
%}
jobjectArray Get_registry_entries_filtered_secure_j(const char* registryGSH,
					const struct reg_security_info* secIn,
					struct registry_contents* regContsOut,
					char* pattern);
