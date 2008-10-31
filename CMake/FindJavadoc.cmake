#
#  CMake build machinery for the RealityGrid Steering Library Wrappers.
#
#  (C) Copyright 2004-2008, University of Manchester, United Kingdom,
#  all rights reserved.
#
#  This software is produced by the Supercomputing, Visualization and
#  e-Science Group, Manchester Computing, University of Manchester
#  as part of the RealityGrid project (http://www.realitygrid.org),
#  funded by the EPSRC under grants GR/R67699/01 and GR/R67699/02.
#
#  LICENCE TERMS
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions
#  are met:
#  1. Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#  2. Redistributions in binary form must reproduce the above copyright
#     notice, this list of conditions and the following disclaimer in the
#     documentation and/or other materials provided with the distribution.
#
#  THIS MATERIAL IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#  LIMITED  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
#  A PARTICULAR PURPOSE ARE DISCLAIMED. THE ENTIRE RISK AS TO THE QUALITY
#  AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE PROGRAM PROVE
#  DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR
#  CORRECTION.
#
#  Author.........: Robert Haines
#----------------------------------------------------------------------

# This module finds javadoc and sets the following
# variable:
#
#  JAVA_JAVADOC    = the full path to javadoc

set(JAVA_BIN_PATH
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\2.0;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.9;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.8;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.7;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.6;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.5;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.4;JavaHome]/bin"
    "[HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.3;JavaHome]/bin"
  $ENV{JAVA_HOME}/bin
  /usr/bin
  /usr/lib/java/bin
  /usr/share/java/bin
  /usr/local/bin
  /usr/local/java/bin
  /usr/local/java/share/bin
  /usr/java/j2sdk1.4.2_04
  /usr/lib/j2sdk1.4-sun/bin
  /usr/java/j2sdk1.4.2_09/bin
  /usr/lib/j2sdk1.5-sun/bin
  /opt/sun-jdk-1.5.0.04/bin
)

find_program(JAVA_JAVADOC
  NAMES javadoc
  PATHS ${JAVA_BIN_PATH}
)

mark_as_advanced(JAVA_JAVADOC)
